package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tacoid.spaceship.SpaceshipGame;

public class FinishFlagActor extends Image {
	public FinishFlagActor() {
		super(SpaceshipGame.manager.get("images/finish_flag.png", Texture.class));
	}
}
