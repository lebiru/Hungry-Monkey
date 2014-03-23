package com.me.HungryMonkey;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.HungryMonkey.tweens.ActorAccessor;

public class PlayScreen implements Screen, InputProcessor {

	private HungryMonkey hmg;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Monkey monkey;
	private TextureAtlas atlas;

	float fruitSpeed = 5f;
	private Array<Fruit> fruits;

	private long lastFruitTime;

	private StopWatch sw;
	private long timeLeft;
	private int points;
	private boolean gameOver;

	private Sound biteSound, hurtSound, chompSound, missedFruit, missedBottle;
	private int roundTime = 10;
	private int miss = 0;

	private Stage stage;
	private Skin skin;
	//private Table table;
	private TweenManager tweenManager;

	public PlayScreen(final HungryMonkey hmg) {
		this.hmg = hmg;
		this.batch = hmg.batch;
		this.camera = new OrthographicCamera();
		Gdx.input.setInputProcessor(this);
		atlas = hmg.atlas;
		sw = new StopWatch();

		hmg.font.setColor(Color.GREEN);
		hmg.font.setScale(1.0f);

		gameOver = false;

		// LOADING SOUND
		biteSound = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/bite.wav"));
		hurtSound = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/hurt.wav"));
		chompSound = Gdx.audio.newSound(Gdx.files
				.internal("Sound/FX/chomp.wav"));
		missedFruit = Gdx.audio.newSound(Gdx.files
				.internal("Sound/FX/missedFruit.wav"));
		missedBottle = Gdx.audio.newSound(Gdx.files
				.internal("Sound/FX/missedBottle.wav"));

		// LOADING MUSIC
		hmg.currentMusic = Gdx.audio.newMusic(Gdx.files
				.internal("Sound/Music/music.ogg"));
		hmg.currentMusic.setLooping(true);

		// LOADING CHARACTERS
		monkey = new Monkey(atlas, Gdx.graphics.getWidth()/2 - 100f, 164);

		lastFruitTime = TimeUtils.nanoTime();

		fruits = new Array<Fruit>();

	}

	private void spawnFruit(TextureAtlas atlas) {
		fruits.add(new Fruit(atlas, fruitSpeed, Gdx.graphics.getWidth() + 70));
		lastFruitTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		camera.update();

		Gdx.gl.glClearColor(0.4f, 0, 0.0f, 1); // 1 Alpha = no transparency
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// UPDATE
		monkey.animatedSprite.setX(monkey.x);
		monkey.animatedSprite.setY(monkey.y);
		timeLeft = roundTime - sw.getElapsedTimeSecs();

		for (Fruit f : fruits) {
			if (f.x + f.fruitAnimatedSprite.getWidth() <= 0) {
				if (f.isFruit) {
					miss++;
					missedFruit.play();
				} else {
					missedBottle.play();
				}
				fruits.removeValue(f, true);
			}
			f.fruitAnimatedSprite.setX(f.x);
			f.fruitAnimatedSprite.setY(f.y);
			f.moveFruit();
		}

		stage.act(delta);
		stage.draw();
		tweenManager.update(delta);
		
		batch.begin();

		monkey.animatedSprite.draw(batch);

		for (Fruit f : fruits) {
			f.fruitAnimatedSprite.draw(batch);
		}

		hmg.font.setScale(1.5f);
		hmg.font.draw(batch, "POINTS: " + points, 3, Gdx.graphics.getHeight() - 20);
		hmg.font.draw(batch, "MISS: " + miss, 3, Gdx.graphics.getHeight() - 40);

		// CHECK
		if (TimeUtils.nanoTime() - lastFruitTime > 1000000000) {
			spawnFruit(atlas);
		}

		// game over hp
		if (miss >= 3) {
			hmg.score = points;
			dispose();
			hmg.message = "You missed 3 fruits and fainted!";
			((Game) Gdx.app.getApplicationListener()).setScreen(new GameOver(
					hmg));

		}

		batch.end();
		
		

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		hmg.currentMusic.stop();
		hmg.currentMusic = Gdx.audio.newMusic(Gdx.files
				.internal("Sound/Music/music.ogg"));
		hmg.currentMusic.setLooping(true);
		hmg.currentMusic.setVolume(0.5f);
		hmg.currentMusic.play();
		sw.start();
		timeLeft = roundTime - sw.getElapsedTimeSecs();

		stage = new Stage();

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"),
				new TextureAtlas("ui/atlas.pack"));


		// creating background
		Image background = new Image(hmg.atlas.findRegion("background"));
		background.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		stage.addActor(background);
		//stage.addActor(table);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());


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
	public boolean keyDown(int keycode) 
	{
		if (keycode == Input.Keys.SPACE) {

			chompSound.play();

			for (Fruit f : fruits) {
				// have to do if fruit check
				// if in mouth
				if (f.x < monkey.x + monkey.animatedSprite.getWidth() && f.x > monkey.x) {
					if (f.isFruit) {
						fruitSpeed += 0.3f;
						fruits.removeValue(f, true);
						points++;
						biteSound.play();

					}

					else {

						fruits.removeValue(f, true);
						hmg.score = points;
						hurtSound.play();
						dispose();
						hmg.message = "You ate a bottle and fainted!";
						((Game) Gdx.app.getApplicationListener())
								.setScreen(new GameOver(hmg));

					}

				}
			}

		}
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
		chompSound.play();

		for (Fruit f : fruits) {
			// have to do if fruit check
			// if in mouth
			if (f.x < monkey.x + monkey.animatedSprite.getWidth() && f.x > monkey.x) {
				if (f.isFruit) {
					fruitSpeed += 0.3f;
					fruits.removeValue(f, true);
					points++;
					biteSound.play();

				}

				else {
					fruits.removeValue(f, true);
					hmg.score = points;
					hurtSound.play();
					dispose();
					hmg.message = "You ate a bottle and fainted!";
					((Game) Gdx.app.getApplicationListener())
							.setScreen(new GameOver(hmg));
				}

			}
		}
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