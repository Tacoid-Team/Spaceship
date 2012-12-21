package com.tacoid.spaceship.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tacoid.spaceship.SpaceshipGame;

public class Meteorite {
	private Actor actor;
	
	public Meteorite(World world, int x, int y, int size, float initialAngularSpeed, Vector2 initialVector) {
		//Dynamic Body
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody;  
		bodyDef.position.set(x, y);

		Body body = world.createBody(bodyDef);  
		CircleShape dynamicShape = new CircleShape();
		dynamicShape.setRadius(size / 2);
		dynamicShape.setPosition(new Vector2(size / 2, size / 2));

		actor = new Image(new TextureRegion(SpaceshipGame.manager.get("images/stone.png", Texture.class)));
		actor.setSize(size, size);
		body.setUserData(actor);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicShape; 
		fixtureDef.density = 10.0f;  
		fixtureDef.friction = 1.0f;  
		fixtureDef.restitution = 0.5f;
		body.createFixture(fixtureDef);
	
		body.setAngularVelocity(initialAngularSpeed);
		body.setLinearVelocity(initialVector);
	}
	
	public Actor getActor() {
		return actor;
	}
}
