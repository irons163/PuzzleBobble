package com.irons.puzzlebobble;

import java.util.LinkedList;

import com.example.try_puzzlebobble.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;

public class Bullet {
	public Bitmap bitmap_bullet;
	public double x;
	public double y;
	public static double x1;
	public static double y1;
	public static double x2;
	public static double y2;
	private int speed;
	private int type;

	// 螢幕邊界
	public static final int GAME_AREA_TOP = CommonUtil.screenHeight / 14;
	public static final int GAME_AREA_LEFT = CommonUtil.screenWidth / 13;
	public static final int GAME_AREA_RIGHT = CommonUtil.screenWidth - 2
			* BitmapUtil.bobble.getWidth();
//	public static final int GAME_AREA_BOTTOM = GameView.position[GameView.position.length - 2][0][1] + 48;
	public static float angel;
	public static boolean isshoot = false;
	public static boolean isCollsion = false;
	public DirectionType direction = DirectionType.DIRECTION_STOP;
//	private int[] spid = new int[9];
	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();

	enum BulletType {
		RED_BULLET, GREEN_BULLET, BLUE_BULLET, YELLOW_BULLET, BLACK_BULLET, WHITE_BULLET, ORANGE_BULLET, PURPLE_BULLET
	}
	
	enum DirectionType {
		DIRECTION_LEFT, DIRECTION_RIGHT, DIRECTION_UP, DIRECTION_STOP, DIRECTION_PREPARE
	}

	public Bullet(Bitmap bitmap_bullet, int type) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.bitmap_bullet = bitmap_bullet;
		speed = BitmapUtil.bobble.getWidth() / 10 * 2;
		x = CommonUtil.screenWidth / 2 - BitmapUtil.bobble.getWidth() / 2;
		y = CommonUtil.screenHeight - Shooter.shooterH / 2
				- BitmapUtil.bobble.getHeight();
		x1 = x;
		y1 = y;
		x2 = x;
		y2 = CommonUtil.screenHeight - Shooter.shooterH / 2 + 10;
		bullets = GameView.bullets;
//		spid = GameView.spid;
	}

	public void onDraw(Canvas canvas) {
		Paint paint = null;
		if (isshoot == true) {
			canvas.drawBitmap(bullets.get(0).bitmap_bullet, (int) x, (int) y,
					paint);
			canvas.drawBitmap(bullets.get(1).bitmap_bullet, (int) x1, (int) y1,
					paint);
			canvas.drawBitmap(bullets.get(2).bitmap_bullet, (int) x2, (int) y2,
					paint);
		} else if (isshoot == false) {
			canvas.drawBitmap(bullets.get(0).bitmap_bullet, (int) x1, (int) y1,
					paint);
			canvas.drawBitmap(bullets.get(1).bitmap_bullet, (int) x2, (int) y2,
					paint);
		}
	}

	public void onShoot() {
		if (isshoot == false) {
			angel = Math.abs(Shooter.angel);
			// System.out.println("angel = "+angel);
			if (Shooter.angel >= 0) {
				direction = DirectionType.DIRECTION_RIGHT;
			} else if (Shooter.angel < 0) {
				direction = DirectionType.DIRECTION_LEFT;
			} else {
				direction = DirectionType.DIRECTION_UP;
			}
				isshoot = true;
				AudioUtil.playSound(R.raw.stick);
		}
	}

	public Bitmap getBitmap_bullet() {
		return bitmap_bullet;
	}
//
//	public void setBitmap_bullet(Bitmap bitmap_bullet) {
//		this.bitmap_bullet = bitmap_bullet;
//	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void onMove() {
		if (isshoot == true) {
			// System.out.println("Bullet direction = "+direction);
			switch (direction) {

			case DIRECTION_UP:
				if (y > GAME_AREA_TOP) {
					y -= (int) speed * Math.cos(angel * Math.PI / 180);
				} else
					y = GAME_AREA_TOP;
				break;
			case DIRECTION_RIGHT:

				if (y > GAME_AREA_TOP) {
					x += (int) speed * Math.sin(angel * Math.PI / 180);
					y -= (int) speed * Math.cos(angel * Math.PI / 180);
				} else
					y = GAME_AREA_TOP;
				
				Log.e("x", "x"+x);
				break;

			case DIRECTION_LEFT:

				if (y > GAME_AREA_TOP) {
					x -= speed * Math.sin(angel * Math.PI / 180);
					y -= speed * Math.cos(angel * Math.PI / 180);
				} else
					y = GAME_AREA_TOP;
				
				Log.e("x", "x"+x);
				break;
			default:
				break;

			}
			if (x > GAME_AREA_RIGHT) {
				direction = DirectionType.DIRECTION_LEFT;

				AudioUtil.playSound(R.raw.rebound);
				
			} else if (x < GAME_AREA_LEFT) {
//				x = GAME_AREA_LEFT;
				Log.e("x", "x"+x);
				direction = DirectionType.DIRECTION_RIGHT;
				
				AudioUtil.playSound(R.raw.rebound);
				
			}

			if (y == 38) {
				isshoot = false;
			}
		}
	}
}
