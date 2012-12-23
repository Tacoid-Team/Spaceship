package com.tacoid.spaceship;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class KeyboardHandler implements InputProcessor {

	private Stage stage;
	private ISpaceshipController spaceship;

	public KeyboardHandler(Stage stage, ISpaceshipController spaceship) {
		this.stage = stage;
		this.spaceship = spaceship;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT) {
			spaceship.engineRight(true);
		} else if (keycode == Keys.RIGHT) {
			spaceship.engineLeft(true);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.LEFT) {
			spaceship.engineRight(false);
		} else if (keycode == Keys.RIGHT) {
			spaceship.engineLeft(false);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return stage.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return stage.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return stage.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}