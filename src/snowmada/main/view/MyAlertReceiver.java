package snowmada.main.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlertReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context,DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		
		}

}
