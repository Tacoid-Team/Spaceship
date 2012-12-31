package com.tacoid.spaceship.screens;

import java.io.BufferedReader;
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
import com.tacoid.spaceship.actors.ExplosionActor;
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
		super.init("images/background2.png", 50, 1024, 1024);
		createObstacles("maps/map1");
		createEnemies("maps/enemies1");
	}

	private Vector2 convertToBox(Vector2 v) {
		return new Vector2(v.x * WORLD_TO_BOX, v.y * WORLD_TO_BOX);
	}
	
	private Vector2 convertToWorld(Vector2 v) {
		return new Vector2(v.x * BOX_TO_WORLD, v.y * BOX_TO_WORLD);
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
				Vector2[] v = new Vector2[] {convertToBox(triangles.get(i)), convertToBox(triangles.get(i-1)), convertToBox(triangles.get(i-2))};
				groundBox.set(v);
				groundBody.createFixture(groundBox, 0.0f);
			}
		}

		stage.addActor(new ObstacleActor(obstacle, triangles));
	}

	private void createEnemies(String f) {
		FileHandle file = Gdx.files.internal(f);
		BufferedReader in = file.reader(512);

		String strLine;
		try {
			while ((strLine = in.readLine()) != null) {
				String infos[] = strLine.split(",");
				int x = Integer.valueOf(infos[0]);
				int y = Integer.valueOf(infos[1]);
				int angle = Integer.valueOf(infos[2]);
				int life = Integer.valueOf(infos[3]);

				Enemy enemy = new Enemy(this, world, x * WORLD_TO_BOX, y * WORLD_TO_BOX, angle, life);
				EnemyActor enemyActor = new EnemyActor(enemy);
				stage.addActor(enemyActor);
				enemies.add(enemy);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			boolean removeBullet = false;

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
			
			// spaceship hit
			if (other.getUserData() instanceof SpaceShipActor && bulletActor.isEnemy()) {
				if (spaceship.getLife() > 0 && !spaceship.getAlreadyHit()) {
					spaceship.updateLife(-1);
					spaceship.setHit();
					removeBullet = true;
				}

			// enemy hit
			} else if (other.getUserData() instanceof EnemyActor && !bulletActor.isEnemy()) {
				EnemyActor enemyActor = (EnemyActor)other.getUserData();
				enemyActor.getEnemy().hit();
				removeBullet = true;

				if (!enemyActor.getEnemy().alive()) {
					stage.getRoot().removeActor(enemyActor);
					other.setUserData(-1);
					ExplosionActor explosion = new ExplosionActor();
					Vector2 pos = convertToWorld(other.getWorldPoint(new Vector2(16, 16)));
					explosion.setPosition(pos.x, pos.y);
					stage.addActor(explosion);
				}
				System.out.println("Enemy hit!");
				
			// obstacle
			} else if (other.getUserData() == null) {
				removeBullet = true;
			}

			if (removeBullet) {
				stage.getRoot().removeActor(bulletActor);
				bulletBody.setUserData(-1);
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
			if (!base || velocity.y < -60f || velocity.len2() > 14000) {
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
