package com.tacoid.spaceship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.tacoid.spaceship.screens.GameScreen;
import com.tacoid.spaceship.screens.GameScreen2;
import com.tacoid.spaceship.screens.LoadingScreen;

public class SpaceshipGame extends Game {

	private LoadingScreen loadingScreen;
	public static AssetManager manager;
	private boolean loaded;

	@Override
	public void render() {
		if (manager.update()) {
			if (!loaded) {
				if (getScreen() == null) {
					setScreen(GameScreen2.getInstance());
				} else {
					getScreen().show();
				}
			}
			loaded = true;
			super.render();
		} else {
			loadingScreen.render(Gdx.graphics.getDeltaTime());
			loaded = false;
		}
	}
	
	@Override
	public void create() {
		loadingScreen = LoadingScreen.getInstance();
		loadingScreen.resize(0, 0);
		manager = new AssetManager();
		loadAssets();
	}

	private void loadAssets() {
		manager.load("images/explosion.png", Texture.class);
		manager.load("images/obstacle_texture.jpg", Texture.class);
		manager.load("images/right_on.png", Texture.class);
		manager.load("images/right_off.png", Texture.class);
		manager.load("images/left_on.png", Texture.class);
		manager.load("images/left_off.png", Texture.class);
		manager.load("images/both_on.png", Texture.class);
		manager.load("images/both_off.png", Texture.class);
		manager.load("images/background1.png", Texture.class);
		manager.load("images/background2.png", Texture.class);
		manager.load("images/stone.png", Texture.class);
		manager.load("images/spaceship.png", Texture.class);
		manager.load("images/spaceship_both.png", Texture.class);
		manager.load("images/spaceship_left.png", Texture.class);
		manager.load("images/spaceship_right.png", Texture.class);
		manager.load("images/lifebar.png", Texture.class);
		manager.load("images/bullet.png", Texture.class);
		manager.load("images/engine.png", Texture.class);
		manager.load("images/base_enemy.png", Texture.class);
		manager.load("images/head_enemy.png", Texture.class);
	}

}
