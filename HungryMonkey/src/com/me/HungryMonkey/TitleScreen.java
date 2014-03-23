package com.me.HungryMonkey;

import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.me.HungryMonkey.tweens.ActorAccessor;

/**
 * Credits to Dermetfan for providing most of this source code via
 * YouTube/BitBucket.
 * 
 * @author Biru
 * 
 */
public class TitleScreen implements Screen {
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

	public TitleScreen(final HungryMonkey hmg) {
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
					.setScreen(new TitleScreen(hmg));
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

		// creating heading
		Label heading = new Label("Hungry Monkey", skin);
		heading.setFontScale(3f);
		heading.setColor(Color.WHITE);
		
		//creating credits
		Label credits = new Label("Music | FoxSynergy | OpenGameArt.org \nProgramming | Ninjabit  | @ninjabit6" +
				"\nThanks | Master484, Q_x | OpenGameArt.org", skin);
		credits.setFontScale(0.7f);
		
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
												.setScreen(new HowtoScreen(hmg));
									}
								})).end().start(tweenManager);

			}

		});
		buttonPlay.pad(15);

		TextButton buttonExit = new TextButton("Exit", skin, "default");
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

		// putting stuff together

		table.add(heading).spaceBottom(50f).row();
		table.add(buttonPlay).spaceBottom(25f).minWidth(200f).row();
		table.add(buttonExit).minWidth(200f);

		stage.addActor(background);
		stage.addActor(table);
		stage.addActor(credits);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// background fading
		Timeline.createSequence()
				.beginSequence()
				.push(Tween.to(background, ActorAccessor.RGB, 0.0f).target(
						0.30f, 0.30f, 0.30f)).end().start(tweenManager);

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
		stage.dispose();
		skin.dispose();
	}

}
