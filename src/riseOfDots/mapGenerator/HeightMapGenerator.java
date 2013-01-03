package riseOfDots.mapGenerator;

import java.util.Arrays;
import java.util.Random;

/**
 * Uses the Diamond-Square Algorithm to generate height map data. Generated data
 * is returned in a 2D array of doubles.
 * 
 * @author Kevin Sacro
 */
public class HeightMapGenerator {

	// the generated map's height and width will be equal to gensize
	private int gensize;
	private int width;
	private int height;
	private double variance;
	private Random r = new Random();

	public static void main(String[] args) {
		double[][] d;
		double highest = 0;
		double lowest = 0;
		HeightMapGenerator g = new HeightMapGenerator();
		g.setSize(600, 600);
		g.setVariance(100);
		d = g.generate();
		for (int x = 0; x < 600; x++)
			for (int y = 0; y < 600; y++) {
				if (d[x][y] > highest)
					highest = d[x][y];
				if (d[x][y] < lowest)
					lowest = d[x][y];
			}
		System.out.println("Highest: " + highest + "\nLowest: " + lowest);
	}

	/**
	 * Lone constructor.
	 */
	public HeightMapGenerator() {

		gensize = (int) Math.pow(2, 9) + 1;
		width = gensize;
		height = gensize;
		variance = 1;
	}

	/**
	 * Adjusts the height map dimensions.
	 * 
	 * @param width
	 *            - The desired width (in pixels) of generated maps.
	 * @param height
	 *            - The desired height (in pixels) of generated maps.
	 */
	public void setSize(int width, int height) {

		this.width = width;
		this.height = height;

		// gensize must be in the form 2^n + 1 and
		// also be greater or equal to both the width
		// and height
		double w = Math.ceil(Math.log(width) / Math.log(2));
		double h = Math.ceil(Math.log(height) / Math.log(2));

		if (w > h) {
			gensize = (int) Math.pow(2, w) + 1;
		} else {
			gensize = (int) Math.pow(2, h) + 1;
		}
	}
	
	/**
	 * Sets the seed for this height map, enabling the reproduction of a specific map
	 * through its seed.
	 * */
	public void setSeed(long seed){
		r.setSeed(seed);
	}

	/**
	 * Adjusts the height map dimensions.
	 * 
	 * @param n
	 *            - Sets the width and height of generated maps to be 2^n + 1
	 *            pixels.
	 */
	public void setGenerationSize(int n) {

		gensize = (int) Math.pow(2, n) + 1;
		width = gensize;
		height = gensize;
	}

	/**
	 * @param v
	 *            - The higher the variance, the rougher the height map. By
	 *            default it is 1.
	 */
	public void setVariance(double v) {
		variance = v;
	}

	/**
	 * Generates height map data and places it in a 2D array. A new height map
	 * is created everytime generate() is called.
	 * 
	 * @return A 2D array containing height map data.
	 */
	public double[][] generate() {
		System.gc();
		double[][] map = new double[gensize][gensize];

		// Place initial seeds for corners
		map[0][0] = r.nextDouble();
		map[0][map.length - 1] = r.nextDouble();
		map[map.length - 1][0] = r.nextDouble();
		map[map.length - 1][map.length - 1] = r.nextDouble();

		map = generate(map);

		if (width < gensize || height < gensize) {

			double[][] temp = new double[width][height];

			for (int i = 0; i < temp.length; i++) {
				temp[i] = Arrays.copyOf(map[i], temp[i].length);
			}

			map = temp;

		}

		return map;

	}

	/**
	 * Fills the specified 2D array with height map data and returns it. Any
	 * index whose value is not 0 will not be overwritten and used during
	 * procedural generation. This makes this method ideal for pre-seeding data
	 * to generate maps with specific features.
	 * 
	 * @param map
	 *            - 2D array containing height map data
	 * 
	 * @return A 2D array containing height map data.
	 */
	public double[][] generate(double[][] map) {

		map = map.clone();
		int step = map.length - 1;

		double v = variance;

		while (step > 1) {

			// SQUARE STEP
			for (int i = 0; i < map.length - 1; i += step) {
				for (int j = 0; j < map[i].length - 1; j += step) {

					double average = (map[i][j] + map[i + step][j]
							+ map[i][j + step] + map[i + step][j + step]) / 4;

					if (map[i + step / 2][j + step / 2] == 0) // check if not
																// pre-seeded
						map[i + step / 2][j + step / 2] = average
								+ randVariance(v);

				}
			}

			// DIAMOND STEP
			for (int i = 0; i < map.length - 1; i += step) {
				for (int j = 0; j < map[i].length - 1; j += step) {

					if (map[i + step / 2][j] == 0) // check if not pre-seeded
						map[i + step / 2][j] = averageDiamond(map,
								i + step / 2, j, step) + randVariance(v);

					if (map[i][j + step / 2] == 0)
						map[i][j + step / 2] = averageDiamond(map, i, j + step
								/ 2, step)
								+ randVariance(v);

					if (map[i + step][j + step / 2] == 0)
						map[i + step][j + step / 2] = averageDiamond(map, i
								+ step, j + step / 2, step)
								+ randVariance(v);

					if (map[i + step / 2][j + step] == 0)
						map[i + step / 2][j + step] = averageDiamond(map, i
								+ step / 2, j + step, step)
								+ randVariance(v);

				}
			}

			v /= 2;
			step /= 2;
		}

		return map;
	}

	private double averageDiamond(double[][] map, int x, int y, int step) {

		int count = 0;
		double average = 0;

		if (x - step / 2 >= 0) {
			count++;
			average += map[x - step / 2][y];
		}

		if (x + step / 2 < map.length) {
			count++;
			average += map[x + step / 2][y];
		}

		if (y - step / 2 >= 0) {
			count++;
			average += map[x][y - step / 2];
		}

		if (y + step / 2 < map.length) {
			count++;
			average += map[x][y + step / 2];
		}

		return average / count;
	}

	private double randVariance(double v) {
		return r.nextDouble() * 2 * v - v;
	}

}