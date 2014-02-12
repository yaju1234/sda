package snowmada.main.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.network.KlHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ReceiverStatus extends BroadcastReceiver {
	private SnowmadaDbAdapter mDbAdapter;

	@Override
	public void onReceive(Context context, Intent intent) {
		mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(context);
		new getAppUsers().execute();
	}

	public class getAppUsers extends AsyncTask<String, Void, Boolean> {
		protected void onPreExecute() {

		}

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject jsonObject = new JSONObject();
	  		try {
				jsonObject.put("fbid", mDbAdapter.getUserFbID());
				JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/app_user.php", jsonObject);
				if(json!=null){
					return json.getBoolean("status");
				}
	  		} catch (JSONException e) {
				e.printStackTrace();
			}
	  		
			return null;
		}

		@Override
		protected void onPostExecute(Boolean status) {

		}
	}

}
