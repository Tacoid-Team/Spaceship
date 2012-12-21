package com.tacoid.spaceship.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tacoid.spaceship.Spaceship;

public class GameScreen implements Screen {
	private static GameScreen instance;
	private World world;  
	private Box2DDebugRenderer debugRenderer;  
	private Body spaceShipBody;
	private SpaceShipActor spaceShipActor;
	private Stage stage, stage_ui;
	static final float BOX_STEP=1/60f;  
	static final int BOX_VELOCITY_ITERATIONS=6;  
	static final int BOX_POSITION_ITERATIONS=2;  
	static final float WORLD_TO_BOX=0.01f;  
	static final float BOX_WORLD_TO=100f;
	private boolean engineLeftOn = false;
	private boolean engineRightOn = false;
	private int life = 100;

	private class SpaceShipActor extends Actor {
		private TextureRegion activeSprite;
		private TextureRegion none, both, left, right;
		
		public SpaceShipActor() {
			none = new TextureRegion(Spaceship.manager.get("images/spaceship.png", Texture.class), 32, 32);
			both = new TextureRegion(Spaceship.manager.get("images/spaceship_both.png", Texture.class), 32, 32);
			left = new TextureRegion(Spaceship.manager.get("images/spaceship_left.png", Texture.class), 32, 32);
			right = new TextureRegion(Spaceship.manager.get("images/spaceship_right.png", Texture.class), 32, 32);
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
	
	private class EngineButton extends Button {

		public EngineButton(final boolean isLeft, Drawable up, Drawable down) {
			super(up, down);
			this.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchDown(event, x, y, pointer, button);
					if (isLeft) {
						engineLeftOn = true;
					} else {
						engineRightOn = true;
					}
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);
					if (isLeft) {
						engineLeftOn = false;
					} else {
						engineRightOn = false;
					}
					return;
				}
			});
		}

	}

	private GameScreen() {
		init();
	}
	
	
	private void createEngineButtons() {
		EngineButton buttonLeft = new EngineButton(true, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_off.png")), 64, 64)), new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_on.png")), 64, 64)));
		EngineButton buttonRight = new EngineButton(false, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_off.png")), 64, 64)), new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_on.png")), 64, 64)));
		buttonRight.setPosition(0, 0);
		buttonLeft.setPosition(480 - 64, 0);
		stage_ui.addActor(buttonLeft);
		stage_ui.addActor(buttonRight);
	}

	private void createStone(int x, int y, int size, float initialAngularSpeed, Vector2 initialVector) {
		//Dynamic Body
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody;  
		bodyDef.position.set(x, y);

		Body body = world.createBody(bodyDef);  
		CircleShape dynamicShape = new CircleShape();
		dynamicShape.setRadius(size / 2);
		dynamicShape.setPosition(new Vector2(size / 2, size / 2));

		Actor sprite = new Image(new TextureRegion(Spaceship.manager.get("images/stone.png", Texture.class)));
		sprite.setSize(size, size);
		body.setUserData(sprite);
		stage.addActor(sprite);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicShape; 
		fixtureDef.density = 10.0f;  
		fixtureDef.friction = 1.0f;  
		fixtureDef.restitution = 0.5f;
		body.createFixture(fixtureDef);
	
		body.setAngularVelocity(initialAngularSpeed);
		body.setLinearVelocity(initialVector);
	}
	
	private void createSpaceship(int x, int y) {
		Camera camera = stage.getCamera();   

		//Dynamic Body  
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody;  
		bodyDef.position.set(x, y);

		spaceShipBody = world.createBody(bodyDef);  
		PolygonShape dynamicShape = new PolygonShape();
		Vector2[] vertices = new Vector2[3];
		vertices[0] = new Vector2(16, 32);
		vertices[1] = new Vector2(0, 6);
		vertices[2] = new Vector2(32, 6);
		dynamicShape.set(vertices);
		spaceShipBody.setAngularDamping(2f);
		spaceShipBody.setLinearDamping(0.1f);

		spaceShipActor = new SpaceShipActor();
		spaceShipBody.setUserData(spaceShipActor);
		stage.addActor(spaceShipActor);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicShape; 
		fixtureDef.density = 1.0f;  
		fixtureDef.friction = 0.5f;  
		fixtureDef.restitution = 0.1f;
		spaceShipBody.createFixture(fixtureDef);
	}

	private void createWalls() {
		Camera camera = stage.getCamera();   

		BodyDef groundBodyDef =new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 65));  
		Body groundBody = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();  
		groundBox.setAsBox((camera.viewportWidth), 1.0f);  
		groundBody.createFixture(groundBox, 0.0f);

		groundBodyDef =new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 4065));  
		groundBody = world.createBody(groundBodyDef);  
		groundBox = new PolygonShape();  
		groundBox.setAsBox((camera.viewportWidth), 1.0f);  
		groundBody.createFixture(groundBox, 0.0f);

		groundBodyDef =new BodyDef();  
		groundBodyDef.position.set(new Vector2(1, 65));  
		groundBody = world.createBody(groundBodyDef);  
		groundBox = new PolygonShape();  
		groundBox.setAsBox(1.0f, 4000);  
		groundBody.createFixture(groundBox, 0.0f);

		groundBodyDef =new BodyDef();
		groundBodyDef.position.set(new Vector2(479, 65));  
		groundBody = world.createBody(groundBodyDef);  
		groundBox = new PolygonShape();
		groundBox.setAsBox(1.0f, 4000);
		groundBody.createFixture(groundBox, 0.0f);
	}

	private void propulse() {
		float rot = spaceShipBody.getAngularVelocity();

		if ((engineRightOn && engineLeftOn) || (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.LEFT))) {
			spaceShipActor.setBoth();
			spaceShipBody.applyLinearImpulse(spaceShipBody.getWorldVector(new Vector2(0, 4000)), spaceShipBody.getWorldCenter());
		} else {
			if ((engineLeftOn || Gdx.input.isKeyPressed(Keys.RIGHT)) && rot > -3) {
				spaceShipActor.setLeft();
				spaceShipBody.applyAngularImpulse(-5000);
			} else if ((engineRightOn || Gdx.input.isKeyPressed(Keys.LEFT)) && rot < 3) {
				spaceShipActor.setRight();
				spaceShipBody.applyAngularImpulse(5000);
			} else {
				spaceShipActor.setNone();
			}
		}
	}
	
	private void propulse2() {
		float rot = spaceShipBody.getAngularVelocity();

		if ((engineRightOn || Gdx.input.isKeyPressed(Keys.RIGHT)) && rot > -3) {
			spaceShipBody.applyLinearImpulse(spaceShipBody.getWorldVector(new Vector2(10, 2000)), spaceShipBody.getWorldPoint(new Vector2(4, 0)));
		} 
		if ((engineLeftOn || Gdx.input.isKeyPressed(Keys.LEFT)) && rot < 3) {
			spaceShipBody.applyLinearImpulse(spaceShipBody.getWorldVector(new Vector2(-10, 2000)), spaceShipBody.getWorldPoint(new Vector2(28, 0)));
		}
	}
	
	public void init() {
		stage = new Stage(480, 800,	false);
		stage_ui = new Stage(480, 800, false);
				
		Actor bgActor = new Image(new TextureRegion(Spaceship.manager.get("images/background1.png", Texture.class), 240, 2000));
		bgActor.setSize(480, 4000);
		bgActor.setPosition(0, 65);
		stage.addActor(bgActor);

		createEngineButtons();

		world = new World(new Vector2(0, 0), true);		
		createWalls();
		createSpaceship(240, 65);

		createStone(300, 3800, 200, 0.2f, new Vector2(-10, -10));
		createStone(250, 3700, 64, 0.2f, new Vector2(0, 10));
		createStone(350, 3600, 64, 0.2f, new Vector2(0, 50));
		createStone(50, 3500, 64, 0.2f, new Vector2(0, 10));
		createStone(100, 3500, 64, -0.2f, new Vector2(0, 20));
		createStone(300, 3500, 64, 0.2f, new Vector2(0, -10));
		createStone(300, 3400, 200, 0.2f, new Vector2(-10, 10));
		createStone(100, 3300, 128, -0.2f, new Vector2(-10, 0));
		createStone(250, 3300, 48, 0.6f, new Vector2(-10, -10));
		createStone(200, 3200, 48, 0.1f, new Vector2(-10, -10));
		createStone(300, 3100, 200, 0.2f, new Vector2(-10, -10));
		createStone(250, 2900, 64, 0.2f, new Vector2(0, 10));
		createStone(350, 2900, 64, 0.2f, new Vector2(0, 50));
		createStone(50, 2800, 64, 0.2f, new Vector2(0, 20));
		createStone(100, 2800, 64, -0.2f, new Vector2(0, 0));
		createStone(300, 2800, 64, 0.2f, new Vector2(0, -10));
		createStone(300, 2700, 200, 0.2f, new Vector2(-10, -10));
		createStone(100, 2600, 128, -0.2f, new Vector2(-10, 0));
		createStone(250, 2600, 48, 0.6f, new Vector2(-10, -100));
		createStone(200, 2500, 48, 0.1f, new Vector2(-10, -100));
		createStone(300, 2400, 200, 0.2f, new Vector2(-10, -10));
		createStone(250, 2300, 64, 0.2f, new Vector2(0, 10));
		createStone(350, 2300, 64, 0.2f, new Vector2(0, -100));
		createStone(50, 2300, 64, 0.2f, new Vector2(0, -10));
		createStone(100, 2300, 64, -0.2f, new Vector2(0, 0));
		createStone(300, 2300, 64, 0.2f, new Vector2(0, -10));
		createStone(300, 2200, 200, 0.2f, new Vector2(-10, -10));
		createStone(100, 2100, 128, -0.2f, new Vector2(-10, 0));
		createStone(250, 2000, 48, 0.6f, new Vector2(-10, -100));
		createStone(200, 1900, 48, 0.1f, new Vector2(-10, -10));
		createStone(300, 1800, 48, 0.6f, new Vector2(-10, -100));
		createStone(100, 1700, 128, 0.6f, new Vector2(-10, 10));
		createStone(200, 1500, 200, 0.1f, new Vector2(-10, -10));
		createStone(200, 1300, 64, 0.6f, new Vector2(-10, -100));
		createStone(300, 1200, 64, 0.3f, new Vector2(-10, 0));
		createStone(250, 1100, 128, 0.3f, new Vector2(-10, -10));
		createStone(200, 900, 92, 0.1f, new Vector2(-1, -10));
		createStone(100, 800, 64, 0.5f, new Vector2(10, -5));
		createStone(300, 600, 64, 0.6f, new Vector2(-10, -10));
		createStone(50, 400, 100, -0.1f, new Vector2(-8, 2));

		debugRenderer = new Box2DDebugRenderer();

		Gdx.input.setInputProcessor(stage_ui);
	}

	@Override
	public void dispose() {

	}

	private void centerCamera() {
		float y = spaceShipBody.getPosition().y;
		if (y < 400) y = 400;
		else if (y > 3665) y = 3665;
		stage.getCamera().position.y = y;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		propulse();

		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);

		Iterator<Body> bi = world.getBodies();

		while (bi.hasNext()){
			Body b = bi.next();

			// Get the bodies user data - in this example, our user 
			// data is an instance of the Entity class
			Actor e = (Actor) b.getUserData();

			if (e != null) {
				// Update the entities/sprites position and angle
				e.setPosition(b.getPosition().x, b.getPosition().y);
				// We need to convert our angle from radians to degrees
				e.setRotation(MathUtils.radiansToDegrees * b.getAngle());
			}
		}

		centerCamera(); 

		stage.act(Gdx.graphics.getDeltaTime());
		stage_ui.act(Gdx.graphics.getDeltaTime());
		
		stage.draw();
		stage_ui.draw();	
		//debugRenderer.render(world, stage.getCamera().combined);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


	public static GameScreen getInstance() {
		if (instance == null) {
			instance = new GameScreen();
		}
		return instance;
	}
}
