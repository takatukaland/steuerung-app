package diese.lohmis.steuerungapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Webinterface {
	@SuppressWarnings("unused")
	static String getAll(Context context){
		String jsonResult;
		String rc = "";
		String url = context.getResources().getString(R.string.web_service_url);
		



		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;
			
		try {
			//HttpPost httppost =	new HttpPost(url);
			//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
		}catch ( Exception e ){
			Log.e ("log_tag", "Error in http connection "+ e.toString());
		}


		
		return rc;
	}

}

