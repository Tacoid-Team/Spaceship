package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tacoid.spaceship.SpaceshipGame;

public class StartFlagActor extends Image {
	public StartFlagActor() {
		super(SpaceshipGame.manager.get("images/start_flag.png", Texture.class));
	}
}
