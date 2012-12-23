package com.tacoid.spaceship.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.tacoid.spaceship.actors.ObstacleActor;

public class GameScreen2 extends AbstractGameScreen {
	private static GameScreen2 instance;
	
	private GameScreen2() {
		init();
	}
	
	protected void init() {
		super.init("images/background2.png", 100, 1024, 1024);
		createObstacles();
	}
	
	private void createObstacles() {
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 0));

		List<Vector2> obstacle = new ArrayList<Vector2>();
		
		// TODO: lire depuis un fichier.
		obstacle.add(new Vector2(160, 560));
		obstacle.add(new Vector2(202, 615));
		obstacle.add(new Vector2(263, 640));
		obstacle.add(new Vector2(314, 619));
		obstacle.add(new Vector2(350, 650));
		obstacle.add(new Vector2(450, 650));
		obstacle.add(new Vector2(490, 614));
		obstacle.add(new Vector2(480, 550));
		obstacle.add(new Vector2(330, 515));
		obstacle.add(new Vector2(250, 515));
		
		EarClippingTriangulator ect = new EarClippingTriangulator();
		List<Vector2> triangles = ect.computeTriangles(obstacle);
		
		for (int i = 0; i < triangles.size(); i++) {
			if ((i + 1) % 3 == 0) {
				Body groundBody = world.createBody(groundBodyDef);  
				PolygonShape groundBox = new PolygonShape();
				Vector2[] v = new Vector2[] {triangles.get(i), triangles.get(i-1), triangles.get(i-2)};
				groundBox.set(v);
				groundBody.createFixture(groundBox, 0.0f);
				
			}
		}
		
		stage.addActor(new ObstacleActor(obstacle, triangles));
	}

	public static GameScreen2 getInstance() {
		if (instance == null) {
			instance = new GameScreen2();
		}
		return instance;
	}


	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getBody() == spaceship.getSpaceShipBody()
				|| contact.getFixtureB().getBody() == spaceship.getSpaceShipBody()) {
			if (spaceship.getLife() > 0) {
				spaceship.updateLife(-1);
			}
			System.out.println("life: " + spaceship.getLife());
		}
	}

}
