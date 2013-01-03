package outOfGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riseOfDots.mapGenerator.HeightMapGenerator;

public class MapGen implements ActionListener{
	
	BufferedImage map = null;
	
	JTextField seed = new JTextField();
	JTextField water = new JTextField();
	JTextField shore = new JTextField();
	JTextField size = new JTextField();
	
	JButton gen = new JButton("Gen");
	JButton rand = new JButton("Seed");
	
	JLabel img = new JLabel();
	
	public static void main(String[] args){
		new MapGen();
	}
	
	public MapGen(){
		JFrame frame = new JFrame("Map generator");
		frame.setLayout(new BorderLayout());
		img.setSize(600, 600);
		map = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < 600; x++)
			for(int y = 0; y < 600; y++)
				map.setRGB(x, y, 0);
		img.setIcon(new ImageIcon(map));
		frame.add(img, BorderLayout.CENTER);
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(9, 2, 3, 3));
		pane.add(new JLabel(" "));
		pane.add(new JLabel(" "));
		pane.add(new JLabel(" "));
		pane.add(new JLabel(" "));
		pane.add(new JLabel("       Seed:"));
		pane.add(seed);
		pane.add(new JLabel("Water level:"));
		pane.add(water);
		pane.add(new JLabel("Shore level:"));
		pane.add(shore);
		pane.add(new JLabel("   Map size:"));
		pane.add(size);
		pane.add(gen);
		pane.add(rand);
		pane.add(new JLabel(" "));
		pane.add(new JLabel(" "));
		pane.add(new JLabel(" "));
		pane.add(new JLabel(" "));
		frame.add(pane, BorderLayout.EAST);
		gen.addActionListener(this);
		rand.addActionListener(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == rand){
			seed.setText(""+((int) (Math.random() * 1000000000)));
		}
		else if(evt.getSource() == gen){
			genMap();
		}
	}
	
	public void genMap(){
		HeightMapGenerator g = new HeightMapGenerator();
		g.setSeed(getSeed());
		int s = Integer.parseInt(size.getText());
		//int s = 600;
		double sea = Double.parseDouble(this.water.getText());
		double shore = Double.parseDouble(this.shore.getText());
		g.setGenerationSize(s);
		System.out.println(s);
		double[][] heightMap = new double[s][s];
        g.generate(heightMap);
		map = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
    	for(int x = 0; x < s; x++)
    		for(int y = 0; y < s; y++){
    			if(heightMap[x][y] <= sea){
    				map.setRGB(x, y, -16776961);
    			}
    			else if(heightMap[x][y] <= shore){
    				map.setRGB(x, y, -128);
    			}
    			else if(heightMap[x][y] > shore){
    				map.setRGB(x, y, -16744448);
    			}
    		}
		img.setIcon(new ImageIcon(map));
	}
	
	public long getSeed(){
		String s = seed.getText();
		boolean isNumerical = true;
		for(int i = 0; i < s.length(); i++)
			if(Character.isDigit(s.charAt(i))) isNumerical = false;
		if(isNumerical)
			return Long.parseLong(s);
		long seed = 1L;
		for(int i = 0; i < s.length(); i++)
			seed *= s.charAt(i);
		return seed;
	}
	
}
