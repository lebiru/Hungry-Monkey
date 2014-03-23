package com.me.HungryMonkey;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.HungryMonkey.AnimatedSprite;

public class Fruit {
	Random ran = new Random();
	TextureRegion fruitRegion;
	Animation fruitAnimation;
	AnimatedSprite fruitAnimatedSprite;
	float x = 0;
	float y = 0;
	float dx = 1;
	private float speed = 5;
	boolean isFruit;

	public Fruit(TextureAtlas atlas, float fruitSpeed, float startX) {
		int fruitSwitch = ran.nextInt(4);
		switch (fruitSwitch) {
		case 0:
			fruitRegion = atlas.findRegion("fruit1");
			isFruit = true;
			break;
		case 1:
			fruitRegion = atlas.findRegion("fruit2");
			isFruit = false;
			break;
		case 2:
			fruitRegion = atlas.findRegion("fruit3");
			isFruit = true;
			break;
		case 3:
			fruitRegion = atlas.findRegion("fruit4");
			isFruit = true;
			break;
		}
		TextureRegion[][] fruitTR = fruitRegion.split(50, 50);
		fruitAnimation = new Animation(0.25f, fruitTR[0]);
		fruitAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		fruitAnimatedSprite = new AnimatedSprite(fruitAnimation);
		x = startX;
		y = 150;
		speed = fruitSpeed;

	}

	public void moveFruit() {
		x -= (dx * speed);
		fruitAnimatedSprite.setPosition(x, y);
	}

	public float betweenTwo(float min, float max) {
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}