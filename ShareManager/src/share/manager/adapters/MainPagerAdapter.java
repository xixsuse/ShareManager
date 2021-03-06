
package share.manager.adapters;

import java.util.ArrayList;

import share.manager.fragments.*;
import share.manager.stock.R;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

	private int nSwipes = 3;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;

		fragment = i == 0 ? new MineFragment() : (i == 1 ? new SharesFragment()
				: new PortfolioFragment());

		fragments.add(fragment);
		return fragment;
	}

	@Override
	public int getCount() {
		return nSwipes;
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		LayoutInflater inflater = (LayoutInflater) collection.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		int resId = 0;
		switch (position) {
			case 0:
				resId = R.layout.fragment_mine;
				break;
			case 1:
				resId = R.layout.fragment_shares;
				break;
			case 2:
				resId = R.layout.fragment_portfolio;
				break;
		}

		View view = inflater.inflate(resId, null);
		((ViewPager) collection).addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		fragments.remove(position);
		((ViewPager) collection).removeViewAt(position);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}
