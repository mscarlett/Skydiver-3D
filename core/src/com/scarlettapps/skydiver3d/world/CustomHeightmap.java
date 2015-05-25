package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.MathUtils;
import com.scarlettapps.skydiver3d.world.utils.DSAlgorithm;
import com.scarlettapps.skydiver3d.world.utils.PerlinNoise;

public class CustomHeightmap {
	
	private static final float DEFAULT_SEA_LEVEL = -5;

	public final float[] vertices;
	public final short[] indices;

	public final float strength;
	public final float stretch;
	public final float seaLevel;
	public final int size;
	
	private final PerlinNoise p;
	private final float[] heightMap;

	public CustomHeightmap(int iterations, int seed, int variation,
			float textureWidth, float strength, float stretch) {
		this(iterations, seed, variation, textureWidth, strength, stretch, DEFAULT_SEA_LEVEL);
	}

	public CustomHeightmap(int iterations, int seed, int variation,
			float textureWidth, float strength, float stretch, float seaLevel) {
		this.size = (1 << iterations) + 1;
		this.heightMap = DSAlgorithm.makeHeightMap(iterations, seed, variation);
		this.vertices = new float[heightMap.length * 8];
		this.indices = new short[size * size * 6];
		this.strength = strength;
		this.stretch = stretch;
		this.seaLevel = seaLevel;
		this.p = new PerlinNoise(MathUtils.random(1, 1000));
		createVertices();
		createIndices();
	}

	public void createVertices() {
		int pitch = size + 1;
		int idx = 0;
		int hIdx = 0;
		int boost = MathUtils.random(0, 10);
		for (int y = 0; y < pitch; y++) {
			for (int x = 0; x < pitch; x++) {
				float z = getHeightAvg(hIdx++) * strength + boost;
				vertices[idx++] = (x - pitch / 2) * stretch;
				vertices[idx++] = (y - pitch / 2) * stretch;
				vertices[idx++] = Math.max(z, seaLevel);
				vertices[idx++] = Colorizer.mix(z+MathUtils.random(-10,10));
				vertices[idx++] = ((float) x) / (pitch/10);
				vertices[idx++] = ((float) y) / (pitch/10);
				vertices[idx++] = MathUtils.clamp(p.turbulence2(x * 0.00573f, y * 0.00573f,
								128f) + 0.5f, 0.5f, 1)/2f;
			}
		}
	}

	public void createIndices() {
		int idx = 0;
		short pitch = (short) (size + 1);
		short i1 = 0;
		short i2 = 1;
		short i3 = (short) (1 + pitch);
		short i4 = pitch;

		short row = 0;

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				indices[idx++] = (short) (i1);
				indices[idx++] = (short) (i2);
				indices[idx++] = (short) (i3);

				indices[idx++] = (short) (i3);
				indices[idx++] = (short) (i4);
				indices[idx++] = (short) (i1);

				i1++;
				i2++;
				i3++;
				i4++;
			}

			row += pitch;
			i1 = row;
			i2 = (short) (row + 1);
			i3 = (short) (i2 + pitch);
			i4 = (short) (row + pitch);
		}
	}

	public float getHeightAvg(int hIdx) {
		return (heightMap[mod(hIdx, heightMap.length)]
				+ heightMap[mod(hIdx + 1, heightMap.length)]
				+ heightMap[mod(hIdx + size, heightMap.length)]
				+ heightMap[mod(hIdx + size + 1, heightMap.length)]
				+ heightMap[mod(hIdx + size - 1, heightMap.length)]
				+ heightMap[mod(hIdx - size + 1, heightMap.length)]
				+ heightMap[mod(hIdx - size - 1, heightMap.length)]
				+ heightMap[mod(hIdx - size, heightMap.length)] + heightMap[mod(
					hIdx - 1, heightMap.length)]) / 9;
	}

	private static int mod(int a, int b) {
		return (((a % b) + b) % b);
	}

	public Mesh createMesh() {
		Mesh mesh = new Mesh(true, this.vertices.length / 3,
				this.indices.length, new VertexAttribute(
						VertexAttributes.Usage.Position, 3, "a_position"),
				new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4,
						"a_color"), new VertexAttribute(
						VertexAttributes.Usage.TextureCoordinates, 2,
						"a_texCoord0"),
				new VertexAttribute(VertexAttributes.Usage.Generic, 1,
						"a_texIntensity"));
		mesh.setVertices(this.vertices);
		mesh.setIndices(this.indices);
		return mesh;
	}

	private static class Colorizer {

		private static final Color DEEP_WATER = new Color(12 / 255f,
				39 / 255f, 136 / 255f, 255 / 255f);
		private static final Color SHALLOW_WATER = new Color(30 / 255f,
				160 / 255f, 206 / 255f, 255 / 255f);
		private static final Color SAND = new Color(200 / 255f, 175 / 255f,
				134 / 255f, 255 / 255f);
		private static final Color LIGHT_GRASS = new Color(113 / 255f,
				123 / 255f, 62 / 255f, 255 / 255f);
		private static final Color DARK_GRASS = new Color(129 / 255f,
				131 / 255f, 82 / 255f, 255 / 255f);

		private static final Color DIRT = new Color(109 / 255f, 167 / 255f,
				38 / 255f, 255 / 255f);
		private static final Color ROCK = new Color(156 / 255f, 156 / 255f,
				156 / 255f, 255 / 255f);

		public static final ColorInterval ZERO = new ColorInterval(
				DEEP_WATER, DEEP_WATER, -100000, -30);
		public static final ColorInterval ONE = new ColorInterval(
				DEEP_WATER, SHALLOW_WATER, -30, -5);
		public static final ColorInterval TWO = new ColorInterval(
				SHALLOW_WATER, SAND, -5, 0);
		public static final ColorInterval THREE = new ColorInterval(SAND,
				LIGHT_GRASS, 0, 10);
		public static final ColorInterval FOUR = new ColorInterval(
				LIGHT_GRASS, DARK_GRASS, 10, 45);
		public static final ColorInterval FIVE = new ColorInterval(
				DARK_GRASS, ROCK, 45, 75);
		public static final ColorInterval SIX = new ColorInterval(DIRT,
				ROCK, 75, 100);
		public static final ColorInterval SEVEN = new ColorInterval(ROCK,
				ROCK, 100, 100000);

		public static final ColorInterval[] colorIntervals = { ZERO, ONE,
				TWO, THREE, FOUR, FIVE, SIX, SEVEN };

		public static float mix(float height) {
			for (ColorInterval i : colorIntervals) {
				if (i.contains(height)) {
					return i.mix(height);
				}
			}
			return -1;
		}

		/**
		 * Class to linearly interpolate between two colors within a
		 * specified interval.
		 */
		public static class ColorInterval {
			/** Temp color reference for generating new color. */
			private static final Color tempColor = new Color();
			/** Color at the minimum of the interval. */
			public final Color minColor;
			/** color at the maximum of the interval */
			public final Color maxColor;
			/** Minimum of the interval (inclusive). */
			public final float min;
			/** Maximum of the interval (inclusive). */
			public final float max;

			/**
			 * Create a new ColorInterval.
			 * 
			 * @param minColor the color for the minimum of the interval.
			 * @param maxColor the color for the maximum of the interval.
			 * @param min the minimum of the interval.
			 * @param max the maximum of the interval.
			 */
			public ColorInterval(Color minColor, Color maxColor, float min,
					float max) {
				this.minColor = minColor;
				this.maxColor = maxColor;
				this.min = min;
				this.max = max;
			}

			/**
			 * Linearly interpolate within this color interval to produce a
			 * new color.
			 * 
			 * @param value the value within the interval.
			 * @return the float bits for the new color.
			 */
			public float mix(float value) {
				tempColor.set(minColor);
				float target = MathUtils.clamp(value - min, 0, max - min)
						/ (max - min);
				tempColor.lerp(maxColor, target);
				return tempColor.toFloatBits();
			}

			/**
			 * Determine if the number is between the min and max of this
			 * interval.
			 * 
			 * @param value the value to check.
			 * @return true if within interval, false otherwise.
			 */
			public boolean contains(float value) {
				return min <= value && value <= max;
			}
		}
	}

}