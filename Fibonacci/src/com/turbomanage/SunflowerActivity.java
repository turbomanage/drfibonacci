package com.turbomanage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SunflowerActivity extends Activity implements
		OnSeekBarChangeListener {

	private static final String PERCENT = "percent";
	private static final int DEFAULT_PERCENT = 5;
	private SeekBar slider;
	private SunflowerView sunflowerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sunflower);
		// must call layout first!
		// Thanks
		// http://stackoverflow.com/questions/1691569/findviewbyid-returns-null-for-custom-component-in-layout-xml-not-for-other-co
		sunflowerView = (SunflowerView) this.findViewById(R.id.sunflowerView1);

		slider = (SeekBar) findViewById(R.id.seekBar2);
		slider.setOnSeekBarChangeListener(this);
		// int width = getResources().getDisplayMetrics().widthPixels;
		// slider.setLayoutParams(new LinearLayout.LayoutParams((int) (0.6 *
		// width), LayoutParams.WRAP_CONTENT));
		slider.setMax(100);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		sunflowerView.setPercent(progress);
		// force redraw
		sunflowerView.postInvalidate();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		super.onPause();
		Editor prefs = getPreferences(MODE_PRIVATE).edit();
		prefs.putInt(PERCENT, slider.getProgress());
		prefs.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		int percent = prefs.getInt(PERCENT, DEFAULT_PERCENT);
		slider.setProgress(percent);
	}

}
