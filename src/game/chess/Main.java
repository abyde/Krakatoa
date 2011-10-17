package game.chess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Main class. Does something.
 * 
 * @author andrewbyde
 */
public class Main {

	public static void main(String[] args) {

		// provide a trivial extension.
		Board b = new Board() {
			public boolean isLegalMove(Piece p, Location loc) {
				return true;
			}
		};
		String input;
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(b);
		help();

		// simple test application!
		try {
			do {
				System.out.print("\n> ");
				input = r.readLine();
				if (isExit(input))
					continue;

				try {
					// interpret input as a move of the form <loc1> <loc2>
					// must have two tokens, each of which is a valid location.
					StringTokenizer st = new StringTokenizer(input);
					if (!st.hasMoreTokens()) {
						help();
						continue;
					}
					String fromS = st.nextToken();

					if (!st.hasMoreTokens()) {
						help();
						continue;
					}
					String toS = st.nextToken();

					Location from = Location.parse(fromS);
					Location to = Location.parse(toS);

					// is there a piece to move?
					Piece pFrom = b.pieceAt(from);
					if (pFrom == null) {
						System.out.println("No piece at " + from);
						continue;
					} else {
						// right, now it might not be legal
						boolean isLegal = b.isLegalMove(pFrom, to);

						// move the piece
						if (isLegal) {
							b.movePiece(from, to);
							System.out.println(b);
						} else {
							System.out.println("Illegal move");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					help();
					// keep going!
				}
			} while (!isExit(input));
			System.out.println("\nGoodbye!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void help() {
		System.out
				.println("Usage: please enter either 'q' to quit, or <loc1> <loc2> to move a piece from loc1 to loc2.");
	}

	private static boolean isExit(String s) {
		if (s == null)
			return false;

		return "q".equals(s) || "quit".equals(s) || "exit".equals(s);
	}
}
