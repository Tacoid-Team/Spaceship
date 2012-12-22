package com.tacoid.spaceship.screens;

import com.badlogic.gdx.physics.box2d.Contact;

public class GameScreen2 extends AbstractGameScreen {
	private static GameScreen2 instance;
	
	private GameScreen2() {
		init();
	}
	
	protected void init() {
		super.init("images/background2.png", 100, 1024, 1024);
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
