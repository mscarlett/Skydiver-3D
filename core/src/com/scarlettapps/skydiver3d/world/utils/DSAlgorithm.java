package com.scarlettapps.skydiver3d.world.utils;

/**
 * This class creates height maps that can be used for various purposes, such as
 * cloud generation, terrain generation, etc. A height map made by this class is
 * wrappable.<br>
 * <br>
 * <a href=http://www.gameprogrammer.com/fractal.html>Diamond-square
 * algorithm</a>
 * 
 * @author Marcel Veltman
 */
public class DSAlgorithm {

	/**
	 * This method uses the seed value to initialize the four corners of the
	 * map. The variation creates randomness in the map. The size of the array
	 * is determined by the amount of iterations (i.e. 1 iteration -> 3x3 array,
	 * 2 iterations -> 5x5 array, etc.).
	 * 
	 * @param iterations
	 *            the amount of iterations to do (minimum of 1)
	 * @param seed
	 *            the starting value
	 * @param variation
	 *            the amount of randomness in the height map (minimum of 0)
	 * @return a height map in the form of a 2-dimensional array containing
	 *         integer values or null if the arguments are out of range
	 */
	public static float[] makeHeightMap(final int iterations, final int seed, int variation) {
		if (iterations < 1 || variation < 0) {
			throw new IllegalArgumentException();
		}

		final int length = (1 << iterations) + 1;
		final float[] map = new float[(length) * (length)];
		final int maxIndex = length - 1;
		int size = length;

		// seed the corners
		map[indexOf(0, 0, length)] = seed;
		map[indexOf(0, maxIndex, length)] = seed;
		map[indexOf(maxIndex, 0, length)] = seed;
		map[indexOf(maxIndex, maxIndex, length)] = seed;

		for (int i = 1; i <= iterations; i++) {
			int minCoordinate = maxIndex >> i;// Minimum coordinate of the
												// current map spaces
			size = minCoordinate << 1;// Area surrounding the current place in
										// the map

			diamondStep(minCoordinate, size, map, variation, length);
			squareStepEven(minCoordinate, map, size, maxIndex, variation, length);
			squareStepOdd(map, size, minCoordinate, maxIndex, variation, length);

			variation = variation >> 1;// Divide variation by 2
		}

		return map;
	}

	/**
	 * Calculates average values of four corner values taken from the smallest
	 * possible square.
	 * 
	 * @param minCoordinate
	 *            the x and y coordinate of the first square center
	 * @param size
	 *            width and height of the squares
	 * @param map
	 *            the height map to fill
	 * @param variation
	 *            the randomness in the height map
	 */
	private static void diamondStep(final int minCoordinate, final int size, final float[] map,
			final int variation, final int length) {
		for (int x = minCoordinate; x < (length - minCoordinate); x += size) {
			for (int y = minCoordinate; y < (length - minCoordinate); y += size) {
				int left = x - minCoordinate;
				int right = x + minCoordinate;
				int up = y - minCoordinate;
				int down = y + minCoordinate;

				// the four corner values
				int val1 = (int)map[indexOf(left, up, length)]; // upper left
				int val2 = (int)map[indexOf(left, down, length)]; // lower left
				int val3 = (int)map[indexOf(right, up, length)]; // upper right
				int val4 = (int)map[indexOf(right, down, length)];// lower right

				calculateAndInsertAverage(val1, val2, val3, val4, variation,
						map, x, y, size, length);
			}
		}
	}

	/**
	 * Calculates average values of four corner values taken from the smallest
	 * possible diamond. This method calculates the values for the even rows,
	 * starting with row 0.
	 * 
	 * @param minCoordinate
	 *            the x-coordinate of the first diamond center
	 * @param map
	 *            the height map to fill
	 * @param size
	 *            the length of the diagonals of the diamonds
	 * @param maxIndex
	 *            the maximum index in the array
	 * @param variation
	 *            the randomness in the height map
	 */
	private static void squareStepEven(final int minCoordinate, final float[] map, final int size,
			final int maxIndex, final int variation, final int length) {
		for (int x = minCoordinate; x < length; x += size) {
			for (int y = 0; y < length; y += size) {
				if (y == maxIndex) {
					map[indexOf(x, y, length)] = map[indexOf(x, 0, length)];
					continue;
				}

				int left = x - minCoordinate;
				int right = x + minCoordinate;
				int down = y + minCoordinate;
				int up = 0;

				if (y == 0) {
					up = maxIndex - minCoordinate;
				} else {
					up = y - minCoordinate;
				}

				// the four corner values
				int val1 = (int)map[indexOf(left, y, length)]; // left
				int val2 = (int)map[indexOf(x, up, length)]; // up
				int val3 = (int)map[indexOf(right, y, length)];// right
				int val4 = (int)map[indexOf(x, down, length)]; // down

				calculateAndInsertAverage(val1, val2, val3, val4, variation,
						map, x, y, size, length);
			}
		}
	}

	/**
	 * Calculates average values of four corner values taken from the smallest
	 * possible diamond. This method calculates the values for the odd rows,
	 * starting with row 1.
	 * 
	 * @param minCoordinate
	 *            the x-coordinate of the first diamond center
	 * @param map
	 *            the height map to fill
	 * @param size
	 *            the length of the diagonals of the diamonds
	 * @param maxIndex
	 *            the maximum index in the array
	 * @param variation
	 *            the randomness in the height map
	 */
	private static void squareStepOdd(final float[] map, final int size, final int minCoordinate,
			final int maxIndex, final int variation, final int length) {
		for (int x = 0; x < length; x += size) {
			for (int y = minCoordinate; y < length; y += size) {
				if (x == maxIndex) {
					map[indexOf(x, y, length)] = map[indexOf(0, y, length)];
					continue;
				}

				int left = 0;
				int right = x + minCoordinate;
				int down = y + minCoordinate;
				int up = y - minCoordinate;

				if (x == 0) {
					left = maxIndex - minCoordinate;
				} else {
					left = x - minCoordinate;
				}

				// the four corner values
				int val1 = (int)map[indexOf(left, y, length)]; // left
				int val2 = (int)map[indexOf(x, up, length)]; // up
				int val3 = (int)map[indexOf(right, y, length)];// right
				int val4 = (int)map[indexOf(x, down, length)]; // down

				calculateAndInsertAverage(val1, val2, val3, val4, variation,
						map, x, y, size, length);
			}
		}
	}

	/**
	 * Calculates an average value, adds a variable amount to that value and
	 * inserts it into the height map.
	 * 
	 * @param val1
	 *            first of the values used to calculate the average
	 * @param val2
	 *            second of the values used to calculate the average
	 * @param val3
	 *            third of the values used to calculate the average
	 * @param val4
	 *            fourth of the values used to calculate the average
	 * @param variation
	 *            adds variation to the average value
	 * @param map
	 *            the height map to fill
	 * @param x
	 *            the x-coordinate of the place to fill
	 * @param y
	 *            the y-coordinate of the place to fill
	 */
	private static void calculateAndInsertAverage(final int val1, final int val2, final int val3,
			final int val4, final int variation, final float[] map, final int x, final int y, final int size, final int length) {
		int avg = (val1 + val2 + val3 + val4) >> 2;// average
		int var = (int) ((Math.random() * ((variation << 1) + 1)) - variation);
		map[indexOf(x, y, length)] = avg + var;
	}

	private static int indexOf(int x, int y, int length) {
		return x * length + y;
	}
	
    public static void main(String[] args) {
        final float[] map = makeHeightMap(9, 128, 128);
        final int size = (1 << 9) + 1;
        
//        for (int y = 0; y < map.length; y++) {
//             for (int x = 0; x < map[y].length; x++) {
//                  System.out.printf("[%3d]", map[x][y]);
//             }
//             System.out.println();
//        }
        
        javax.swing.JFrame frame = new javax.swing.JFrame();
        javax.swing.JPanel panel = new javax.swing.JPanel(){
             public void paint(java.awt.Graphics g){
                  super.paint(g);
                  for(int y = 0; y < size; y++){
                       for(int x = 0; x < size; x++){
                            int value = (int)map[indexOf(x,y,size)];
                            if(value > 255){
                                 value = 255;
                            }else if(value < 0){
                                 value = 0;
                            }
                            java.awt.Color color = new java.awt.Color(value, value, value);
                            g.setColor(color);
                            g.fillRect(x, y, 1, 1);
                       }
                  }
             }
        };
        panel.setPreferredSize(new java.awt.Dimension(size, size));
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
   }
}