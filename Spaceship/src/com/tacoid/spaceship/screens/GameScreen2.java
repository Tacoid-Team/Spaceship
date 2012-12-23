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
	
	private void createObstacle(List<Vector2> obstacle) {
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 0));
		
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
	
	private void createObstacles() {
		List<Vector2> obstacle = new ArrayList<Vector2>();
		obstacle.add(new Vector2(76,9));
		obstacle.add(new Vector2(310,7));
		obstacle.add(new Vector2(327,25));
		obstacle.add(new Vector2(452,21));
		obstacle.add(new Vector2(602,57));
		obstacle.add(new Vector2(755,25));
		obstacle.add(new Vector2(941,77));
		obstacle.add(new Vector2(969,227));
		obstacle.add(new Vector2(945,421));
		obstacle.add(new Vector2(989,679));
		obstacle.add(new Vector2(965,889));
		obstacle.add(new Vector2(953,978));
		obstacle.add(new Vector2(804,926));
		obstacle.add(new Vector2(638,986));
		obstacle.add(new Vector2(440,950));
		obstacle.add(new Vector2(246,986));
		obstacle.add(new Vector2(121,950));
		obstacle.add(new Vector2(36,849));
		obstacle.add(new Vector2(76,740));
		obstacle.add(new Vector2(40,562));
		obstacle.add(new Vector2(68,413));
		obstacle.add(new Vector2(28,287));
		obstacle.add(new Vector2(61,145));
		obstacle.add(new Vector2(0,100));
		obstacle.add(new Vector2(0,1024));
		obstacle.add(new Vector2(1023,1024));
		obstacle.add(new Vector2(1024,1));
		obstacle.add(new Vector2(0,1));
		obstacle.add(new Vector2(0,100));
		obstacle.add(new Vector2(76,9));
		createObstacle(obstacle);
		obstacle = new ArrayList<Vector2>();
		obstacle.add(new Vector2(210,300));
		obstacle.add(new Vector2(274,178));
		obstacle.add(new Vector2(456,174));
		obstacle.add(new Vector2(699,219));
		obstacle.add(new Vector2(808,275));
		obstacle.add(new Vector2(828,554));
		obstacle.add(new Vector2(755,542));
		obstacle.add(new Vector2(719,308));
		obstacle.add(new Vector2(662,388));
		obstacle.add(new Vector2(549,267));
		obstacle.add(new Vector2(452,360));
		obstacle.add(new Vector2(375,255));
		obstacle.add(new Vector2(246,493));
		obstacle.add(new Vector2(210,300));
		createObstacle(obstacle);
		obstacle = new ArrayList<Vector2>();
		obstacle.add(new Vector2(189,784));
		obstacle.add(new Vector2(169,647));
		obstacle.add(new Vector2(387,655));
		obstacle.add(new Vector2(440,833));
		obstacle.add(new Vector2(189,784));
		createObstacle(obstacle);
		obstacle = new ArrayList<Vector2>();
		obstacle.add(new Vector2(602,809));
		obstacle.add(new Vector2(630,659));
		obstacle.add(new Vector2(828,667));
		obstacle.add(new Vector2(832,829));
		obstacle.add(new Vector2(602,809));
		createObstacle(obstacle);
		obstacle = new ArrayList<Vector2>();
		obstacle.add(new Vector2(501,623));
		obstacle.add(new Vector2(533,485));
		obstacle.add(new Vector2(408,481));
		obstacle.add(new Vector2(412,449));
		obstacle.add(new Vector2(585,465));
		obstacle.add(new Vector2(525,651));
		obstacle.add(new Vector2(501,623));
		createObstacle(obstacle);
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