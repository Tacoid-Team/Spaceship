package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tacoid.spaceship.SpaceshipGame;
import com.tacoid.spaceship.objects.Spaceship;

public class SpaceShipActor extends Group {

    
	private Actor leftEngine;
	private Actor rightEngine;
	
	boolean leftState;
	boolean rightState;
	
	private Sprite shipSprite;
	private TextureRegion shipTexture;
	private Spaceship ship;
	
	public SpaceShipActor(Spaceship ship) {
		this.ship = ship;
		leftEngine = new EngineActor();
		rightEngine = new EngineActor();
		shipTexture = new TextureRegion(SpaceshipGame.manager.get("images/spaceship.png", Texture.class), 32, 32);
		shipSprite = new Sprite(shipTexture);	
		
		leftState = false;
		rightState = false;
		
		leftEngine.setPosition(-shipTexture.getRegionWidth()/2, -shipTexture.getRegionHeight()/2);
		rightEngine.setPosition(shipTexture.getRegionWidth()/2-8, -shipTexture.getRegionHeight()/2);
		
		this.addActor(leftEngine);
		this.addActor(rightEngine);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		float alpha = blink();
		shipSprite.setRotation(getRotation());
		shipSprite.setPosition(getX() - shipSprite.getWidth()/2, getY() - shipSprite.getHeight()/2);
		shipSprite.draw(batch, alpha * parentAlpha);

		super.draw(batch, alpha * parentAlpha);
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
		leftEngine.setVisible(true);
		rightEngine.setVisible(true);
	}
	
	public void setNone() {
		leftEngine.setVisible(false);
		rightEngine.setVisible(false);
	}
	
	public void setLeft() {
		leftEngine.setVisible(true);
		rightEngine.setVisible(false);
	}
	
	public void setRight() {
		leftEngine.setVisible(false);
		rightEngine.setVisible(true);
	}
}
