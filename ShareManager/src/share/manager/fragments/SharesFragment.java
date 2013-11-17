package share.manager.fragments;

import java.util.ArrayList;

import share.manager.adapters.CompanyAdapter;
import share.manager.connection.ConnectionThread;
import share.manager.stock.CompanyActivity;
import share.manager.stock.R;
import share.manager.stock.ShareManager;
import share.manager.utils.FileHandler;
import share.manager.utils.RESTFunction;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SharesFragment extends Fragment {

	private View rootView;
	private ShareManager app;
	private RESTFunction currentFunction;
	private ProgressDialog pDiag;
	private boolean resuming = false;
	
	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unchecked")
	private Handler threadConnectionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(currentFunction) {
				case GET_COMPANY_STOCK:
					buildList((ArrayList<String>) msg.obj);
					if(pDiag != null) pDiag.dismiss();
					break;
				default:
					break;
			}		
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		rootView = inflater.inflate(R.layout.fragment_shares, container, false);
		app = (ShareManager) getActivity().getApplication();
		startQuotas();
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(resuming) {
			refresh();
			resuming = !resuming;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		resuming = true;
	}
	
	public void refresh() {
		pDiag = ProgressDialog.show(getActivity(), "",
				"Fetching results...", true);
		startQuotas();
	}
	
	public void startQuotas() {
		currentFunction = RESTFunction.GET_COMPANY_STOCK;
		String link = app.yahooQuote;
		
		ArrayList<String> info = FileHandler.readFile();
		
		if(info.size() > 0) {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_shares);
			if(frame.findViewById(0xfefefefe) != null) {
				frame.removeView(frame.findViewById(0xfefefefe));
			}
			
			for(String s : info)
				link += s.split("\\|")[1] + "+";
			
			link = link.substring(0, link.length()-1);
			
			ConnectionThread dataThread = new ConnectionThread(
					link, threadConnectionHandler, getActivity());
			dataThread.start();
		}
		else {
			FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.frame_shares);
			if(frame.findViewById(0xfefefefe) != null) {
				TextView text = (TextView) frame.findViewById(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
			}
			else {
				TextView text = new TextView(getActivity());
				text.setId(0xfefefefe);
				text.setText("Please subscribe to a company in order to see its stock evolution!");
				text.setTextColor(Color.WHITE);
				text.setTextSize(25.0f);
				text.setGravity(Gravity.CENTER);
				frame.addView(text, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			if(pDiag != null)
				pDiag.dismiss();
		}
	}
	
	private void buildList(ArrayList<String> received) {
		
		final String[] names = FileHandler.getNames();
		String[] regions = FileHandler.getRegions();
		
		boolean[] status = new boolean[received.size()];
		String[] changes = new String[received.size()];
		
		for(int i = 0; i < received.size(); i++) {
			String[] split = received.get(i).split(",");
			float ch = Float.parseFloat(split[4]);
			if(ch > 0) status[i] = true;
			else status[i] = false;
			
			changes[i] = Math.abs(ch)+"";
		}
		
		ListView listResults = (ListView) rootView.findViewById(R.id.list_share_following);

		listResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), CompanyActivity.class);
				intent.putExtra("Tick", FileHandler.getTickFromName(names[arg2]));
				startActivity(intent);
			}
			
		});
		
		listResults.setAdapter(new CompanyAdapter(getActivity(), R.layout.company_box, 
				names, regions, status, changes));
	}
}
