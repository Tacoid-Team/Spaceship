package com.tacoid.spaceship.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.SpaceshipGame;

public class ExplosionActor extends Actor{

	private static final int        FRAME_COLS = 4;
	private static final int        FRAME_ROWS = 4;
	Animation                       explosionAnimation; 
	Texture                         explosionSheet;
	TextureRegion[]                 explosionFrames;
	TextureRegion                   currentFrame;
	Sprite sprite;
	float stateTime; 

	public ExplosionActor() {
		super();
		explosionSheet = SpaceshipGame.manager.get("images/explosion.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(explosionSheet, 32, 32); 
		explosionFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				explosionFrames[index++] = tmp[i][j];
			}
		}
		explosionAnimation = new Animation(0.06f, explosionFrames);              // #11
		stateTime = 0.0f;
		sprite = new Sprite();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		currentFrame = explosionAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, getX(), getY());
		stateTime += Gdx.graphics.getDeltaTime();
		
		if (stateTime >= 0.06 * 16) {
			this.remove();
		}
	}

}
