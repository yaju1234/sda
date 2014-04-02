package snowmada.main.view;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.strapin.Enum.URL;
import com.strapin.network.KlHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpView extends BaseView{
	
	public EditText firstName;
	public EditText lastName;
	public EditText email;
	public EditText phone;
	public EditText password;
	public EditText confPassword;
	public Button signUp;
	public static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.signup);
		firstName     = (EditText)findViewById(R.id.et_fname);
		lastName      = (EditText)findViewById(R.id.et_lname);
		email         = (EditText)findViewById(R.id.et_email);
		password      = (EditText)findViewById(R.id.et_password);
		confPassword  = (EditText)findViewById(R.id.et_conf_password);
		phone         = (EditText)findViewById(R.id.et_ph);
		signUp        = (Button)findViewById(R.id.btn_signup);
		signUp.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_signup:
			if(validation()){
				new SignUpWeb().execute();
			}
			break;
		}
	}	
	
	public boolean validation(){
		 boolean flg = true;
		if(firstName.getText().toString().trim().equals("")){
			firstName.setError("Please enter First Name");
			flg = false;
		}
		if(lastName.getText().toString().trim().equals("")){
			lastName.setError("Please enter Last Name");
			flg = false;
		}
		if(email.getText().toString().trim().equals("")){
			email.setError("Please enter Email");
			flg = false;
		}
		if(!isvalidMailid(email.getText().toString().trim())){
			email.setError("Please enter valid Email");
			flg = false;
		}
		
		if(phone.getText().toString().trim().equals("")){
			email.setError("Please enter Phone");
			flg = false;
		}
		
		if(password.getText().toString().trim().equals("")){
			password.setError("Please enter Password");
			flg = false;
		}
		if(confPassword.getText().toString().trim().equals("")){
			confPassword.setError("Please enter Confirm Password");
			flg = false;
		}
		if(!confPassword.getText().toString().trim().equals(password.getText().toString().trim())){
			confPassword.setError("Confirm Password mismatch");
			flg = false;
		}		
		return flg;
	}
	
	public  boolean isvalidMailid(String mail) {		
		return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
	}
	
	public class SignUpWeb extends AsyncTask<Void, Void, Boolean>{	

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDailog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean flg = false;
			
			try {
				JSONObject request = new JSONObject();
				request.put("fname",    firstName.getText().toString().trim());
				request.put("lname",    lastName.getText().toString().trim());
				request.put("phone1",    phone.getText().toString().trim());
				request.put("email",    email.getText().toString().trim());
				request.put("password", password.getText().toString().trim());
				JSONObject response  = KlHttpClient.SendHttpPost(URL.SIGNUP.getUrl(), request);
			if(response!=null){
				flg = response.getBoolean("status");
			}
			} catch (JSONException e) {
				e.printStackTrace();
				dismissProgressDialog();
				return flg;
			}			
			return flg;
		}		

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dismissProgressDialog();
			if(result){				
				Toast.makeText(SignUpView.this, "Sign up successfull", Toast.LENGTH_LONG).show();
				SignUpView.this.finish();
			}
		}
		
	}

}
