package com.me.HungryMonkey;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class HungryMonkey extends Game {
	private OrthographicCamera camera;
	SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	public TextureAtlas atlas;
	public int currentLevel = 0;
	public int points = 0;
	public float accuracy = 0;

	public Music currentMusic;
	public Sound select;
	public int score;
	public String message = "test";
	
	BitmapFont font;
	
	public TitleScreen title;
	public PlayScreen play;
	public GameOver round;
	public HowtoScreen howto;
	
	public static final String TITLE = "Hungry Monkey", VERSION = "jammywammy";
	Rectangle viewport;

	public void create() 
	{

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		atlas = new TextureAtlas("Art/Atlas.txt");

		// use libgdx's default Arial font.
		font = new BitmapFont();

		title = new TitleScreen(this);
		select = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/select.wav"));
		
		this.setScreen(title);

	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		currentMusic.dispose();
	}

	public void render() {
		super.render();
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
