package com.tacoid.spaceship.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.tacoid.spaceship.objects.Meteorite;

public class GameScreen extends AbstractGameScreen {

	private static GameScreen instance;

	private GameScreen() {
		init();
	}
	
	private void init() {
		init("maps/1/", "images/background1.png", 0, 480, 4000);
		createMeteorite();
	}
	
	private void createStone(int x, int y, int size, float initialAngularSpeed, Vector2 initialVector) {
		stage.addActor(new Meteorite(world, x, y, size, initialAngularSpeed, initialVector).getActor());
	}
	
	private void createMeteorite() {
		createStone(300, 3800, 200, 0.2f, new Vector2(-10, -10));
		createStone(250, 3700, 64, 0.2f, new Vector2(0, 10));
		createStone(350, 3600, 64, 0.2f, new Vector2(0, 50));
		createStone(50, 3500, 64, 0.2f, new Vector2(0, 10));
		createStone(100, 3500, 64, -0.2f, new Vector2(0, 20));
		createStone(300, 3500, 64, 0.2f, new Vector2(0, -10));
		createStone(300, 3400, 200, 0.2f, new Vector2(-10, 10));
		createStone(100, 3300, 128, -0.2f, new Vector2(-10, 0));
		createStone(250, 3300, 48, 0.6f, new Vector2(-10, -10));
		createStone(200, 3200, 48, 0.1f, new Vector2(-10, -10));
		createStone(300, 3100, 200, 0.2f, new Vector2(-10, -10));
		createStone(250, 2900, 64, 0.2f, new Vector2(0, 10));
		createStone(350, 2900, 64, 0.2f, new Vector2(0, 50));
		createStone(50, 2800, 64, 0.2f, new Vector2(0, 20));
		createStone(100, 2800, 64, -0.2f, new Vector2(0, 0));
		createStone(300, 2800, 64, 0.2f, new Vector2(0, -10));
		createStone(300, 2700, 200, 0.2f, new Vector2(-10, -10));
		createStone(100, 2600, 128, -0.2f, new Vector2(-10, 0));
		createStone(250, 2600, 48, 0.6f, new Vector2(-10, -100));
		createStone(200, 2500, 48, 0.1f, new Vector2(-10, -100));
		createStone(300, 2400, 200, 0.2f, new Vector2(-10, -10));
		createStone(250, 2300, 64, 0.2f, new Vector2(0, 10));
		createStone(350, 2300, 64, 0.2f, new Vector2(0, -100));
		createStone(50, 2300, 64, 0.2f, new Vector2(0, -10));
		createStone(100, 2300, 64, -0.2f, new Vector2(0, 0));
		createStone(300, 2300, 64, 0.2f, new Vector2(0, -10));
		createStone(300, 2200, 200, 0.2f, new Vector2(-10, -10));
		createStone(100, 2100, 128, -0.2f, new Vector2(-10, 0));
		createStone(250, 2000, 48, 0.6f, new Vector2(-10, -100));
		createStone(200, 1900, 48, 0.1f, new Vector2(-10, -10));
		createStone(300, 1800, 48, 0.6f, new Vector2(-10, -100));
		createStone(100, 1700, 128, 0.6f, new Vector2(-10, 10));
		createStone(200, 1500, 200, 0.1f, new Vector2(-10, -10));
		createStone(200, 1300, 64, 0.6f, new Vector2(-10, -100));
		createStone(300, 1200, 64, 0.3f, new Vector2(-10, 0));
		createStone(250, 1100, 128, 0.3f, new Vector2(-10, -10));
		createStone(200, 900, 92, 0.1f, new Vector2(-1, -10));
		createStone(100, 800, 64, 0.5f, new Vector2(10, -5));
		createStone(300, 600, 64, 0.6f, new Vector2(-10, -10));
		createStone(50, 400, 100, -0.1f, new Vector2(-8, 2));
	}

	public static GameScreen getInstance() {
		if (instance == null) {
			instance = new GameScreen();
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
