package com.tacoid.spaceship.screens;

import com.badlogic.gdx.Screen;

public class LoadingScreen implements Screen {

	private static LoadingScreen instance;
	private static boolean initialized;

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public static LoadingScreen getInstance() {
		if (instance == null) {
			instance = new LoadingScreen();
		}
		if (!initialized) {
			instance.init();
		}
		return instance;
	}

	private void init() {
		// TODO Auto-generated method stub
		
	}
}
