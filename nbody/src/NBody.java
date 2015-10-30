import java.io.*;
import java.util.*;

import javax.swing.JFileChooser;

import princeton.StdAudio;
import princeton.StdDraw;

public class NBody {
	public static final double G = 6.67E-11;
	private static final int DELAY = 10;
	private static final String PREFIX = "nbody/data/";

	/**
	 * Displays file chooser for browsing in the project directory. and opens
	 * the selected file
	 *
	 * @return a new Scanner that produces values scanned from the selected
	 *         file. null if file could not be opened or was not selected
	 */
	public static Scanner openFileFromDialog() {
		Scanner scan = null;
		System.out.println("Opening file dialog.");
		JFileChooser openChooser = new JFileChooser(System.getProperties().getProperty("user.dir"));
		int retval = openChooser.showOpenDialog(null);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = openChooser.getSelectedFile();
			try {
				scan = new Scanner(file);
				System.out.println("Opening: " + file.getName() + ".");
			} catch (FileNotFoundException e) {
				System.out.println("Could not open selected file.");
				e.printStackTrace();
			}
		} else {
			System.out.println("File open canceled.");
		}
		return scan;
	}

	/**
	 * returns Euclidean distance between (x1, y1) and (x2, y2)
	 *
	 * @param x1
	 *            x-coordinate of point 1
	 * @param y1
	 *            y-coordinate of point 1
	 * @param x2
	 *            x-coordinate of point 2
	 * @param y2
	 *            y-coordinate of point 2
	 * @return Euclidean distance between (x1, y1) and (x2, y2)
	 */
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	/**
	 * return the magnitude of the gravitational force between two bodies of
	 * mass m1 and m2 that are distance r apart
	 *
	 * @param m1
	 *            mass of body 1 in kg
	 * @param m2
	 *            mass of body 2 in kg
	 * @param r
	 *            distance in m
	 * @return force between m1 and m2 that are distance r apart
	 */
	public double force(double m1, double m2, double r) {
		return G * m1 * m2 / r / r;
	}

	/**
	 * Returns the x positions and y positions of bodies
	 * 
	 * @param totalTime
	 *            The total amount of universe time to run for
	 * @param timeStep
	 *            The value of delta t in the equations to calculate position
	 * @param info
	 *            The scanner with info about the initial conditions of the
	 *            bodies
	 * @return an array whose first element is the x positions of the bodies,
	 *         and whose second element is the y positions of the bodies at time
	 *         t = totalTime
	 */
	public double[][] positions(Scanner info, int totalTime, int timeStep) {
		int N = info.nextInt();
		double[][] output = new double[2][N];
		double R = info.nextDouble();
		StdDraw.setXscale(-R, R);
		StdDraw.setYscale(-R, R);
		ArrayList<Planet> planets = new ArrayList<>(N);
		for (int i = 0; i < N; i++)
			planets.add(new Planet(info.nextDouble(), info.nextDouble(), info.nextDouble(), info.nextDouble(),
					info.nextDouble(), info.next()));
		info.close();

		for (int i = 0; i < totalTime; i += timeStep) {
			StdDraw.clear();
			StdDraw.picture(0, 0, PREFIX + "starfield.jpg");
			planets.forEach(p -> {
				p.fX = 0;
				p.fY = 0;
				planets.forEach(q -> {
					if (!p.equals(q)) {
						double d = distance(p.x, p.y, q.x, q.y);
						double f = force(p.m, q.m, d);
						p.fX += (q.x - p.x) / d * f;
						p.fY += (q.y - p.y) / d * f;
					}
				});
				p.vX += p.fX / p.m * timeStep;
				p.vY += p.fY / p.m * timeStep;
			});
			planets.forEach(p -> {
				p.x += p.vX * timeStep;
				p.y += p.vY * timeStep;
				// System.out.println(p.vX + " " + p.vY);
				StdDraw.picture(p.x, p.y, p.img);
			});
			StdDraw.show(DELAY);
		}
		for (int i = 0; i < N; i++) {
			output[0][i] = planets.get(i).x;
			output[1][i] = planets.get(i).y;
		}
		return output;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException {
		Scanner info;
		int time, dt;
		if (args.length == 3) {
			info = new Scanner(new File(PREFIX + args[0]));
			time = Integer.parseInt(args[1]);
			dt = Integer.parseInt(args[2]);
		} else {
			//info = openFileFromDialog();
			 info = new Scanner(new File(PREFIX+"planets.txt"));
			time = 10000000;
			dt = 25000;
		}

		// StdAudio.play("data/2001.mid");
		NBody myNBody = new NBody();

		if (false) {
			double[][] test = myNBody.positions(new Scanner(new File(PREFIX + "planets.txt")), 100000, 25000);
			for (int i = 0; i < test.length; i++) {
				for (int j = 0; j < test[i].length; j++) {
					System.out.print(test[i][j] + " ");
				}
				System.out.println();
			}
		}
		myNBody.positions(info, time, dt);
		StdDraw.clear();
		StdAudio.close();
	}

	class Planet {
		double x, y, vX, vY, m, fX, fY;
		String img;

		Planet(double x, double y, double vX, double vY, double mass, String img) {
			this.x = x;
			this.y = y;
			this.vX = vX;
			this.vY = vY;
			this.m = mass;
			this.img = PREFIX + img;
		}

		public String toString() {
			return x + "\t" + y + "\t" + vX + "\t" + vY + "\t" + m + "\t" + img;
		}
	}
}