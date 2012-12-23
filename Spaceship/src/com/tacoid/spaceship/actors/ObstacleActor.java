package com.tacoid.spaceship.actors;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ObstacleActor extends Actor {
	private Mesh mesh;
	private Mesh lines;
	
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
				new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
		
		vertices = new float[triangles.size() * 4];
		indices = new short[triangles.size()];
		for (int i = 0; i < triangles.size(); i++) {
			vertices[i * 4] = triangles.get(i).x;
			vertices[i * 4 + 1] = triangles.get(i).y;
			vertices[i * 4 + 2] = 0;
			vertices[i * 4 + 3] = Color.toFloatBits(100, 100, 100, 255);
			indices[i] = (short)i;
		}

		mesh.setVertices(vertices);
        mesh.setIndices(indices);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.end();
		mesh.render(GL10.GL_TRIANGLES);
		lines.render(GL10.GL_LINE_STRIP);
		batch.begin();
	}
}
