package com.me.HungryMonkey;

import java.util.Random;

import com.me.HungryMonkey.tweens.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Credits to Dermetfan for providing most of this source code via
 * YouTube/BitBucket.
 * 
 * @author Biru
 * 
 */
public class HowtoScreen implements Screen {
	SpriteBatch batch;
	HungryMonkey hmg;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Random ran = new Random();
	private Vector2 mousePos;

	private Stage stage;
	private Skin skin;
	private Table table;
	private TweenManager tweenManager;

	public HowtoScreen(final HungryMonkey hmg) {
		this.hmg = hmg;
		this.batch = hmg.batch;
		this.font = hmg.font;

		this.camera = new OrthographicCamera();
		font.setScale(5f);

		mousePos = new Vector2();

		Texture.setEnforcePotImages(false);

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		batch.end();

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new HowtoScreen(hmg));
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"),
				new TextureAtlas("ui/atlas.pack"));

		table = new Table(skin);
		table.setFillParent(true);

		// creating background
		Image background = new Image(hmg.atlas.findRegion("background"));
		background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Image instructions = new Image(hmg.atlas.findRegion("instructions"));
		instructions.setBounds(50, 50, Gdx.graphics.getWidth() / 1.15f, Gdx.graphics.getHeight() / 1.15f);

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
										((Game) Gdx.app
												.getApplicationListener())
												.setScreen(new PlayScreen(hmg));
									}
								})).end().start(tweenManager);

			}

		});
		buttonPlay.setWidth(200f);
		buttonPlay.setHeight(60f);

		stage.addActor(background);
		stage.addActor(instructions);
		stage.addActor(buttonPlay);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// beginning places
		Tween.set(background, ActorAccessor.RGB).target(0.30f, 0.30f, 0.30f)
				.start(tweenManager);

		// drop in
		Tween.from(instructions, ActorAccessor.Y, .75f)
				.target(Gdx.graphics.getHeight() / 8).start(tweenManager);
		Tween.set(buttonPlay, ActorAccessor.X)
				.target(Gdx.graphics.getWidth() / 2 - 100).start(tweenManager);
		Tween.from(buttonPlay, ActorAccessor.Y, .75f).target(-100)
				.start(tweenManager);
		Tween.to(buttonPlay, ActorAccessor.Y, .75f)
				.target(Gdx.graphics.getHeight() / 10).start(tweenManager);

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
		stage.dispose();
		skin.dispose();
	}

}
