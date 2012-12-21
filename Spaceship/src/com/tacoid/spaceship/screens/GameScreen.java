package com.tacoid.spaceship.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tacoid.spaceship.KeyboardHandler;
import com.tacoid.spaceship.SpaceshipGame;
import com.tacoid.spaceship.actors.EngineButton;
import com.tacoid.spaceship.objects.Meteorite;
import com.tacoid.spaceship.objects.Spaceship;

public class GameScreen implements Screen, ContactListener {
	private static GameScreen instance;
	private World world;  
	private Box2DDebugRenderer debugRenderer;  

	private Stage stage, stage_ui;
	
	static final float BOX_STEP=1/60f;  
	static final int BOX_VELOCITY_ITERATIONS=6;  
	static final int BOX_POSITION_ITERATIONS=2;  
	static final float WORLD_TO_BOX=0.01f;  
	static final float BOX_WORLD_TO=100f;
	
	private Spaceship spaceship;

	private GameScreen() {
		init();
	}
	
	
	private void createEngineButtons() {
		EngineButton buttonLeft = new EngineButton(spaceship, true, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_off.png")), 64, 64)), new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_on.png")), 64, 64)));
		EngineButton buttonRight = new EngineButton(spaceship, false, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_off.png")), 64, 64)), new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_on.png")), 64, 64)));
		buttonRight.setPosition(0, 0);
		buttonLeft.setPosition(480 - 64, 0);
		stage_ui.addActor(buttonLeft);
		stage_ui.addActor(buttonRight);
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

	
	public void init() {
		stage = new Stage(480, 800,	false);
		stage_ui = new Stage(480, 800, false);
				
		Actor bgActor = new Image(new TextureRegion(SpaceshipGame.manager.get("images/background1.png", Texture.class), 240, 2000));
		bgActor.setSize(480, 4000);
		bgActor.setPosition(0, 65);
		stage.addActor(bgActor);

		world = new World(new Vector2(0, 0), true);		
		createWalls();
		createMeteorite();		
		spaceship = new Spaceship(world, 240, 65);
		stage.addActor(spaceship.getActor());

		debugRenderer = new Box2DDebugRenderer();
		
		world.setContactListener(this);
		createEngineButtons();
		Gdx.input.setInputProcessor(new KeyboardHandler(stage_ui, spaceship));
	}

	private void createStone(int x, int y, int size, float initialAngularSpeed, Vector2 initialVector) {
		stage.addActor(new Meteorite(world, x, y, size, initialAngularSpeed, initialVector).getActor());
	}
	
	private void createMeteorite() {
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
	}


	@Override
	public void dispose() {

	}

	private void centerCamera() {
		float y = spaceship.getY();
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

		spaceship.propulse();

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


	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getBody() == spaceship.getSpaceShipBody()
				|| contact.getFixtureB().getBody() == spaceship.getSpaceShipBody()) {
			if (spaceship.getLife() > 0) {
				spaceship.updateLife(-1);
			}
			System.out.println("life: " + spaceship.getLife());
		}
	}


	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}
