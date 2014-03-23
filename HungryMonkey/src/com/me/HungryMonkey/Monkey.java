package com.me.HungryMonkey;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Monkey 
{
	TextureRegion region;
	Animation animation;
	AnimatedSprite animatedSprite;
	
	float x = 300;
	float y = 164;

	public Monkey(TextureAtlas atlas, float startX, float startY)
	{
		//hero
		region = atlas.findRegion("monkey");
		TextureRegion[][] heroTR = region.split(200, 100);
		animation = new Animation(0.1f, heroTR[0]);
		animation.setPlayMode(Animation.LOOP_PINGPONG);
		animatedSprite = new AnimatedSprite(animation);
		x = startX;
		y = startY;
		
		
	}

	public void reset(Point startCorner) 
	{
		x = startCorner.x;
		y = startCorner.y;
		
	}

}
