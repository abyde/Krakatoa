package words;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Tester extends JFrame {

	public static void main(String[] args) {
		Properties props = new Properties(args);
		wordsPath = props.getProperty("wordsPath", "/Users/abyde/Documents/brasil/words.csv");
		long seed = props.getLong("seed", 1l);
		try {
			Tester t = new Tester(seed);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	class TesterCanvas extends Canvas implements ActionListener {
		String english;
		String portuguese;
		Font f;
		JTextField seedText = new JTextField();

		TesterCanvas() {
			f = new Font("Arial", Font.PLAIN, 30);
			seedText.setText("" + seed);
			seedText.addActionListener(this);
		}

		@Override
		public void paint(Graphics g) {
			g.setFont(f);

			// knows to do nothing if it's null
			drawWord(g, 100, english);
			drawWord(g, 200, portuguese);
			g.drawString("" + count, 10, 30);
		}

		private void drawWord(Graphics g, int y, String word) {
			if (word == null)
				return;
			FontMetrics fm = g.getFontMetrics();
			int width = fm.stringWidth(word);
			int x = (getWidth() - width) / 2;
			g.drawString(word, x, y);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = seedText.getText();
			System.out.println("Got action " + s);
			try {
				long seed = Long.parseLong(s);
				setSeed(seed);
			} catch (Exception e) {
				// alert
			}
		}
	}

	TesterCanvas c;
	long seed;
	int count = 0;
	Random r;
	List<String> ewords = new ArrayList<String>();
	List<String> pwords = new ArrayList<String>();
	Map<String, String> e2p = new HashMap<String, String>();
	Map<String, String> p2e = new HashMap<String, String>();
	static String wordsPath;

	// use classpath
	private void loadWords() throws IOException {
		File f = new File(wordsPath);
		FileInputStream fis = new FileInputStream(f);
		InputStreamReader in = new InputStreamReader(fis, "utf-8");
		BufferedReader br = new BufferedReader(in);
		String l = null;
		int wordCount = 0;
		while ((l = br.readLine()) != null) {
			// split on comma
			int index = l.indexOf(',');
			if (index < 0) {
				index = l.indexOf('\t');
			}
			if (index < 0) {
				index = l.indexOf('=');
			}
			if (index < 0) {
				index = l.indexOf(' ');
			}
			if (index < 0) {
				System.out.println("Could not find ',', '\t', '=' or ' ' in '"
						+ l + "' -- ignore");
			}
			String portuguese = l.substring(0, index);
			String english = l.substring(index + 1);
			if (english.indexOf(',') > 0)
				english = english.substring(0, english.indexOf(','));
			
			pwords.add(portuguese);
			ewords.add(english);
			e2p.put(english, portuguese);
			p2e.put(portuguese, english);
			wordCount++;
		}
		System.out.println("Read " + wordCount + " words. e2p=" + e2p.size() + " p2e=" + p2e.size());
		br.close();
	}

	protected void setSeed(long seed) {
		this.seed = seed;
		r = new Random(seed);
		count = 0;
		if (c != null) {
			c.english = null;
			c.portuguese = null;
			c.repaint();
		}
	}
	
	Tester(long _seed) throws Exception {
		loadWords();

		setSeed(_seed);
		 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c = new TesterCanvas();
		c.setPreferredSize(new Dimension(600, 400));
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(c, BorderLayout.CENTER);
		cp.add(c.seedText, BorderLayout.NORTH);

		// list of buttons
		JPanel jp = new JPanel();
		JButton e = new JButton("english");
		e.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = r.nextInt(ewords.size());
				String word = ewords.get(index);
				c.english = word;
				c.portuguese = null;
				count++;
				c.repaint();
			}
		});

		JButton p = new JButton("portuguese");
		p.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = r.nextInt(pwords.size());
				System.out.println("index = " + index);
				String word = pwords.get(index);
				c.english = null;
				c.portuguese = word;
				count++;
				c.repaint();
			}
		});

		JButton t = new JButton("translate");
		t.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (c.english != null) {
					c.portuguese = e2p.get(c.english);
				} else if (c.portuguese != null) {
					c.english = p2e.get(c.portuguese);
				}
				c.repaint();
			}
		});
		
		JButton r = new JButton("reset");
		r.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSeed(seed);
				c.repaint();
			}
		});

		jp.setLayout(new FlowLayout());
		jp.add(e);
		jp.add(t);
		jp.add(p);
		jp.add(r);
		cp.add(jp, BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}
}
