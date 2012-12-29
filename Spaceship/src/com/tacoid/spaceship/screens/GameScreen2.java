package com.tacoid.spaceship.screens;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.tacoid.spaceship.actors.BulletActor;
import com.tacoid.spaceship.actors.EnemyActor;
import com.tacoid.spaceship.actors.ObstacleActor;
import com.tacoid.spaceship.actors.SpaceShipActor;
import com.tacoid.spaceship.objects.Enemy;

public class GameScreen2 extends AbstractGameScreen {
	private static GameScreen2 instance;
	private List<Enemy> enemies = new ArrayList<Enemy>();

	private GameScreen2() {
		init();
	}

	protected void init() {
		super.init("images/background2.png", 100, 1024, 1024);
		createObstacles("maps/map1");
		Enemy enemy = new Enemy(this, world, 230, 260, 118, 1);
		EnemyActor enemyActor = new EnemyActor(enemy);
		stage.addActor(enemyActor);
		enemies.add(enemy);
	}

	private void createObstacle(List<Vector2> obstacle) {
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 0));

		EarClippingTriangulator ect = new EarClippingTriangulator();
		List<Vector2> triangles = ect.computeTriangles(obstacle);

		for (int i = 0; i < triangles.size(); i++) {
			if ((i + 1) % 3 == 0) {
				Body groundBody = world.createBody(groundBodyDef);  
				PolygonShape groundBox = new PolygonShape();
				Vector2[] v = new Vector2[] {triangles.get(i), triangles.get(i-1), triangles.get(i-2)};
				groundBox.set(v);
				groundBody.createFixture(groundBox, 0.0f);
			}
		}

		stage.addActor(new ObstacleActor(obstacle, triangles));
	}

	private void createObstacles(String map) {
		FileHandle file = Gdx.files.internal(map);
		BufferedReader in = file.reader(512);

		String strLine;
		List<Vector2> obstacle = new ArrayList<Vector2>();

		try {
			while ((strLine = in.readLine()) != null) {
				if (strLine.equals("-")) {
					createObstacle(obstacle);
					obstacle = new ArrayList<Vector2>();
				} else {
					String coords[] = strLine.split(",");
					obstacle.add(new Vector2(Float.valueOf(coords[0]), Float.valueOf(coords[1])));
				}
				System.out.println(strLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (obstacle.size() > 0) {
			createObstacle(obstacle);
		}
	}

	public static GameScreen2 getInstance() {
		if (instance == null) {
			instance = new GameScreen2();
		}
		return instance;
	}

	@Override
	protected void step(float delta) {
		super.step(delta);
		if (spaceship.tryFire()) {
			System.out.println("Fire !");
			stage.addActor(spaceship.createBullet());
		}

		Iterator<Enemy> it = enemies.iterator();
		while (it.hasNext()) {
			Enemy enemy = it.next();
			if (enemy.alive()) {
				enemy.step();
			} else {
				it.remove();
			}
		}
	}

	@Override
	public void beginContact(Contact contact) {
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		if (bodyA.getUserData() instanceof BulletActor
				|| bodyB.getUserData() instanceof BulletActor) {
			Body bulletBody;
			Body other;
			if (bodyA.getUserData() instanceof BulletActor) {
				bulletBody = bodyA;
				other = bodyB;
			} else {
				bulletBody = bodyB;
				other = bodyA;
			}
			BulletActor bulletActor = (BulletActor)bulletBody.getUserData();
			if (other.getUserData() instanceof SpaceShipActor) {
				if (bulletActor.isEnemy()) {
					if (spaceship.getLife() > 0 && !spaceship.getAlreadyHit()) {
						spaceship.updateLife(-1);
						spaceship.setHit();
					}
				}
			} else if (other.getUserData() == null) {
				stage.getRoot().removeActor(bulletActor);
				bulletBody.setUserData(-1);
			} else if (other.getUserData() instanceof EnemyActor && !bulletActor.isEnemy()) {
				EnemyActor enemyActor = (EnemyActor)other.getUserData();
				enemyActor.getEnemy().hit();
				stage.getRoot().removeActor(bulletActor);
				bulletBody.setUserData(-1);
				if (!enemyActor.getEnemy().alive()) {
					stage.getRoot().removeActor(enemyActor);
					other.setUserData(-1);
				}
				System.out.println("Enemy hit!");
			}
			return;
		}


		if (contact.getFixtureA().getBody() == spaceship.getSpaceShipBody()
				|| contact.getFixtureB().getBody() == spaceship.getSpaceShipBody()) {

			WorldManifold manifold = contact.getWorldManifold();

			boolean base = true;
			for (int i = 0; i < manifold.getNumberOfContactPoints(); i++) {
				Vector2 v = manifold.getPoints()[i];
				Vector2 localV = spaceship.getSpaceShipBody().getLocalPoint(v);

				if (!spaceship.isBase(localV)) {
					base = false;
				}
			}

			Vector2 velocity = spaceship.getSpaceShipBody().getLinearVelocity();
			if (!base || velocity.y > 20f || (velocity.y * velocity.y + velocity.x * velocity.x) > 14000) {
				if (spaceship.getLife() > 0 && !spaceship.getAlreadyHit()) {
					spaceship.updateLife(-1);
					spaceship.setHit();
					System.out.println("bam! " + spaceship.getLife());
					contact.setRestitution(0.7f); // La restitution est plus forte.
				}
			}
		}
	}

}
