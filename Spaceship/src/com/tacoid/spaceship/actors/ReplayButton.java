package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tacoid.spaceship.SpaceshipGame;
import com.tacoid.spaceship.screens.AbstractGameScreen;

public class ReplayButton extends Button {
	public ReplayButton(final AbstractGameScreen screen) {
		super(new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/replay.png", Texture.class))));
		
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				screen.restart();
				return true;
			}
		});
	}
}
