package diese.lohmis.steuerungapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
//import android.view.ViewGroup.LayoutParams;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	public static ArrayList<Steuerungen> steuerungen = new ArrayList<Steuerungen>();
	public static ArrayList<RelaySwitch> RelaySwitche = new ArrayList<RelaySwitch>();
	public static ArrayList<ToggleButton> ToggleButtons = new ArrayList<ToggleButton>();
	public static SQLiteOpenHelper database;
	public static SQLiteDatabase connection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for(String sql : getResources().getStringArray(R.array.createTables))
			   System.out.println(sql);
		
		database = new DatabaseHelper(this);
		connection = database.getWritableDatabase();
		connection.enableWriteAheadLogging();
		
		UpdateLocalDB task = new UpdateLocalDB(this.getBaseContext());
		// passes values for the urls string array
		task.execute(new String[] { getResources().getString(R.string.web_service_url) });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	// Handle item selection
	/*switch (item.getItemId()) {
		case R.id.menu:
			newGame(); return true;
		case R.id.help: showHelp();*/
		return true; 
		/*default: return super.onOptionsItemSelected(item);
	} */
	}
	
	private class RelaySwitch extends Switch{
		public RelaySwitch(Context context, Relays relay) {
			super(context);
			LayoutParams layout = null;
			
			//layout.setGravity();
			
			this.setText(relay.name);
			this.setLayoutParams(layout);
			// TODO Auto-generated constructor stub
		}
	}
	
	private class UpdateLocalDB extends AsyncTask<String, Void, String> {
		private Context mContext;
		String jsonResult;
		
	    public UpdateLocalDB (Context context){
	         mContext = context;
	    }
	    
		@Override
		protected String doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			
			try {
				HttpResponse response = httpClient.execute(httpGet);
				jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private StringBuilder inputStreamToString(InputStream is) {
			   String rLine = "";
			   StringBuilder answer = new StringBuilder();
			   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			 
			   try {
			    while ((rLine = rd.readLine()) != null) {
			     answer.append(rLine);
			    }
			   }
			 
			   catch (IOException e) {
			    // e.printStackTrace();
			    Toast.makeText(getApplicationContext(),
			      "Error..." + e.toString(), Toast.LENGTH_LONG).show();
			   }
			   return answer;
			  }
		
		@Override
		  protected void onPostExecute(String result) {
		   //ListDrwaer();
			try {
				JSONObject jsonResponse = new JSONObject(jsonResult);
				JSONArray jsSteuerungen = jsonResponse.optJSONArray("Steuerungen");
				for (int i = 0; i < jsSteuerungen.length(); i++) {
				    JSONObject jsonChildNode = jsSteuerungen.getJSONObject(i);
				    Steuerungen AktSteuerung = new Steuerungen();
				    AktSteuerung.name = jsonChildNode.optString("name");
				    AktSteuerung.id = jsonChildNode.optInt("id");
				    AktSteuerung.ip = jsonChildNode.optString("ip");
				    AktSteuerung.aktiv = jsonChildNode.optBoolean("aktiv");
				    AktSteuerung.classname = jsonChildNode.optString("classname");
				    
				    AktSteuerung.Update();
				    
				    JSONArray jsRelays= jsonChildNode.optJSONArray("relays");
					for (int iRelay = 0; iRelay < jsRelays.length(); iRelay++) {
					    JSONObject jsonChildNodeRelay = jsRelays.getJSONObject(iRelay);
					    Relays AktRelay = new Relays();
					    AktRelay.steuerung	= jsonChildNodeRelay.optInt("steuerung");
					    AktRelay.relay		= jsonChildNodeRelay.optInt("relay");
					    AktRelay.ausgang	= jsonChildNodeRelay.optInt("ausgang");
					    AktRelay.eingang	= jsonChildNodeRelay.optInt("eingang");
					    AktRelay.led		= jsonChildNodeRelay.optInt("led");
					    AktRelay.status		= jsonChildNodeRelay.optInt("status");
					    
					    AktRelay.aktiv 		= jsonChildNodeRelay.optBoolean("aktiv");

					    AktRelay.name		= jsonChildNodeRelay.optString("name");
					    AktRelay.classname	= jsonChildNodeRelay.optString("classname");
					    
					    //RelaySwitch tmpSwitch = new RelaySwitch(mContext, AktRelay);
					    
					    //MainActivity.RelaySwitche.add(new RelaySwitch(mContext, AktRelay));
					    LinearLayout linear = (LinearLayout)findViewById(R.id.AktiveRelays);
					    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					            LinearLayout.LayoutParams.MATCH_PARENT,
					            LinearLayout.LayoutParams.WRAP_CONTENT);
					    
					    ToggleButton btn = new ToggleButton(mContext);
					    //Button btn = new Button(mContext);
					    
					    btn.setId(AktSteuerung.id * 16 + AktRelay.relay);
					    btn.setText(AktRelay.name);
					    btn.setTextOn(AktRelay.name);
					    btn.setTextOff(AktRelay.name);
					    btn.setTextColor(Color.BLACK);
					    linear.addView(btn, params);
					    
					    AktRelay.Update();
				    
				    } // Ende FOR Relays
				
				} // Ende FOR Steuerungen
				
				MainActivity.steuerungen = Steuerungen.getAll();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	 

	 }
}

