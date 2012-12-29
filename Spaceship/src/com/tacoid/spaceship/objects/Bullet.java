package com.tacoid.spaceship.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.tacoid.spaceship.actors.BulletActor;

public class Bullet {
	private BulletActor actor;
	
	public Bullet(World world, Vector2 position, float direction, boolean isEnemy) {
		//Dynamic Body
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody;  
		bodyDef.position.set(position);
		bodyDef.gravityScale = 0;

		Body body = world.createBody(bodyDef);
		CircleShape dynamicShape = new CircleShape();
		dynamicShape.setRadius(2);
		dynamicShape.setPosition(new Vector2(2, 2));
		
		actor = new BulletActor(isEnemy);
		body.setUserData(actor);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicShape; 
		fixtureDef.density = 1.0f;  
		fixtureDef.friction = 1.0f;  
		fixtureDef.restitution = 0.5f;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef);
	
		body.setAngularVelocity(0);
		body.setLinearVelocity((float)Math.cos(direction + Math.PI / 2) * 100000, (float)Math.sin(direction + Math.PI / 2) * 100000);
	}
	
	public BulletActor getActor() {
		return actor;
	}
}
