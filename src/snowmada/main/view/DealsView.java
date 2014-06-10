package snowmada.main.view;

import org.json.JSONObject;

import com.strapin.Enum.URL;
import com.strapin.Util.ImageLoader;
import com.strapin.network.KlHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DealsView extends Activity {
    private TextView lbl_merchant,lbl_deals, lbl_address,lbl_description;
    private ImageView iv_dealsImage;
    private String advt_id;
    private ProgressBar progressBar;
    private RelativeLayout ll_main_layout;
    public ImageLoader    imageLoader;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_details);
        advt_id = getIntent().getExtras().getString("advt_id");
        lbl_merchant = (TextView)findViewById(R.id.lbl_marchant);
        lbl_deals = (TextView)findViewById(R.id.lbl_deals);
        lbl_address = (TextView)findViewById(R.id.lbl_address);
        lbl_description = (TextView)findViewById(R.id.lbl_description);
        iv_dealsImage = (ImageView)findViewById(R.id.iv_deals);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
      
        ll_main_layout = (RelativeLayout)findViewById(R.id.ll_main_layout);
        imageLoader = new ImageLoader(this);
        new DealsWeb().execute();
        
    }
    
    
    
    class DealsWeb extends AsyncTask<String, Void, Void> {
	String merchant;
	String deals;
	String address;
	String descriptin;
	String image;

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... param) {
	    try {
		JSONObject request = new JSONObject();
		request.put("advt_id", advt_id);
		JSONObject response = KlHttpClient.SendHttpPost(URL.DEALS_DETAILS.getUrl(), request);
		if(response!=null){
		    merchant = response.getString("name"); 
		    deals = response.getString("advt_name"); 
		    address = response.getString("address"); 
		    descriptin = response.getString("description"); 
		    image = URL.BANNER_ADD.getUrl()+response.getString("advt_image"); 
		   
		}
	    } catch (Exception e) {

		return null;
	    }
	    return null;
	}

	@Override
	protected void onProgressUpdate(Void... unsued) {

	}

	@Override
	protected void onPostExecute(Void sResponse) {
	    imageLoader.DisplayImage(image, iv_dealsImage);
	    lbl_merchant.setText("Marchant - "+merchant);
	    lbl_deals.setText("Deals- "+deals);
	    lbl_address.setText("Address- "+address);
	    lbl_description.setText(descriptin);
	    progressBar.setVisibility(View.INVISIBLE);
	    ll_main_layout.setVisibility(View.VISIBLE);
	}
    }

}
