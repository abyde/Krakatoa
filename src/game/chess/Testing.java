package game.chess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Testing {

	public static void main(String[] args) {
		try {
			String f = "iter.out";
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(f)));
			
			pw.println("t\t keys\t iter");
			pw.println("hello");
			pw.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
