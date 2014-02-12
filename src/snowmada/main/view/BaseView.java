package snowmada.main.view;


import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.strapin.Interface.IBase;
import com.strapin.application.AppInfo;
import com.strapin.application.SnomadaApp;

import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BaseView extends FragmentActivity implements IBase,OnClickListener,TextWatcher,OnMarkerClickListener,OnMapLongClickListener,OnMarkerDragListener,OnInfoWindowClickListener{
	public SnomadaApp app;
	public boolean inIt  = false;

	@Override
	protected void onStart() {
		super.onStart();
		app = (SnomadaApp) getApplication();
		if(!app.inIt){
			app.inIt = true;
			app.setAppInfo(new AppInfo(this));
		}
		init();
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {	}

	@Override
	public void onMarkerDrag(Marker marker) {	}

	@Override
	public void onMarkerDragEnd(Marker marker) {	}

	@Override
	public void onMarkerDragStart(Marker marker) {	}

	@Override
	public void onMapLongClick(LatLng point) {	}


	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	@Override
	public void afterTextChanged(Editable s) {	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {	}

	@Override
	public void onClick(View v) {	}
	
	@Override
	public void init(){	}

	@Override
	public void setCustomizeColorText(Button view,String white, String blue) {
		view.setText(Html.fromHtml("<font color=\"#ffffff\">"+white+"</font><font color=\"#28b6ff\">"+blue+"</font>"));
		
	}
	

}
