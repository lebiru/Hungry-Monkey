package com.me.HungryMonkey;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.HungryMonkey.tweens.ActorAccessor;

public class GameOver implements Screen, InputProcessor {

	private HungryMonkey hmg;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private TextureAtlas atlas;

	private Stage stage;
	private Skin skin;
	private Table table;
	private TweenManager tweenManager;

	private StopWatch sw;
	private long timeLeft;
	private int points;

	private Sound biteSound, hurtSound;

	public GameOver(final HungryMonkey hmg) {
		this.hmg = hmg;
		this.batch = hmg.batch;
		this.camera = new OrthographicCamera();
		Gdx.input.setInputProcessor(this);
		atlas = hmg.atlas;
		sw = new StopWatch();

		// hmg.currentMusic.stop();

		hmg.font.setColor(Color.GREEN);
		hmg.font.setScale(1.0f);

		// LOADING SOUND
		biteSound = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/bite.wav"));
		hurtSound = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/hurt.wav"));

	}

	@Override
	public void render(float delta) {
		camera.update();

		Gdx.gl.glClearColor(0.4f, 0, 0.0f, 1); // 1 Alpha = no transparency
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.end();

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		if (hmg.currentMusic.isPlaying()) {
			hmg.currentMusic.stop();
		}

		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"),
				new TextureAtlas("ui/atlas.pack"));

		table = new Table(skin);
		table.setFillParent(true);

		Label heading = new Label("Game Over", skin);
		heading.setFontScale(3f);
		
		Label score = new Label("Score: " + hmg.score, skin);
		score.setFontScale(2f);
		
		Label messageLabel = new Label(hmg.message, skin);
		score.setFontScale(1.5f);
		
		
		
		// creating buttons
		TextButton buttonPlay = new TextButton("Play", skin, "default");
		buttonPlay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				hmg.select.play();
				Timeline.createParallel()
				.beginParallel()
				.push(Tween.to(table, ActorAccessor.ALPHA, .75f)
						.target(0))
						.push(Tween.to(table, ActorAccessor.Y, .75f)
								.target(table.getY() + 50)
								.setCallback(new TweenCallback() {

									@Override
									public void onEvent(int type,
											BaseTween<?> source) {

										dispose();
										((Game) Gdx.app.getApplicationListener())
										.setScreen(new PlayScreen(hmg));
									}
								})).end().start(tweenManager);
			}

		});
		buttonPlay.pad(15);

		TextButton buttonExit = new TextButton("Quit", skin, "default");
		buttonExit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				hmg.select.play();
				Timeline.createParallel()
				.beginParallel()
				.push(Tween.to(table, ActorAccessor.ALPHA, .75f)
						.target(0))
						.push(Tween.to(table, ActorAccessor.Y, .75f)
								.target(table.getY() - 50)
								.setCallback(new TweenCallback() {

									@Override
									public void onEvent(int type,
											BaseTween<?> source) {

										dispose();
										Gdx.app.exit();
									}
								})).end().start(tweenManager);
			}
		});
		buttonExit.pad(15);
		
		// creating background
		Image background = new Image(hmg.atlas.findRegion("background"));
		background.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		stage.addActor(background);

		// putting stuff together
		table.add(heading).spaceBottom(50).minWidth(200f).row();
		table.add(messageLabel).spaceBottom(25f).row();
		table.add(score).spaceBottom(50).minWidth(200f).row();
		table.add(buttonPlay).spaceBottom(25f).minWidth(200f).row();
		table.add(buttonExit).minWidth(200f);

		stage.addActor(background);
		stage.addActor(table);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// heading color animation
		Timeline.createSequence()
		.beginSequence()
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
		.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// background fading
		Timeline.createSequence()
		.beginSequence()
		.push(Tween.to(background, ActorAccessor.RGB, 0.5f).target(
				0.30f, 0.30f, 0.30f)).end().start(tweenManager);

		// heading and buttons fade-in
		Timeline.createSequence()
		.beginSequence()
		.push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
		.push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
		.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
		.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .25f).target(1))
		.push(Tween.to(buttonExit, ActorAccessor.ALPHA, .25f).target(1))
		.end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0)
		.start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f)
		.target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());

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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}