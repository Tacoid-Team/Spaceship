package com.tacoid.spaceship.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.SpaceshipGame;

public class EngineActor extends Actor{

	private static final int        FRAME_COLS = 3;
	private static final int        FRAME_ROWS = 1;
	Animation                       engineAnimation; 
	Texture                         engineSheet;
	TextureRegion[]                 engineFrames;
	TextureRegion                   currentFrame;
	Sprite sprite;
	float stateTime; 

	public EngineActor() {
		super();
		engineSheet = SpaceshipGame.manager.get("images/engine.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(engineSheet, 8,8); 
		engineFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				engineFrames[index++] = tmp[i][j];
			}
		}
		engineAnimation = new Animation(0.12f, engineFrames);              // #11
		stateTime = 0.0f;
		sprite = new Sprite();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		currentFrame = engineAnimation.getKeyFrame(stateTime, true);
		stateTime += Gdx.graphics.getDeltaTime(); 
		sprite.setRegion(currentFrame);   
		sprite.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		sprite.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight() / 2);
		sprite.setPosition(this.getX(), this.getY());
		sprite.draw(batch, parentAlpha);
	}

}
