package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.SpaceshipGame;
import com.tacoid.spaceship.objects.Spaceship;

public class LifeActor extends Actor {
	private Spaceship spaceship;
	private Sprite sprite;

	public LifeActor(Spaceship spaceship) {
		this.spaceship = spaceship;
		sprite = new Sprite(new TextureRegion(SpaceshipGame.manager.get("images/lifebar.png", Texture.class), 42, 10));
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		for (int i = 0; i < spaceship.getInitialLife(); i++) {
			if (spaceship.getLife() <= i) {
				sprite.setColor(1f, 1f, 1f, 1);
			} else {
				sprite.setColor(0.2f, 1f, 0.2f, 1);
			}
			sprite.setPosition(getX(), getY() + i * sprite.getHeight());
			sprite.draw(batch, parentAlpha);	
		}
	}
}
