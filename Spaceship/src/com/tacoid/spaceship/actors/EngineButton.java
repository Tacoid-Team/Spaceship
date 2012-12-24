package com.tacoid.spaceship.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tacoid.spaceship.ISpaceshipController;

public class EngineButton extends Button {

	public enum Direction {UP, LEFT, RIGHT};
	
	public EngineButton(final ISpaceshipController controller, final Direction dir, Drawable up, Drawable down) {
		super(up, down);
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				if (dir == Direction.LEFT) {
					controller.engineRight(true);
				} else if (dir == Direction.RIGHT) {
					controller.engineLeft(true);
				} else {
					controller.engineBoth(true);
				}
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (dir == Direction.LEFT) {
					controller.engineRight(false);
				} else if (dir == Direction.RIGHT) {
					controller.engineLeft(false);
				} else {
					controller.engineBoth(false);
				}
				return;
			}
		});
	}

}