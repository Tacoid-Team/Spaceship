package com.tacoid.spaceship.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tacoid.spaceship.ISpaceshipController;

public class SpaceshipButton extends Button {

	public enum ButtonAction {UP, FIRE};
	
	public SpaceshipButton(final ISpaceshipController controller, final ButtonAction dir, Drawable up, Drawable down) {
		super(up, down);
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				if (dir == ButtonAction.UP) {
					controller.engineBoth(true);
				} else {
					controller.fire(true);
				}
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (dir == ButtonAction.UP) {
					controller.engineBoth(false);
				} else {
					controller.fire(false);
				}
				return;
			}
		});
	}

}