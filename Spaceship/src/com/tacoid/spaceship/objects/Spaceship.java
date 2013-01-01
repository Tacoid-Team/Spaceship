package com.tacoid.spaceship.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.ISpaceshipController;
import com.tacoid.spaceship.actors.BulletActor;
import com.tacoid.spaceship.actors.SpaceShipActor;
import com.tacoid.spaceship.screens.AbstractGameScreen;

public class Spaceship implements ISpaceshipController {
	private boolean leftOn = false;
	private boolean rightOn = false;
	private boolean bothOn = false;
	private Body spaceShipBody;
	private SpaceShipActor spaceShipActor;
	private int life, initialLife;
	private long hitDate;
	private long fireDate;
	private boolean isFiring;
	private World world;

	public Spaceship(World world, float x, float y, int initialLife) {
		this.initialLife = initialLife;
		this.life = initialLife;
		this.world = world;
		
		//Dynamic Body  
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody;  
		bodyDef.position.set(x, y);

		spaceShipBody = world.createBody(bodyDef);  
		PolygonShape dynamicShape = new PolygonShape();
		Vector2[] vertices = new Vector2[3];
		vertices[0] = new Vector2(0 * AbstractGameScreen.WORLD_TO_BOX, 16 * AbstractGameScreen.WORLD_TO_BOX);
		vertices[1] = new Vector2(-16 * AbstractGameScreen.WORLD_TO_BOX, -10 * AbstractGameScreen.WORLD_TO_BOX);
		vertices[2] = new Vector2(16 * AbstractGameScreen.WORLD_TO_BOX, -10 * AbstractGameScreen.WORLD_TO_BOX);
		dynamicShape.set(vertices);
		spaceShipBody.setAngularDamping(2f);
		spaceShipBody.setLinearDamping(0.1f);

		spaceShipActor = new SpaceShipActor(this);
		spaceShipBody.setUserData(spaceShipActor);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicShape; 
		fixtureDef.density = 1.0f;  
		fixtureDef.friction = 0.5f;  
		fixtureDef.restitution = 0.3f;
		spaceShipBody.createFixture(fixtureDef);
	}
	
	public Body getBody() {
		return spaceShipBody;
	}
	
	public BulletActor createBullet() {
		Bullet bullet = new Bullet(world, spaceShipBody.getWorldPoint(new Vector2(-2 * AbstractGameScreen.WORLD_TO_BOX, 20 * AbstractGameScreen.WORLD_TO_BOX)), spaceShipBody.getAngle(), false);
		return bullet.getActor();
	}

	public void propulse() {
		float rot = spaceShipBody.getAngularVelocity();

		spaceShipBody.setLinearDamping(spaceShipBody.getLinearVelocity().len2() / 8000);
		
		if (areBothOn()) {
			spaceShipBody.applyLinearImpulse(spaceShipBody.getWorldVector(new Vector2(0, 500)), spaceShipBody.getWorldCenter());
		}

		if (isLeftOn() ^ isRightOn()) {
			if (isLeftOn() && rot > -3) {
				spaceShipBody.applyAngularImpulse(-500);
			} else if (isRightOn() && rot < 3) {
				spaceShipBody.applyAngularImpulse(500);
			}
		}
		
		spaceShipBody.applyAngularImpulse(Gdx.input.getAccelerometerX() * 50);
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

	public int getInitialLife() {
		return initialLife;
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
		return localV.y >= -11 * AbstractGameScreen.WORLD_TO_BOX && localV.y <= -9 * AbstractGameScreen.WORLD_TO_BOX;
	}

	public void setHit() {
		this.hitDate = System.currentTimeMillis();
	}

	public long getHitDate() {
		return this.hitDate;
	}

	public boolean getAlreadyHit() {
		return System.currentTimeMillis() - hitDate < 3000;
	}
	
	public void fire(boolean on) {
		this.isFiring = on;
	}
	
	public boolean tryFire() {
		if (isFiring) {
			if (System.currentTimeMillis() - fireDate > 100) {
				fireDate = System.currentTimeMillis();
				return true;
			}
		}
		return false;
	}
}
