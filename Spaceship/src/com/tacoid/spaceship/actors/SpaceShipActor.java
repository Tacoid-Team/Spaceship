package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.SpaceshipGame;
import com.tacoid.spaceship.objects.Spaceship;

public class SpaceShipActor extends Actor {
	private Sprite activeSprite;
	private TextureRegion none, both, left, right;
	private Spaceship ship;
	
	public SpaceShipActor(Spaceship ship) {
		this.ship = ship;
		none = new TextureRegion(SpaceshipGame.manager.get("images/spaceship.png", Texture.class), 32, 32);
		both = new TextureRegion(SpaceshipGame.manager.get("images/spaceship_both.png", Texture.class), 32, 32);
		left = new TextureRegion(SpaceshipGame.manager.get("images/spaceship_left.png", Texture.class), 32, 32);
		right = new TextureRegion(SpaceshipGame.manager.get("images/spaceship_right.png", Texture.class), 32, 32);
		activeSprite = new Sprite(none);	
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		float alpha = blink();
		activeSprite.setRotation(getRotation());
		activeSprite.setPosition(getX() - activeSprite.getWidth()/2, getY() - activeSprite.getHeight()/2);
		activeSprite.draw(batch, alpha * parentAlpha);
	}
	
	private float blink() {
		long currentDate = System.currentTimeMillis();
		if (currentDate - ship.getHitDate() < 3000) {
			return (float)Math.abs(Math.cos(Math.PI * 5 * (currentDate - ship.getHitDate()) / 3000.0f));
		} else {
			return 1f;
		}
	}
	
	public void setBoth() {
		activeSprite = new Sprite(both);
	}
	
	public void setNone() {
		activeSprite = new Sprite(none);
	}
	
	public void setLeft() {
		activeSprite = new Sprite(left);
	}
	
	public void setRight() {
		activeSprite = new Sprite(right);
	}
}
