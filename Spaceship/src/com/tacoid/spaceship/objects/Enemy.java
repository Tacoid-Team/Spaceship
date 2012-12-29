package com.tacoid.spaceship.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy {
	private int life;
	private Body body;
	
	public Enemy(World world, int x, int y, int angle, int life) {
		this.life = life;
		
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(x, y));
		groundBodyDef.angle = angle * (float)Math.PI / 180;

		body = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();
		Vector2[] v = new Vector2[] {new Vector2(0, 0), new Vector2(32, 0), new Vector2(24, 24), new Vector2(8, 24)};
		groundBox.set(v);
		body.createFixture(groundBox, 0.0f);
	}

	public void hit() {
		life--;
	}
	
	public boolean alive() {
		return life > 0;
	}
	
	public Body getBody() {
		return body;
	}
}