package com.turbomanage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.text.NumberFormat;

public class SeriesActivity extends Activity implements OnSeekBarChangeListener {
	/** Called when the activity is first created. */
	private static final int DEFAULT_N = 8;
	private static final int MAX_FIB_LANDSCAPE = 54;
	private static final int MAX_FIB_PORTRAIT = 30;
	private static final String FIBN = "FIBN";
	private long[] fib = new long[MAX_FIB_LANDSCAPE + 1];
	private int n;
	private int maxFib;
	private TextView label;
	private TextView textView;
	private SeekBar slider;
	private TextView sumText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.series);
		initFibs();
		label = (TextView) findViewById(R.id.textView0);
		textView = (TextView) findViewById(R.id.textView1);
		sumText = (TextView) findViewById(R.id.sumEq);
		slider = (SeekBar) findViewById(R.id.seekBar1);
		slider.setOnSeekBarChangeListener(this);
		// init for orientation
		checkOrientation  (getResources().getConfiguration());
	}

	/**
	 * Intentionally wiring into onCreate() and onResume() instead
	 * of just using onConfigurationChanged() because the latter doesn't
	 * fire when the tab is inactive. And adding android:configurationChanges
	 * attribute in the manifest suppresses default behavior. 
	 * 
	 * @param newConfig
	 */
	public void checkOrientation  (Configuration newConfig) {
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			maxFib = MAX_FIB_LANDSCAPE;
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			maxFib = MAX_FIB_PORTRAIT;
			// Uh-oh, shrink the number to fit
			if (n > maxFib) {
				n = maxFib;
			}
		}
		slider.setProgress(n);
		slider.setMax(maxFib);
		updateDisplay();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Editor prefs = getPreferences(MODE_PRIVATE).edit();
		prefs.putInt(FIBN, n);
		prefs.commit();
	}

	// Gets called when this tab selected
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		n = prefs.getInt(FIBN, DEFAULT_N);
		// Check orientation as may have changed while tab inactive
		checkOrientation (getResources().getConfiguration());
	}

	private void updateDisplay() {
		label.setText(" fib(" + n + ") ");
		NumberFormat fmt = NumberFormat.getInstance();
		textView.setText(fmt.format(fib[n]));
		if (n > 1) {
		    sumText.setText(fmt.format(fib[n - 2]) + " + "
					+ fmt.format(fib[n - 1]));
		} else {
			sumText.setText(null);
		}
	}

	private void initFibs() {
		for (int i = 0; i <= MAX_FIB_LANDSCAPE; i++)
			fib[i] = (i < 2) ? i : fib[i - 2] + fib[i - 1];
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		this.n = progress;
		updateDisplay();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Click handler for plus button.
	 * 
	 * @param view
	 */
	public void increment(View view) {
	    int curPosition = slider.getProgress();
	    if (curPosition < maxFib)
	    {
	        slider.setProgress(curPosition + 1);
	    } 
	}
	
	/**
	 * Click handler for minus button.
	 * 
	 * @param view
	 */
	public void decrement(View view) {
	    int curPosition = slider.getProgress();
	    if (curPosition > 0) {
	        slider.setProgress(curPosition - 1);
	    }
    }
	
}