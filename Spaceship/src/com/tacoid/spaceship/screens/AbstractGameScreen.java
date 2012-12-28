package com.tacoid.spaceship.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.tacoid.spaceship.actors.EngineButton.Direction;
import com.tacoid.spaceship.actors.LifeActor;
import com.tacoid.spaceship.objects.Spaceship;

public class AbstractGameScreen implements Screen, ContactListener {
	protected World world;
	protected int WORLD_HEIGHT = 1024;
	protected int WORLD_WIDTH = 1024;
	protected Stage stage, stage_ui;
	protected Spaceship spaceship;
	private Box2DDebugRenderer debugRenderer;
	private int gravity;
	private static final float BOX_STEP = 1/60f;
	private static final int BOX_VELOCITY_ITERATIONS = 6;  
	private static final int BOX_POSITION_ITERATIONS = 2;  
	
	protected void init(String background, int gravity, int world_width, int world_height) {
		this.gravity = gravity;
		WORLD_WIDTH = world_width;
		WORLD_HEIGHT = world_height;
		stage = new Stage(480, 800,	false);
		stage_ui = new Stage(480, 800, false);
		
		// Image de fond.
		Actor bgActor = new Image(SpaceshipGame.manager.get(background, Texture.class));
		bgActor.setSize(WORLD_WIDTH, WORLD_HEIGHT);
		bgActor.setPosition(0, 0);
		stage.addActor(bgActor);
		
		// Création du monde.
		world = new World(new Vector2(0, -gravity), true);
		
		// Murs autour.
		createWalls();
		
		// Création du spaceship.
		spaceship = new Spaceship(world, 240, 20, 5);
		stage.addActor(spaceship.getActor());
		LifeActor lifeActor = new LifeActor(spaceship);
		lifeActor.setPosition(20, 700);
		stage_ui.addActor(lifeActor);

		
		debugRenderer = new Box2DDebugRenderer();
		createEngineButtons();
		world.setContactListener(this);
		Gdx.input.setInputProcessor(new KeyboardHandler(stage_ui, spaceship));
	}
	
	protected void initBackground(String image) {
		Actor bgActor = new Image(SpaceshipGame.manager.get(image, Texture.class));
		bgActor.setSize(WORLD_WIDTH, WORLD_HEIGHT);
		bgActor.setPosition(0, 0);
		stage.addActor(bgActor);
	}
	
	protected void createWalls() {
		// ground.
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 0));  
		Body groundBody = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();  
		groundBox.setAsBox(WORLD_WIDTH, 1.0f);  
		groundBody.createFixture(groundBox, 0.0f);

		// top
		groundBodyDef =new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, WORLD_HEIGHT));  
		groundBody = world.createBody(groundBodyDef);  
		groundBox = new PolygonShape();  
		groundBox.setAsBox(WORLD_WIDTH, 1.0f);  
		groundBody.createFixture(groundBox, 0.0f);

		// left
		groundBodyDef =new BodyDef();
		groundBodyDef.position.set(new Vector2(1, 0));  
		groundBody = world.createBody(groundBodyDef);  
		groundBox = new PolygonShape();
		groundBox.setAsBox(1.0f, WORLD_HEIGHT);
		groundBody.createFixture(groundBox, 0.0f);

		// right
		groundBodyDef =new BodyDef();
		groundBodyDef.position.set(new Vector2(WORLD_WIDTH, 0));
		groundBody = world.createBody(groundBodyDef);
		groundBox = new PolygonShape();
		groundBox.setAsBox(1.0f, WORLD_HEIGHT);
		groundBody.createFixture(groundBox, 0.0f);
	}
	
	protected void createEngineButtons() {
		EngineButton buttonBoth = new EngineButton(spaceship, Direction.UP, new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/both_off.png", Texture.class), 100, 100)), new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/both_on.png", Texture.class), 100, 100)));
		EngineButton buttonLeft = new EngineButton(spaceship, Direction.LEFT, new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/left_off.png", Texture.class), 100, 100)), new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/left_on.png", Texture.class), 100, 100)));
		EngineButton buttonRight = new EngineButton(spaceship, Direction.RIGHT, new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/right_off.png", Texture.class), 100, 100)), new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/right_on.png", Texture.class), 100, 100)));
		buttonBoth.setPosition(5, 50);
		buttonRight.setPosition(480 - 105, 50);
		buttonLeft.setPosition(480 - 210, 50);
		stage_ui.addActor(buttonLeft);
		stage_ui.addActor(buttonRight);
		stage_ui.addActor(buttonBoth);
	}
	
	protected void centerCamera() {
		float y = spaceship.getY();
		float vH2 = stage.getCamera().viewportHeight / 2;
		if (y < vH2) y = vH2;
		else if (y > WORLD_HEIGHT - vH2) y = WORLD_HEIGHT - vH2;
		stage.getCamera().position.y = y;

		float x = spaceship.getX();
		float vW2 = stage.getCamera().viewportWidth / 2;
		if (x < vW2) x = vW2;
		else if (x > WORLD_WIDTH - vW2) x = WORLD_WIDTH - vW2;
		stage.getCamera().position.x = x;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
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
	
	protected void step(float delta) {
		spaceship.propulse();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		step(delta);

		//spaceship.getSpaceShipBody().applyForceToCenter(new Vector2(0, -gravity));
		
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);

		Iterator<Body> bi = world.getBodies();

		while (bi.hasNext()){
			Body b = bi.next();

			// Get the bodies user data - in this example, our user 
			// data is an instance of the Entity class
			if (b.getUserData() instanceof Integer && (Integer)b.getUserData() == -1) {
				world.destroyBody(b);
			}
			
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
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
