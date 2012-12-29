package com.tacoid.spaceship.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tacoid.spaceship.SpaceshipGame;
import com.tacoid.spaceship.objects.Enemy;

public class EnemyActor extends Actor {
	private TextureRegion base;
	private Sprite head;
	private Enemy enemy;
	
	public EnemyActor(Enemy enemy) {
		this.enemy = enemy;
		base = new TextureRegion(SpaceshipGame.manager.get("images/base_enemy.png", Texture.class));
		head = new Sprite(SpaceshipGame.manager.get("images/head_enemy.png", Texture.class));
		setPosition(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y);
		setRotation((float)(enemy.getBody().getAngle() * 180 / Math.PI));
		enemy.getBody().setUserData(this);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		double rot = getRotation() * Math.PI / 180 + 0.92;
		
		head.setPosition(getX() - 8 + 25f * (float)Math.cos(rot), getY() - 8 + 25f * (float)Math.sin(rot));
		head.rotate(1);
		head.draw(batch);
		
		batch.draw(base, getX(), getY(), 0, 0, 32, 32, 1, 1, getRotation());
	}

	public Enemy getEnemy() {
		return enemy;
	}
}
