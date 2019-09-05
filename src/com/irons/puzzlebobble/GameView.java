package com.irons.puzzlebobble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.example.try_puzzlebobble.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private boolean isGameRun = true;
	private Shooter shooter;
	private SurfaceHolder surfaceHolder;

	public static Bullet bullet;
	// public List<Bobble> list = new ArrayList<Bobble>();
	public static LinkedList<Bullet> bullets;
	public static Bitmap bitmap_onebullet = BitmapUtil.bobble;
	public static Bitmap[] normal_bobble;
	public static Bitmap[] frozen_bobble;
	public static Bitmap[] blind_bobble;
	public static Bitmap bitmap_win;
	public static Bitmap bitmap_loss;
	public static Bobble[][] bobbles;
	private Random random;
	public static boolean soundFlag = true;
	private int firstX, firstY;
	public static int bobbleTotalLinesCount;
	public static int[][][] position;
	private static final int CLEAR_BUBBLE_NUMBER = 3;
	private GameStateType gameStateType = GameStateType.None;

	// private boolean flag;

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		bullets = new LinkedList<Bullet>();
		normal_bobble = new Bitmap[8];
		frozen_bobble = new Bitmap[8];
		blind_bobble = new Bitmap[8];
		random = new Random();

		initBobbleBitmapArray();
		initBullet();

		bitmap_loss = BitmapFactory.decodeResource(getResources(),
				R.drawable.lose_panel);
		bitmap_win = BitmapFactory.decodeResource(getResources(),
				R.drawable.win_panel);

		double showBobblePanelPresent = 3 / 4.0;
		double showBobblePanelHeight = CommonUtil.screenHeight
				* showBobblePanelPresent;
		int bobbleDistanceX = BitmapUtil.bobble.getWidth();
		int bobbleDistanceY = BitmapUtil.bobble.getHeight() * 5 / 6;
		int bobbleOffsetX = bobbleDistanceX / 2;

		for (int bobbleLine = 0;; bobbleLine++) {
			if (showBobblePanelHeight < Bullet.GAME_AREA_TOP + bobbleLine
					* bobbleDistanceY) {
				bobbleTotalLinesCount = bobbleLine - 1;
				Log.e("bobbleTotalLinesCount", bobbleTotalLinesCount + "");
				break;
			}
		}

		firstX = CommonUtil.screenWidth / 16;
		firstY = Bullet.GAME_AREA_TOP;
		position = new int[bobbleTotalLinesCount][8][8];
		for (int i = 0; i < position.length; i++) {
			for (int j = 0; j < position[0].length; j++) {
				if (i % 2 == 0) {
					position[i][j][0] = firstX + j * bobbleDistanceX;
					position[i][j][1] = firstY + i * bobbleDistanceY;
				} else if (i % 2 == 1) {
					position[i][j][0] = firstX + j * bobbleDistanceX
							+ bobbleOffsetX;
					position[i][j][1] = firstY + i * bobbleDistanceY;
				}
				// System.out.println("position["+i+"]["+j+"]"+position[i][j][0]+","+position[i][j][1]);
			}
		}

		bobbles = new Bobble[position.length][position[0].length];

		for (int i = 0; i < GameMap.map.length; i++) {
			for (int j = 0; j < GameMap.map[i].length; j++) {
				int type = GameMap.map[i][j];
				bobbles[i][j] = new Bobble(newBobbleBitmap(type), type,
						new Point(position[i][j][0], position[i][j][1]));
			}
		}

		shooter = new Shooter();
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float touchX = event.getX();
		float touchY = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			shooter.desginAngelByTouchXY(touchX, touchY);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			bullet.onShoot();
		}
		return true;
	}

	private void move() {
		bullet.onMove();
		Point pointBulletXY = new Point((int) bullet.x, (int) bullet.y);
		boolean isCollision = false;
		exit: for (int i = 0; i < bobbles.length; i++) {
			for (int j = 0; j < bobbles[0].length; j++) {
				if (bobbles[i][j] != null) {

					if (bobbles[i][j].isNeedClear()) {
						bobbles[i][j] = null;
					} else {
						Point pointBobbleXY = bobbles[i][j].getPoint();
						CollisionType collisionType = isCollisionDetected(
								pointBulletXY, pointBobbleXY,
								BitmapUtil.bobble.getHeight() / 2);
						int type = bullets.get(0).getType();
						switch (collisionType) {
						case Right:
							createNewBubbleAndClearDetectedAfterCollision(i,
									j + 1, type);
							isCollision = true;
							break exit;
						case RinghtTop:
							createNewBubbleAndClearDetectedAfterCollision(
									i - 1, j + i % 2, type);
							isCollision = true;
							break exit;
						case LeftTop:
							createNewBubbleAndClearDetectedAfterCollision(
									i - 1, j - 1 + i % 2, type);
							isCollision = true;
							break exit;
						case Left:
							createNewBubbleAndClearDetectedAfterCollision(i,
									j - 1, type);
							isCollision = true;
							break exit;
						case LeftBottom:
							createNewBubbleAndClearDetectedAfterCollision(
									i + 1, j - 1 + i % 2, type);
							isCollision = true;
							break exit;
						case RinghtBottom:
							createNewBubbleAndClearDetectedAfterCollision(
									i + 1, j + i % 2, type);
							isCollision = true;
							break exit;
						case None:
							break;
						}
					}
				}
			}
		}
		if (isCollision) {
			// Bullet.isshoot = false;
			bullets.remove(0);
			bullet = bullets.getFirst();
			bullets.add(newBullet(random.nextInt(100) % 8));

			gameFinishDetectedAndSetIsGameRunFlag();
			Bullet.isshoot = !isGameRun;
		}
		
		for (Bobble bobble : removeBubbleAnimationArrayList){
			bobble.scatteredMove();
		}
	}

	Bobble newAddBubbleAfterCollision;
	ArrayList<Bobble> removeBubbleAnimationArrayList = new ArrayList<Bobble>();
	
	private void createNewBubbleAndClearDetectedAfterCollision(
			int newBobbleLine, int newBobblePosition, int type) {
		removeBubbleAnimationArrayList.clear();
		newAddBubbleAfterCollision = new Bobble(
				newBobbleBitmap(type), type, new Point(
						position[newBobbleLine][newBobblePosition][0],
						position[newBobbleLine][newBobblePosition][1]));
		bobbles[newBobbleLine][newBobblePosition] = newAddBubbleAfterCollision;

		HashSet<Bobble> bubbleClearHashSet = new HashSet<Bobble>();
		clearBubbleDetected(newBobbleLine, newBobblePosition, type,
				bubbleClearHashSet);
		if (bubbleClearHashSet.size() >= CLEAR_BUBBLE_NUMBER) {
			Iterator<Bobble> iterator = bubbleClearHashSet.iterator();
			while (iterator.hasNext()) {
				Bobble clearBubble = iterator.next();
				if(clearBubble.getPoint().x < newAddBubbleAfterCollision.getPoint().x){
					clearBubble.setScatteredDirectionType(Bobble.ScatteredDirectionType.DIRECTION_RIGHT);
				}else{
					clearBubble.setScatteredDirectionType(Bobble.ScatteredDirectionType.DIRECTION_LEFT);
				}
				removeBubbleAnimationArrayList.add(clearBubble);
				clearBubble.clear();
			}
			// Bobble[] bobbless = new Bobble[bubbleClearHashSet.size()];
			// bobbless = bubbleClearHashSet.toArray(bobbless);
			// for(int i=0; i< bobbless.length; i++){
			// bobbless[i].clear();
			// // bobble = null;
			// }
			for (int bubblePosition = 0; bubblePosition < bobbles[0].length; bubblePosition++) {
				if (bobbles[0][bubblePosition] != null) {
					detectBubbleWithLinkToTheOther(0, bubblePosition);
				}
			}
			
			for (int i = bobbles.length - 1; i >= 0; i--) {
				for (int j = bobbles[0].length - 1; j >= 0; j--) {
					if (bobbles[i][j] != null && !bobbles[i][j].isNeedClear()) {
						if(bobbles[i][j].isCheckedForLeft())
							bobbles[i][j].setIsCheckedForLeft(false);
						else{
							bobbles[i][j].setScatteredDirectionType(Bobble.ScatteredDirectionType.DIRECTION_DOWN);
							removeBubbleAnimationArrayList.add(bobbles[i][j]);
							bobbles[i][j] = null;
						}		
					}
				}
			}
		}
	}

	private void clearBubbleDetected(int bubbleLineForDetected,
			int bubblePositionForDetected, int type,
			HashSet<Bobble> bubbleClearHashSet) {
		if ((bubbleLineForDetected >= 0 && bubbleLineForDetected < bobbles.length)
				&& (bubblePositionForDetected >= 0 && bubblePositionForDetected < 8)) {
			Bobble bubbleForDetected = bobbles[bubbleLineForDetected][bubblePositionForDetected];
			if (bubbleForDetected != null) {
				if (bubbleClearHashSet.size() == 0
						|| type == bubbleForDetected.getType()) {
					if (!isClearBubbleExistInHashSet(bubbleForDetected,
							bubbleClearHashSet)) {
						bubbleClearHashSet.add(bubbleForDetected);
						int lastBubbleLine = bubbleLineForDetected;
						int lastBubblePosition = bubblePositionForDetected;
						rightBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						rightTopBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						leftTopBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						leftBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						leftBottomBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
						rightBottomBubbleForDetected(lastBubbleLine,
								lastBubblePosition, type, bubbleClearHashSet);
					}
				}
			}
		}

		// if (i<0)
		// return true;
		// else if (j < 0 || j > bobbles[i].length)
		// return false;
		// else{
		// boolean hasLinkTopLeftBubble = hasLinkTopBubble(i - 1, j - 1 + i %
		// 2);
		// boolean hasLinkTopRightBubble = hasLinkTopBubble(i - 1, j + i % 2);
		// return (hasLinkTopLeftBubble || hasLinkTopRightBubble);
		// }
	}

	private boolean isClearBubbleExistInHashSet(Bobble bubbleForDetected,
			HashSet<Bobble> bubbleClearHashSet) {
		boolean isClearBubbleExistInHashSet = false;
		for (Bobble bobble : bubbleClearHashSet) {
			isClearBubbleExistInHashSet = bobble.equals(bubbleForDetected);
			if (isClearBubbleExistInHashSet)
				break;
		}
		return isClearBubbleExistInHashSet;
	}

	private void rightBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		clearBubbleDetected(lastBubbleLine, lastBubblePosition + 1, type,
				bubbleClearHashSet);
	}

	private void rightTopBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		clearBubbleDetected(lastBubbleLine - 1, lastBubblePosition
				+ lastBubbleLine % 2, type, bubbleClearHashSet);
	}

	private void leftTopBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		clearBubbleDetected(lastBubbleLine - 1, lastBubblePosition - 1
				+ lastBubbleLine % 2, type, bubbleClearHashSet);
	}

	private void leftBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		clearBubbleDetected(lastBubbleLine, lastBubblePosition - 1, type,
				bubbleClearHashSet);
	}

	private void leftBottomBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		clearBubbleDetected(lastBubbleLine + 1, lastBubblePosition - 1
				+ lastBubbleLine % 2, type, bubbleClearHashSet);
	}

	private void rightBottomBubbleForDetected(int lastBubbleLine,
			int lastBubblePosition, int type, HashSet<Bobble> bubbleClearHashSet) {
		clearBubbleDetected(lastBubbleLine + 1, lastBubblePosition
				+ lastBubbleLine % 2, type, bubbleClearHashSet);
	}

	private void detectBubbleWithLinkToTheOther(int i, int j) {
		if (i < 0)
			return;
		else if (j < 0 || j > bobbles[i].length - 1)
			return;
		else if (bobbles[i][j] == null || bobbles[i][j].isNeedClear())
			return;
		else if (bobbles[i][j].isCheckedForLeft())
			return;
		else {
			bobbles[i][j].setIsCheckedForLeft(true);
			detectBubbleWithLinkToTheOther(i, j + 1);
			detectBubbleWithLinkToTheOther(i - 1, j - 1 + i % 2);
			detectBubbleWithLinkToTheOther(i - 1, j + i % 2);
			detectBubbleWithLinkToTheOther(i, j - 1);
			detectBubbleWithLinkToTheOther(i + 1, j - 1 + i % 2);
			detectBubbleWithLinkToTheOther(i + 1, j + i % 2);
		}
	}

	enum GameStateType {
		None, Lose, Win
	}

	private void gameFinishDetectedAndSetIsGameRunFlag() {
		gameFinishDetected();
		isGameFinish();
	}

	private void gameFinishDetected() {
		if (isLoss()) {
			gameStateType = GameStateType.Lose;
		} else if (isWin()) {
			gameStateType = GameStateType.Win;
		}
	}

	private void isGameFinish() {
		if (gameStateType == GameStateType.Lose
				|| gameStateType == GameStateType.Win) {
			isGameRun = false;
		}
	}

	@SuppressLint("WrongCall")
	private void draw() {
		Canvas canvas = surfaceHolder.lockCanvas();
		canvas.drawColor(Color.WHITE);

		Paint paint = null;

		drawBackGround(canvas);
		bullet.onDraw(canvas);
		shooter.onDraw(canvas);

		for (int i = 0; i < bobbles.length; i++) {
			for (int j = 0; j < bobbles[0].length; j++) {
				if (bobbles[i][j] != null) {
					bobbles[i][j].MyDraw(canvas, paint);
				}
			}
		}
		
		for (Bobble bobble : removeBubbleAnimationArrayList){
			bobble.MyDraw(canvas, paint);
		}

		if (gameStateType == GameStateType.Lose) {
			if (soundFlag) {
				AudioUtil.playSound(R.raw.lose);
				AudioUtil.playSound(R.raw.noh);
			}
			for (int i = 0; i < bobbles.length; i++) {
				for (int j = 0; j < bobbles[0].length; j++) {
					if (bobbles[i][j] != null) {
						bobbles[i][j]
								.setBobbleBitmap(frozen_bobble[bobbles[i][j]
										.getType()]);
						bobbles[i][j].MyDraw(canvas, paint);
					}
				}
			}

			canvas.drawBitmap(bitmap_loss, 0, CommonUtil.screenHeight / 2
					- bitmap_loss.getHeight() / 2, paint);
		} else if (gameStateType == GameStateType.Win) {
			if (soundFlag) {
				AudioUtil.playSound(R.raw.applause);
			}
			canvas.drawBitmap(bitmap_win, 0, CommonUtil.screenHeight / 2
					- bitmap_win.getHeight() / 2, paint);
		}

		surfaceHolder.unlockCanvasAndPost(canvas);

	}

	private void drawBackGround(Canvas canvas) {
		Rect src = new Rect(0, 0, BitmapUtil.gameBg.getWidth(),
				BitmapUtil.gameBg.getHeight());
		Rect dst = new Rect(0, 0, CommonUtil.screenWidth,
				CommonUtil.screenHeight);
		canvas.drawBitmap(BitmapUtil.gameBg, src, dst, null);
	}

	Thread gameThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isGameRun) {
				move();
				draw();
			}
		}
	});

	private void initBobbleBitmapArray() {
		normal_bobble[0] = BitmapUtil.bobble;
		normal_bobble[1] = BitmapUtil.bobble2;
		normal_bobble[2] = BitmapUtil.bobble3;
		normal_bobble[3] = BitmapUtil.bobble4;
		normal_bobble[4] = BitmapUtil.bobble5;
		normal_bobble[5] = BitmapUtil.bobble6;
		normal_bobble[6] = BitmapUtil.bobble7;
		normal_bobble[7] = BitmapUtil.bobble8;

		frozen_bobble[0] = BitmapUtil.frozenBobble;
		frozen_bobble[1] = BitmapUtil.frozenBobble2;
		frozen_bobble[2] = BitmapUtil.frozenBobble3;
		frozen_bobble[3] = BitmapUtil.frozenBobble4;
		frozen_bobble[4] = BitmapUtil.frozenBobble5;
		frozen_bobble[5] = BitmapUtil.frozenBobble6;
		frozen_bobble[6] = BitmapUtil.frozenBobble7;
		frozen_bobble[7] = BitmapUtil.frozenBobble8;

		blind_bobble[0] = BitmapUtil.blindBobble;
		blind_bobble[1] = BitmapUtil.blindBobble2;
		blind_bobble[2] = BitmapUtil.blindBobble3;
		blind_bobble[3] = BitmapUtil.blindBobble4;
		blind_bobble[4] = BitmapUtil.blindBobble5;
		blind_bobble[5] = BitmapUtil.blindBobble6;
		blind_bobble[6] = BitmapUtil.blindBobble7;
		blind_bobble[7] = BitmapUtil.blindBobble8;
	}

	private void initBullet() {
		int type1 = random.nextInt(100) % 8;
		bullets.add(newBullet(type1));
		int type2 = random.nextInt(100) % 8;
		bullets.add(newBullet(type2));
		int type3 = random.nextInt(100) % 8;
		bullets.add(newBullet(type3));

		System.out.println("bullet = " + bullet.getType());
		System.out.println(type1 + "---" + type2 + "---" + type3);
		for (int i = 0; i < bullets.size(); i++) {
			System.out
					.println("bullets[" + i + "]=" + bullets.get(i).getType());
		}
	}

	public Bullet newBullet(int type) {
		if (CommonUtil.isBlind == false) {
			bullet = new Bullet(newBobbleBitmap(type), type);
		} else {
			bullet = new Bullet(newBobbleBitmap(type), type);
		}
		return bullet;
	}

	public Bitmap newBobbleBitmap(int type) {
		Bitmap newBobbleBitmap;
		if (CommonUtil.isBlind == false) {
			newBobbleBitmap = normal_bobble[type];
		} else {
			newBobbleBitmap = blind_bobble[type];
		}
		return newBobbleBitmap;
	}

	private boolean isLoss() {
		boolean isLoss = false;
		for (int j = 0; j < bobbles[0].length; j++) {
			if (bobbles[position.length - 1][j] != null) {
				isLoss = true;
				break;
			}
		}
		return isLoss;
	}

	private boolean isWin() {
		boolean isWin = true;
		// for (int i = 0; i < bobbles.length; i++) {
		// for (int j = 0; j < bobbles[0].length; j++) {
		// if (bobbles[i][j] != null) {
		// isWin = false;
		// break;
		// } else
		// isWin = true;
		// }
		// if (isWin == false) {
		// break;
		// }
		// }
		// for (int i = 0; i < bobbles.length; i++) {
		for (int j = 0; j < bobbles[0].length; j++) {
			if (bobbles[0][j] == null) {
				isWin = true;
				break;
			} else
				isWin = false;
		}
		// if (isWin == false) {
		// break;
		// }
		// }
		return isWin;
	}

	public enum CollisionType { // 碰撞情形以 enum 宣告
		None, Left, Right, LeftTop, RinghtTop, LeftBottom, RinghtBottom
	}

	private CollisionType isCollisionDetected(Point bulletXY, Point bobbleXY,
			float fR) {
		int bulletX = bulletXY.x;
		int bulletY = bulletXY.y;
		int bobbleX = bobbleXY.x;
		int bobbleY = bobbleXY.y;
		double distancePOW = Math.pow(bulletX - bobbleX, 2)
				+ Math.pow(bulletY - bobbleY, 2);
		float distance = (float) Math.sqrt(distancePOW);
		if (distance <= fR * 2) {

			float hitAngle = ((float) ((Math.atan2((-1) * (bulletY - bobbleY),
					(bulletX - bobbleX))) / Math.PI * 180));
			if (hitAngle < 0) {
				hitAngle = 360 + hitAngle;
			}

			if (hitAngle <= 30 || hitAngle > 330) {
				return CollisionType.Right;
			} else if (hitAngle <= 90 && hitAngle > 30) {
				return CollisionType.RinghtTop;
			} else if (hitAngle <= 150 && hitAngle > 90) {
				return CollisionType.LeftTop;
			} else if (hitAngle <= 210 && hitAngle > 150) {
				return CollisionType.Left;
			} else if (hitAngle <= 270 && hitAngle > 210) {
				return CollisionType.LeftBottom;
			} else if (hitAngle <= 330 && hitAngle > 270) {
				return CollisionType.RinghtBottom;
			}
		}
		return CollisionType.None;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
