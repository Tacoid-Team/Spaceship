package com.tacoid.spaceship;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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

public class Spaceship implements ApplicationListener {
	World world;  
	Box2DDebugRenderer debugRenderer;  
	Body body;
	private Stage stage, stage_ui;
	static final float BOX_STEP=1/60f;  
	static final int BOX_VELOCITY_ITERATIONS=6;  
	static final int BOX_POSITION_ITERATIONS=2;  
	static final float WORLD_TO_BOX=0.01f;  
	static final float BOX_WORLD_TO=100f;
	boolean engineLeftOn = false;
	boolean engineRightOn = false;

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

	private void createEngineButtons() {
		EngineButton buttonLeft = new EngineButton(true, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_off.png")), 64, 64)), new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_on.png")), 64, 64)));
		EngineButton buttonRight = new EngineButton(false, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_off.png")), 64, 64)), new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/engine_on.png")), 64, 64)));
		buttonLeft.setPosition(0, 0);
		buttonRight.setPosition(480 - 64, 0);
		stage_ui.addActor(buttonLeft);
		stage_ui.addActor(buttonRight);
	}

	private void createSpaceship() {
		Camera camera = stage.getCamera();   

		//Dynamic Body  
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.DynamicBody;  
		bodyDef.position.set(camera.viewportWidth / 4, camera.viewportHeight / 2);

		body = world.createBody(bodyDef);  
		PolygonShape dynamicShape = new PolygonShape();
		Vector2[] vertices = new Vector2[3];
		vertices[0] = new Vector2(16, 26);
		vertices[1] = new Vector2(0, 0);
		vertices[2] = new Vector2(32, 0);
		dynamicShape.set(vertices);
		body.setAngularDamping(2f);
		body.setLinearDamping(0.1f);

		Actor sprite = new Image(new TextureRegion(new Texture(Gdx.files.internal("images/spaceship.png")), 32, 26));
		body.setUserData(sprite);
		stage.addActor(sprite);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicShape; 
		fixtureDef.density = 1.0f;  
		fixtureDef.friction = 0.5f;  
		fixtureDef.restitution = 0.3f;
		body.createFixture(fixtureDef);
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
		float rot = body.getAngularVelocity();

		if ((engineRightOn && engineLeftOn) || (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.LEFT))) {
			body.applyLinearImpulse(body.getWorldVector(new Vector2(0, 4000)), body.getWorldCenter());
		} else {
			if ((engineRightOn || Gdx.input.isKeyPressed(Keys.RIGHT)) && rot > -3) {
				body.applyAngularImpulse(-5000);
			} 
			if ((engineLeftOn || Gdx.input.isKeyPressed(Keys.LEFT)) && rot < 3) {
				body.applyAngularImpulse(5000);
			}
		}
	}
	
	private void propulse2() {
		float rot = body.getAngularVelocity();

		if ((engineRightOn || Gdx.input.isKeyPressed(Keys.RIGHT)) && rot > -3) {
			body.applyLinearImpulse(body.getWorldVector(new Vector2(10, 2000)), body.getWorldPoint(new Vector2(4, 0)));
		} 
		if ((engineLeftOn || Gdx.input.isKeyPressed(Keys.LEFT)) && rot < 3) {
			body.applyLinearImpulse(body.getWorldVector(new Vector2(-10, 2000)), body.getWorldPoint(new Vector2(28, 0)));
		}
	}
	
	@Override
	public void create() {
		stage = new Stage(480, 800,	false);
		stage_ui = new Stage(480, 800, false);
		Actor bgActor = new Image(new TextureRegion(new Texture(Gdx.files.internal("images/background1.png")), 480, 4000));
		bgActor.setPosition(0, 65);
		stage.addActor(bgActor);
		createEngineButtons();

		world = new World(new Vector2(0, 0), true);		
		createWalls();
		createSpaceship();

		debugRenderer = new Box2DDebugRenderer();

		Gdx.input.setInputProcessor(stage_ui);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render() {		
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

	private void centerCamera() {
		float y = body.getPosition().y;
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
}
