package com.tacoid.spaceship.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tacoid.spaceship.SpaceshipGame;


public class MapsScreen implements Screen {

	private static MapsScreen instance;
	private Stage stage;
	
	public MapsScreen() {
		stage = new Stage(480, 800,	false);
		
		FileHandle skinFile = Gdx.files.internal( "skin/uiskin.json" );
        TextureAtlas textureAtlas = new TextureAtlas("skin/uiskin.atlas");
        Skin skin = new Skin( skinFile, textureAtlas);
		
		Table table = new Table();
		table.defaults().space(10).pad(10).center();
		
		for (int i = 1; i <= 20; i++) {
			TextButton button = new TextButton("Level " + i, skin);
			table.row().fill(true, true).expand(true, true).pad(10, 10, 10, 10).minHeight(50);
			table.add(button);
			
			final int mapId = i;
			
			button.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					SpaceshipGame.getInstance().setScreen(new GameScreen(mapId));
					return true;
				}
			});
		}
		
		ScrollPane scrollPane = new ScrollPane(table, skin);
		scrollPane.setWidth(480);
		scrollPane.setHeight(800);
		scrollPane.setFadeScrollBars(false);
		stage.addActor(scrollPane);
		
		//table.debug();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
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

	public static MapsScreen getInstance() {
		if (instance == null) {
			instance = new MapsScreen();
		}
		return instance;
	}
	
}