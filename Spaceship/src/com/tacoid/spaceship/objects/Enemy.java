package com.tacoid.spaceship.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tacoid.spaceship.screens.AbstractGameScreen;

public class Enemy {
	private int life;
	private float direction;
	private Body body;
	private AbstractGameScreen screen;
	
	public Enemy(AbstractGameScreen gameScreen, World world, int x, int y, int angle, int life) {
		this.life = life;
		this.direction = 0;
		this.screen = gameScreen;
		
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(x, y));
		groundBodyDef.angle = angle * (float)Math.PI / 180;

		body = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();
		Vector2[] v = new Vector2[] {new Vector2(0, 0), new Vector2(32, 0), new Vector2(24, 24), new Vector2(8, 24)};
		groundBox.set(v);
		body.createFixture(groundBox, 0.0f);
	}
	
	public void step() {
		float x0 = body.getPosition().x;
		float y0 = body.getPosition().y;
		float x1 = screen.getSpaceship().getX();
		float y1 = screen.getSpaceship().getY();
		direction = (float)(Math.atan2(y1 - y0, x1 - x0) * 180 / Math.PI) + 270;
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
	
	public float getDirection() {
		return direction;
	}
}