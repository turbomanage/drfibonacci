package com.turbomanage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SunflowerView extends View {

	public static final int ORANGE = 0xfff87306;
	public static final Paint ORANGE_FILL = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final int SEED_RADIUS = 5;
	private static final int SCALE_FACTOR = 7;
	public static final double TAU = Math.PI * 2;
	public static final double PHI = (Math.sqrt(5) + 1) / 2;
	private int percent, maxSeeds;
	private int height;
	private int width;
	private int maxR;
	private int xc;
	private int yc;

	public SunflowerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ORANGE_FILL.setColor(0xFFF87306);
		ORANGE_FILL.setStyle(Style.FILL_AND_STROKE);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		onLayoutChange();
	}
	
	/**
	 * Draw the complete figure for the current number of seeds
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int numSeeds = maxSeeds * this.percent / 100;
		for (int i = 0; i < numSeeds ; i++) {
			double theta = i * TAU / PHI;
			double r = Math.sqrt(i) * SCALE_FACTOR;
			int x = (int) Math.round(xc + r * Math.cos(theta));
			int y = (int) Math.round(yc - r * Math.sin(theta));
			drawSeed(x, y, canvas);
		}
	}

	void onLayoutChange() {
		height = this.getHeight();
		width = this.getWidth();
		maxR = Math.min(height, width) / 2;
		int range = (maxR - SEED_RADIUS) / SCALE_FACTOR;
		maxSeeds = range * range;
		xc = width / 2;
		yc = height / 2;
	}

	void setPercent(int percent) {
		this.percent = percent;
	}

	/**
	 * Draw a small circle representing a seed centered at (x,y)
	 * 
	 * @param x
	 *            Center of the seed head
	 * @param y
	 *            Center of the seed head
	 * @param canvas 
	 */
	private void drawSeed(int x, int y, Canvas canvas) {
		int sr = SEED_RADIUS;
		canvas.drawArc(new RectF(x-sr,y-sr,x+sr,y+sr), 0, 360, false, ORANGE_FILL);
	}

}