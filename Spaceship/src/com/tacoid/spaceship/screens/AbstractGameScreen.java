package com.tacoid.spaceship.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
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
import com.tacoid.spaceship.actors.ReplayButton;
import com.tacoid.spaceship.actors.SpaceshipButton;
import com.tacoid.spaceship.actors.SpaceshipButton.ButtonAction;
import com.tacoid.spaceship.actors.LifeActor;
import com.tacoid.spaceship.objects.Spaceship;

public class AbstractGameScreen implements Screen, ContactListener {
	protected World world;
	protected int start_x, start_y;
	protected int end_x, end_y;
	protected int WORLD_HEIGHT = 1024;
	protected int WORLD_WIDTH = 1024;
	protected Stage stage, stage_ui;
	protected Spaceship spaceship;
	private Box2DDebugRenderer debugRenderer;
	private int initialLife;
	private static final float BOX_STEP = 1/60f;
	private static final int BOX_VELOCITY_ITERATIONS = 6;  
	private static final int BOX_POSITION_ITERATIONS = 2;
	public static final float WORLD_TO_BOX = 0.5f;
	public static final float BOX_TO_WORLD = 1/WORLD_TO_BOX;
	
	public Spaceship getSpaceship() {
		return spaceship;
	}
	
	public void addActor(Actor actor) {
		stage.addActor(actor);
	}
	
	protected void init(String map, String background, int gravity, int world_width, int world_height) {
		WORLD_WIDTH = world_width;
		WORLD_HEIGHT = world_height;
		stage = new Stage(480, 800,	false);
		stage_ui = new Stage(480, 800, false);
		
		// Image de fond.
		Actor bgActor = new Image(SpaceshipGame.manager.get(background, Texture.class));
		bgActor.setSize(WORLD_WIDTH, WORLD_HEIGHT);
		bgActor.setPosition(0, 0);
		stage.addActor(bgActor);
		
		ReplayButton replayButton = new ReplayButton(this);
		replayButton.setPosition(400, 720);
		stage_ui.addActor(replayButton);
		
		// Création du monde.
		world = new World(new Vector2(0, -gravity), true);
		
		// Get start_x et start_y.
		parseStart(map + "/start");
		
		// Get end_x et end_y.
		parseEnd(map + "/end");		
		
		// Création du spaceship.
		spaceship = new Spaceship(world, start_x * WORLD_TO_BOX, start_y * WORLD_TO_BOX, initialLife);
		stage.addActor(spaceship.getActor());
		LifeActor lifeActor = new LifeActor(spaceship);
		lifeActor.setPosition(20, 720);
		stage_ui.addActor(lifeActor);

		debugRenderer = new Box2DDebugRenderer();
		createEngineButtons();
		world.setContactListener(this);
		Gdx.input.setInputProcessor(new KeyboardHandler(stage_ui, spaceship));
	}
	
	private void parseStart(String startFile) {
		FileHandle file = Gdx.files.internal(startFile);
		String[] coord = file.readString().replaceAll("(\\r|\\n)", "").split(",");
		start_x = Integer.parseInt(coord[0]);
		start_y = Integer.parseInt(coord[1]);
		initialLife = Integer.parseInt(coord[2]);
	}
	
	private void parseEnd(String endFile) {
		FileHandle file = Gdx.files.internal(endFile);
		String[] coord = file.readString().replaceAll("(\\r|\\n)", "").split(",");
		end_x = Integer.parseInt(coord[0]);
		end_y = Integer.parseInt(coord[1]);
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
		SpaceshipButton buttonBoth = new SpaceshipButton(spaceship, ButtonAction.UP, new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/both_off.png", Texture.class))), new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/both_on.png", Texture.class))));
		buttonBoth.setPosition(5, 50);
		stage_ui.addActor(buttonBoth);
		
		SpaceshipButton buttonFire = new SpaceshipButton(spaceship, ButtonAction.FIRE, new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/fire_off.png", Texture.class))), new TextureRegionDrawable(new TextureRegion(SpaceshipGame.manager.get("images/fire_on.png", Texture.class))));
		buttonFire.setPosition(480 - 128, 50);
		stage_ui.addActor(buttonFire);
	}
	
	protected void centerCamera() {
		float y = spaceship.getY() * BOX_TO_WORLD;
		float vH2 = stage.getCamera().viewportHeight / 2;
		if (y < vH2) y = vH2;
		else if (y > WORLD_HEIGHT - vH2) y = WORLD_HEIGHT - vH2;
		stage.getCamera().position.y = y;

		float x = spaceship.getX() * BOX_TO_WORLD;
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
		
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);

		Iterator<Body> bi = world.getBodies();

		while (bi.hasNext()){
			Body b = bi.next();
			if (b == null) continue;

			// Get the bodies user data - in this example, our user 
			// data is an instance of the Entity class
			if (b.getUserData() instanceof Integer && (Integer)b.getUserData() == -1) {
				world.destroyBody(b);
			}
			
			Actor e = (Actor) b.getUserData();

			if (e != null) {
				// Update the entities/sprites position and angle
				e.setPosition(b.getPosition().x * BOX_TO_WORLD, b.getPosition().y * BOX_TO_WORLD);
				// We need to convert our angle from radians to degrees
				e.setRotation(MathUtils.radiansToDegrees * b.getAngle());
			}
		}

		centerCamera();

		stage.act(Gdx.graphics.getDeltaTime());
		stage_ui.act(Gdx.graphics.getDeltaTime());
		
		stage.draw();
		stage_ui.draw();
		/*Matrix4 debugMatrix = new Matrix4(stage.getCamera().combined);
		debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);
		debugRenderer.render(world, debugMatrix);*/
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

	public void restart() {
		spaceship.moveTo(start_x * WORLD_TO_BOX, start_y * WORLD_TO_BOX, 0, true);
		spaceship.reinitLife();
		System.out.println("Restart game !");
	}
}
