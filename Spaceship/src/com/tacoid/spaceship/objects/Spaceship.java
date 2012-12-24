package com.tacoid.spaceship.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.ISpaceshipController;
import com.tacoid.spaceship.actors.SpaceShipActor;

public class Spaceship implements ISpaceshipController {
	private boolean leftOn = false;
	private boolean rightOn = false;
	private boolean bothOn = false;
	private Body spaceShipBody;
	private SpaceShipActor spaceShipActor;
	private int life = 100;
	
	public Spaceship(World world, int x, int y) {
			//Dynamic Body  
			BodyDef bodyDef = new BodyDef();  
			bodyDef.type = BodyType.DynamicBody;  
			bodyDef.position.set(x, y);

			spaceShipBody = world.createBody(bodyDef);  
			PolygonShape dynamicShape = new PolygonShape();
			Vector2[] vertices = new Vector2[3];
			vertices[0] = new Vector2(16, 32);
			vertices[1] = new Vector2(0, 6);
			vertices[2] = new Vector2(32, 6);
			dynamicShape.set(vertices);
			spaceShipBody.setAngularDamping(2f);
			spaceShipBody.setLinearDamping(0.1f);

			spaceShipActor = new SpaceShipActor();
			spaceShipBody.setUserData(spaceShipActor);

			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = dynamicShape; 
			fixtureDef.density = 1.0f;  
			fixtureDef.friction = 0.5f;  
			fixtureDef.restitution = 0.3f;
			spaceShipBody.createFixture(fixtureDef);
	}
	
	public void propulse() {
		float rot = spaceShipBody.getAngularVelocity();

		if (areBothOn()) {
			spaceShipBody.applyLinearImpulse(spaceShipBody.getWorldVector(new Vector2(0, 4000)), spaceShipBody.getWorldCenter());
		}
		
		if (isLeftOn() ^ isRightOn()) {
			if (isLeftOn() && rot > -3) {
				spaceShipBody.applyAngularImpulse(-5000);
			} else if (isRightOn() && rot < 3) {
				spaceShipBody.applyAngularImpulse(5000);
			}
		}
	}
	
	public float getX() {
		return spaceShipBody.getPosition().x;
	}
	
	public float getY() {
		return spaceShipBody.getPosition().y;
	}
	
	public Body getSpaceShipBody() {
		return spaceShipBody;
	}
	
	public Actor getActor() {
		return spaceShipActor;
	}
	
	public int getLife() {
		return life;
	}
	
	public void updateLife(int delta) {
		life += delta;
	}
	
	@Override
	public void engineLeft(boolean on) {
		leftOn = on;
		updateActor();
	}

	@Override
	public void engineRight(boolean on) {
		rightOn = on;
		updateActor();
	}
	
	@Override
	public void engineBoth(boolean on) {
		bothOn = on;
		updateActor();
	}

	private void updateActor() {
		if (areBothOn()) {
			spaceShipActor.setBoth();
		} else {
			if (leftOn) {
				spaceShipActor.setLeft();
			} else if (rightOn) {
				spaceShipActor.setRight();
			} else {
				spaceShipActor.setNone();
			}
		}
	}
	
	@Override
	public boolean isLeftOn() {
		return leftOn;
	}

	@Override
	public boolean isRightOn() {
		return rightOn;
	}

	@Override
	public boolean areBothOn() {
		return leftOn && rightOn || bothOn;
	}

	public boolean isBase(Vector2 localV) {
		return localV.y >= 5 && localV.y <= 7;
	}
}
