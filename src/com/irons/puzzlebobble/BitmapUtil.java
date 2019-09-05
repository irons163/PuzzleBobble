package com.irons.puzzlebobble;

import com.example.try_puzzlebobble.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtil {
	static Context context;
	
	public static Bitmap shooter;
	public static Bitmap gameBg;
	public static Bitmap bobble;
	public static Bitmap bobble2;
	public static Bitmap bobble3;
	public static Bitmap bobble4;
	public static Bitmap bobble5;
	public static Bitmap bobble6;
	public static Bitmap bobble7;
	public static Bitmap bobble8;
	public static Bitmap frozenBobble;
	public static Bitmap frozenBobble2;
	public static Bitmap frozenBobble3;
	public static Bitmap frozenBobble4;
	public static Bitmap frozenBobble5;
	public static Bitmap frozenBobble6;
	public static Bitmap frozenBobble7;
	public static Bitmap frozenBobble8;
	public static Bitmap blindBobble;
	public static Bitmap blindBobble2;
	public static Bitmap blindBobble3;
	public static Bitmap blindBobble4;
	public static Bitmap blindBobble5;
	public static Bitmap blindBobble6;
	public static Bitmap blindBobble7;
	public static Bitmap blindBobble8;
	
	public static void initBitmap(Context context){
		BitmapUtil.context = context;
		initBitmap();
	}
	
	private static void initBitmap(){
		shooter = BitmapFactory.decodeResource(context.getResources(), R.drawable.shooter, null);
		gameBg = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.background);
		gameBg = Bitmap.createBitmap(gameBg, gameBg.getWidth()/4, 0, gameBg.getWidth()/2, gameBg.getHeight());
		bobble = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_1);
		bobble2 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_2);
		bobble3 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_3);
		bobble4 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_4);
		bobble5 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_5);
		bobble6 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_6);
		bobble7 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_7);
		bobble8 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.bubble_8);
		
		int bobbleWidth = (int) ((CommonUtil.screenWidth - CommonUtil.screenWidth / 10.0)/9);
		
		bobble = Bitmap.createScaledBitmap(bobble, bobbleWidth,  bobbleWidth, false);
		bobble2 = Bitmap.createScaledBitmap(bobble2, bobbleWidth,  bobbleWidth, false);
		bobble3 = Bitmap.createScaledBitmap(bobble3, bobbleWidth,  bobbleWidth, false);
		bobble4 = Bitmap.createScaledBitmap(bobble4, bobbleWidth,  bobbleWidth, false);
		bobble5 = Bitmap.createScaledBitmap(bobble5, bobbleWidth,  bobbleWidth, false);
		bobble6 = Bitmap.createScaledBitmap(bobble6, bobbleWidth,  bobbleWidth, false);
		bobble7 = Bitmap.createScaledBitmap(bobble7, bobbleWidth,  bobbleWidth, false);
		bobble8 = Bitmap.createScaledBitmap(bobble8, bobbleWidth,  bobbleWidth, false);
		
		frozenBobble = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_1);
		frozenBobble2 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_2);
		frozenBobble3 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_3);
		frozenBobble4 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_4);
		frozenBobble5 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_5);
		frozenBobble6 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_6);
		frozenBobble7 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_7);
		frozenBobble8 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.frozen_8);
		
		int frozenBobbleHeight = (int) ((float)frozenBobble.getHeight()
				/ frozenBobble.getWidth() * bobbleWidth);
		
		frozenBobble = Bitmap.createScaledBitmap(frozenBobble, bobbleWidth,  frozenBobbleHeight, false);
		frozenBobble2 = Bitmap.createScaledBitmap(frozenBobble2, bobbleWidth,  frozenBobbleHeight, false);
		frozenBobble3 = Bitmap.createScaledBitmap(frozenBobble3, bobbleWidth,  frozenBobbleHeight, false);
		frozenBobble4 = Bitmap.createScaledBitmap(frozenBobble4, bobbleWidth,  frozenBobbleHeight, false);
		frozenBobble5 = Bitmap.createScaledBitmap(frozenBobble5, bobbleWidth,  frozenBobbleHeight, false);
		frozenBobble6 = Bitmap.createScaledBitmap(frozenBobble6, bobbleWidth,  frozenBobbleHeight, false);
		frozenBobble7 = Bitmap.createScaledBitmap(frozenBobble7, bobbleWidth,  frozenBobbleHeight, false);
		frozenBobble8 = Bitmap.createScaledBitmap(frozenBobble8, bobbleWidth,  frozenBobbleHeight, false);
		
		blindBobble = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_1);
		blindBobble2 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_2);
		blindBobble3 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_3);
		blindBobble4 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_4);
		blindBobble5 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_5);
		blindBobble6 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_6);
		blindBobble7 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_7);
		blindBobble8 = BitmapFactory.decodeResource(context.getResources(), 
				R.drawable.blind_8);
		
		blindBobble = Bitmap.createScaledBitmap(blindBobble, bobbleWidth,  bobbleWidth, false);
		blindBobble2 = Bitmap.createScaledBitmap(blindBobble2, bobbleWidth,  bobbleWidth, false);
		blindBobble3 = Bitmap.createScaledBitmap(blindBobble3, bobbleWidth,  bobbleWidth, false);
		blindBobble4 = Bitmap.createScaledBitmap(blindBobble4, bobbleWidth,  bobbleWidth, false);
		blindBobble5 = Bitmap.createScaledBitmap(blindBobble5, bobbleWidth,  bobbleWidth, false);
		blindBobble6 = Bitmap.createScaledBitmap(blindBobble6, bobbleWidth,  bobbleWidth, false);
		blindBobble7 = Bitmap.createScaledBitmap(blindBobble7, bobbleWidth,  bobbleWidth, false);
		blindBobble8 = Bitmap.createScaledBitmap(blindBobble8, bobbleWidth,  bobbleWidth, false);
		
//		Matrix matrix = new Matrix();
//		  matrix.postScale(1.5f,1.5f);
		
//		bobble = Bitmap.createBitmap(bobble, 0,0,(CommonUtil.screenWidth - CommonUtil.screenWidth / 13)/5,  (CommonUtil.screenWidth - CommonUtil.screenWidth / 13)/5);
//		  bobble = Bitmap.createBitmap(bobble, 0,0, bobble.getWidth(), bobble.getHeight(), matrix, true);
	}
	
}
