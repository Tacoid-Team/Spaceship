package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tacoid.spaceship.SpaceshipGame;

public class BulletActor extends Image {
	public BulletActor() {
		super(SpaceshipGame.manager.get("images/bullet.png", Texture.class));
	}
}
