// Copyright 2014 Michael Scarlett
// All rights reserved

package com.scarlettapps.skydiver3d.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.scarlettapps.skydiver3d.SkyDiver3D;
import com.scarlettapps.skydiver3d.worldstate.StatusManager.WorldState;
import com.scarlettapps.skydiver3d.worldview.Renderer;

public class TerrainTemp extends GameObject {
	
	private final int grid = 64; // patch resolution
	private final float scale = 9000; // scale x/y
	private Mesh mesh;
	private ShaderProgram shader;
	private Texture heightmap;
	private Texture texterrain;
	
	private float[] vertices;
	
	private int u_projTrans;
	private int u_texHeightmap;
	private int u_texTerrain;
	private int u_mapPosition;
	private int u_offset;
	private int u_scale;
	
	public TerrainTemp() {
		super(false,true);
		
		FileHandle vertexShader = Gdx.files.internal("data/shaders/clipmap.vert");
		FileHandle fragmentShader = Gdx.files.internal("data/shaders/clipmap.frag");
		shader = new ShaderProgram(vertexShader, fragmentShader);
		
		if (SkyDiver3D.DEV_MODE) {
			if (!shader.isCompiled()) {
				Gdx.app.log(SkyDiver3D.LOG, shader.getLog());
			}
		}
		
		boolean pedantic = false;
		
		u_texHeightmap = shader.fetchUniformLocation("u_texHeightmap", pedantic);
		u_mapPosition = shader.fetchUniformLocation("u_mapPosition", pedantic);
		u_offset = shader.fetchUniformLocation("u_offset", pedantic);
		u_scale = shader.fetchUniformLocation("u_scale", pedantic);
		u_texTerrain = shader.fetchUniformLocation("u_texTerrain", pedantic);
		u_projTrans = shader.fetchUniformLocation("u_projview", pedantic);
		
		vertices = new float[4 * (grid+1) * (grid+4)];
		
		setVertices();
		
		mesh = new Mesh(true, this.vertices.length / 2,
				0, new VertexAttribute(
						VertexAttributes.Usage.Position, 2, "a_position"));
		mesh.setVertices(vertices);
		
		heightmap = new Texture(Gdx.files.internal("data/heightmap.png"));
		texterrain = new Texture(Gdx.files.internal("data/grass.jpeg"), true);
		texterrain.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
	}
	
	private void setVertices() {
		int idx = 0;
		
		for(int j = 0; j < grid+1; j++)
		for(int i = 0; i < grid+2; i++)
		{
			for (int k = 0; k < ((i==0) ? 2 : 1); k++)
			{
				vertices[idx++]=((float)i)/grid;
				vertices[idx++]=((float)j)/grid;
			}			
			++j;
			for (int k = 0; k < ((i==grid+1) ? 2 : 1); k++)
			{
				vertices[idx++]=((float)i)/grid;
				vertices[idx++]=((float)j)/grid;
			}
			--j;
		}
	}
	
	public void render(Camera camera) {
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_FRONT);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDepthFunc(GL20.GL_LESS);
		
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		heightmap.bind();
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
		texterrain.bind();
		
		shader.begin();
		shader.setUniformi(u_texHeightmap, 0);
		shader.setUniformi(u_texTerrain, 1);
		
		Matrix4 mat = camera.combined;
		Vector3 viewpos = camera.position;
		
		shader.setUniformMatrix(u_projTrans, mat);
		
		float sxy = scale; // scale x/y
		shader.setUniformf(u_mapPosition, -viewpos.x / ((float) 2 * 512 * grid),
				-viewpos.z / ((float) 2 * 512 * grid), 0, 0);

		float ox = ((int) (viewpos.x * 1) & 511)
					/ ((float) 512 * grid)+1f;
		float oy = ((int) (viewpos.z * 1) & 511)
					/ ((float) 512 * grid)+1f;

		shader.setUniformf(u_scale, sxy * 0.25f, sxy * 0.25f, 1, 1);

		// render
		shader.setUniformf(u_offset, ox + ((float) -2), oy + ((float) -2), 0, 0);
		mesh.render(shader, GL20.GL_TRIANGLE_FAN);
		
		shader.end();
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
}
