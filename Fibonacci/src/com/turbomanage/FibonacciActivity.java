package com.turbomanage;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class FibonacciActivity extends TabActivity {

	public static final String SELECTED_TAB = "selectedTab";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SeriesActivity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("series").setIndicator("Series",
                          res.getDrawable(R.drawable.ic_tab_series))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, SunflowerActivity.class);
        spec = tabHost.newTabSpec("sunflower").setIndicator("Sunflower",
                          res.getDrawable(R.drawable.ic_tab_sunflower))
                      .setContent(intent);
        tabHost.addTab(spec);
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	Editor prefs = getPreferences(MODE_PRIVATE).edit();
    	int currentTab = getTabHost().getCurrentTab();
    	prefs.putInt(SELECTED_TAB, currentTab);
    	prefs.commit();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	int selectedTab = prefs.getInt(SELECTED_TAB, 0);
    	getTabHost().setCurrentTab(selectedTab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}