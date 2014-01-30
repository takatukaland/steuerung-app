package diese.lohmis.steuerungapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CheckBox;
import android.widget.Switch;

public class Relays {
	int steuerung;
	int relay;
	int ausgang;
	int eingang;
	int led;
	int status;
	boolean aktiv;
	String name;
	String classname;
	RelaySchaltzeiten[] relayschaltzeiten;
	
	Switch mySwitch;
	CheckBox aktivBox;
	
	private static String dbtable = "relays";
	
	public boolean Update(){
		boolean rc = false;
		ContentValues cv = new ContentValues();
		cv.put("steuerung", this.steuerung);
		cv.put("relay", this.relay);
		cv.put("ausgang", this.ausgang);
		cv.put("eingang", this.eingang);
		cv.put("led", this.led);
		cv.put("status", this.status);
		cv.put("aktiv", this.aktiv);
		cv.put("name", this.name);
		cv.put("classname", this.classname);
		
		MainActivity.connection.insertWithOnConflict(Relays.dbtable, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		return rc;
	}

}

