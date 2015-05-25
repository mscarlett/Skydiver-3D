// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.scarlettapps.skydiver3d.Skydiver3D;
import com.scarlettapps.skydiver3d.world.utils.PerlinNoise;
import com.scarlettapps.skydiver3d.worldstate.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class Terrain extends GameObject { //TODO fix bug that causes parachute to randomly not open // also fix bug in which screen unresponsive
	
	private Mesh mesh;
	private ShaderProgram shader;
	private int u_mvpMatrix;
	private int u_fogFactor;
	private int u_texture0;
	private int u_texture1;
	private int u_texture2;
	private int u_texture3;
	
	CustomHeightmap heightmap;
	
	public Terrain() {
		super(false,true);
	}
	
	@Override
	public void initialize() {
		heightmap = new CustomHeightmap(4, MathUtils.random(5,10), MathUtils.random(15,30), 217f, 2, 2790);
		mesh = heightmap.createMesh();
		mesh.setVertices(heightmap.vertices);
		mesh.setIndices(heightmap.indices);
		
		String vertexShader = "#ifdef GL_ES\n"
				+ "precision mediump float;\n"
				+ "#endif\n"
				+ "uniform mat4 u_mvpMatrix;                   \n"
				+ "attribute vec4 a_position;                  \n"
				+ "attribute vec4 a_color;                  \n"
				+ "attribute vec2 a_texCoord0;                  \n"
				+ "attribute float a_texIntensity;                  \n"
				+ "varying vec4 v_color;                  \n"
				//+ "varying vec2 v_texCoord0;                  \n"
				+ "void main()                                 \n"
				+ "{                                           \n"
				+ "   v_color = a_color;                       \n"
				//+ "   v_texCoord0 = a_texCoord0;                       \n"
				+ "   vec4 pos = a_position;\n"
				+ "   pos.z = 0.0;\n"
				+ "   gl_Position = u_mvpMatrix *pos;  \n"
				+ "}                            \n";
		String fragmentShader = "#ifdef GL_ES\n"
				+ "precision mediump float;\n"
				+ "#endif\n"
				+ "uniform float u_fogFactor;\n"
				+ "varying vec4 v_color;                  \n"
				//+ "varying vec2 v_texCoord0;                  \n"
				+"\n"
				//+"vec2 truncate(vec2 v)  \n"
				//+"\n{"
				//+"float dx = v.x - float(int(v.x));\n"
				//+"float dy = v.y - float(int(v.y));\n"
				//+"return vec2(dx, dy);\n"
				//+"}\n"
				//+" \n"
				+ "void main()                                  \n"
				+ "{                                            \n"
				+ "const vec4 fogColor = vec4(1.0, 1.0, 1.0, 1.0);\n"
				//+ "vec4 texColor;"
				//+ "vec2 newTexCoord = truncate(30.0*v_texCoord0);"
				//+ "float h1 = -5.0;\n"
				//+ "float h2 = 0.0;\n"
				//+ "float h3 = 10.0;\n"
				//+ "float h4 = 75.0;\n"
				+ "gl_FragColor = mix( fogColor, v_color, u_fogFactor);\n"
				+ "}";

		shader = new ShaderProgram(vertexShader, fragmentShader);
		
		if (Skydiver3D.DEV_MODE) {
			if (!shader.isCompiled()) {
				Gdx.app.log(Skydiver3D.LOG, shader.getLog());
			}
		}
		
		u_mvpMatrix = shader.getUniformLocation("u_mvpMatrix");
		u_fogFactor = shader.getUniformLocation("u_fogFactor");
		
		for (int i = 1; i < noiseMatrix.length; i++) {
			noiseMatrix[i] = Math.abs(p.noise1(((float)i)/noiseMatrix.length)) + noiseMatrix[i-1];
		}
		float gamma = MathUtils.random(0.3f,0.7f);
		for (int i = 1; i < noiseMatrix.length; i++) {
			noiseMatrix[i] = noiseMatrix[i]/noiseMatrix[noiseMatrix.length-1]*gamma;
		}
	}
	
	@Override
	public void reset() {
		
	}

	public void render(Camera cam) {
		/*Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		texture0.bind();
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
		texture1.bind();
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE2);
		texture2.bind();
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE3);
		texture3.bind();*/
		shader.begin();
		/*shader.setUniformi(u_texture0, 0);
		shader.setUniformi(u_texture1, 1);
		shader.setUniformi(u_texture2, 2);
		shader.setUniformi(u_texture3, 3);*/
		shader.setUniformMatrix(u_mvpMatrix, cam.combined);
		float fogFactor = noise(cam.position.z); 
		shader.setUniformf(u_fogFactor, 1-fogFactor);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}
	
	PerlinNoise p = new PerlinNoise(MathUtils.random(1,100000));
	
	public static float[] noiseMatrix = new float[Skydiver.STARTING_HEIGHT];

	private float noise(float z) {
		if (z > 0) {
			return noiseMatrix[(int)z];
		}
		return 0;
	}

	@Override
	protected void updateObject(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void renderObject(Renderer renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWorldStateChanged(WorldState worldState) {
		// TODO Auto-generated method stub
		
	}

	public float getAltitude(float x, float y) {
		int pitch = heightmap.size+1;
		x = x/heightmap.stretch+pitch/2;
		y = y/heightmap.stretch+pitch/2;
		try {
			return heightmap.vertices[((int)x+(int)y*pitch)*7+2];
		} catch (Exception e) {
			return -1000;
		}
	}
}
