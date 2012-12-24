package com.tacoid.spaceship.actors;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.SpaceshipGame;

public class ObstacleActor extends Actor {
	private Mesh mesh;
	private Mesh lines;
	private Texture texture;
	
	public ObstacleActor(List<Vector2> polygon, List<Vector2> triangles) {
		lines = new Mesh(true, polygon.size(), polygon.size() + 1, new VertexAttribute(Usage.Position, 3, "a_position"));
		
		float[] vertices = new float[(polygon.size()) * 3];
		short[] indices = new short[polygon.size() + 1];
		for (int i = 0; i < polygon.size(); i++) {
			vertices[i * 3] = polygon.get(i).x;
			vertices[i * 3 + 1] = polygon.get(i).y;
			vertices[i * 3 + 2] = 0;
			indices[i] = (short)i;
		}
		indices[polygon.size()] = 0;

		lines.setVertices(vertices);
        lines.setIndices(indices);
		
		mesh = new Mesh(true, triangles.size(), triangles.size(),
				new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
		
		vertices = new float[triangles.size() * 5];
		indices = new short[triangles.size()];
		for (int i = 0; i < triangles.size(); i++) {
			vertices[i * 5] = triangles.get(i).x;
			vertices[i * 5 + 1] = triangles.get(i).y;
			vertices[i * 5 + 2] = 0;
			vertices[i * 5 + 3] = triangles.get(i).x / 1024.0f;
			vertices[i * 5 + 4] = triangles.get(i).y / 1024.0f;
			indices[i] = (short)i;
		}

		mesh.setVertices(vertices);
        mesh.setIndices(indices);
        
        texture = SpaceshipGame.manager.get("images/obstacle_texture.jpg", Texture.class);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();
		Gdx.gl10.glColor4f(1, 1, 1, 1);
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);
		
		Gdx.gl.glLineWidth(2);
		Gdx.gl10.glColor4f(0.6f, 0.3f, 0.0f, 1);
		lines.render(GL10.GL_LINE_STRIP);
		
		batch.begin();
	}
}
