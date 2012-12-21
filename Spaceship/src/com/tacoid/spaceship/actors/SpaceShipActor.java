package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.SpaceshipGame;

public class SpaceShipActor extends Actor {
	private TextureRegion activeSprite;
	private TextureRegion none, both, left, right;
	
	public SpaceShipActor() {
		none = new TextureRegion(SpaceshipGame.manager.get("images/spaceship.png", Texture.class), 32, 32);
		both = new TextureRegion(SpaceshipGame.manager.get("images/spaceship_both.png", Texture.class), 32, 32);
		left = new TextureRegion(SpaceshipGame.manager.get("images/spaceship_left.png", Texture.class), 32, 32);
		right = new TextureRegion(SpaceshipGame.manager.get("images/spaceship_right.png", Texture.class), 32, 32);
		activeSprite = none;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(activeSprite, getX(), getY(), 0, 0, 32, 32, 1, 1, getRotation());
	}
	
	public void setBoth() {
		activeSprite = both;
	}
	
	public void setNone() {
		activeSprite = none;
	}
	
	public void setLeft() {
		activeSprite = left;
	}
	
	public void setRight() {
		activeSprite = right;
	}
}
