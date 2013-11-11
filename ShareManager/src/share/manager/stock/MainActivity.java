package share.manager.stock;

import share.manager.adapters.MainPagerAdapter;
import share.manager.listeners.BusTabListener;
import share.manager.listeners.SwipeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	@SuppressLint("HandlerLeak")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			System.out.println((String)msg.obj);
		}
	};
	
        
	/*	ConnectionThread dataThread = new ConnectionThread(
				app.yahooChart+ShareUtils.createChartLink(10, 10, 2013, 11, 10, 2013, 'w', "DELL"), threadConnectionHandler, null, this);
		dataThread.start();
	 */
	
	MainPagerAdapter mCentralActivity;
	private ViewPager mViewPager;
	//private ShareManager app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//app = (ShareManager) getApplicationContext();
		tabHandler();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);         
        }
	}
	
	public void tabHandler() {
		mCentralActivity = new MainPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.MainPager);
		mViewPager.setAdapter(mCentralActivity);
		mViewPager.setOnPageChangeListener(new SwipeListener(mViewPager, MainActivity.this));
		//app.setAppViewPager(mViewPager);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new BusTabListener(mViewPager);

		actionBar.addTab(actionBar.newTab().setText("Portfolio")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("My Shares")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Overall Shares")
				.setTabListener(tabListener));
	}
    
}
