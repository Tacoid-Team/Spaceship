package com.tacoid.spaceship.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tacoid.spaceship.ISpaceshipController;

public class EngineButton extends Button {

	public EngineButton(final ISpaceshipController controller, final boolean isLeft, Drawable up, Drawable down) {
		super(up, down);
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				if (isLeft) {
					controller.engineLeft(true);
				} else {
					controller.engineRight(true);
				}
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (isLeft) {
					controller.engineLeft(false);
				} else {
					controller.engineRight(false);
				}
				return;
			}
		});
	}

}