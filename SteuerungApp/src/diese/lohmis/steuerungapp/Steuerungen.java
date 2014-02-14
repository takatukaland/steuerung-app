package diese.lohmis.steuerungapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Steuerungen {
	int steuerung;
	String name;
	String ip;
	boolean aktiv;
	String classname;
	

	Relays[] relays;
	
	private static String dbtable = "steuerungen";

//	@SuppressWarnings("null")
	static ArrayList<Steuerungen> getAll(){
		ArrayList<Steuerungen> rc = new ArrayList<Steuerungen>();
		Steuerungen aktSteuerung = new Steuerungen();
		
	    Cursor cursor = 
	            //MainActivity.connection.rawQuery("select name,id,ip,aktiv,classname from steuerungen", null);
	    		MainActivity.connection.query(dbtable, null, null, null, null, null, "id");
	    
	    // 3. if we got results get the first one
	    while(cursor.moveToNext ()){
	    	aktSteuerung.name = cursor.getString(cursor.getColumnIndex("name"));
	    	aktSteuerung.steuerung = Integer.parseInt(cursor.getString(cursor.getColumnIndex("steuerung")));
	    	aktSteuerung.ip = cursor.getString(cursor.getColumnIndex("ip"));
	    	aktSteuerung.aktiv = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("aktiv")));
	    	aktSteuerung.classname = cursor.getString(cursor.getColumnIndex("classname"));
	    	rc.add(aktSteuerung);
	    }
		return rc;
	}
	
	public boolean Update(){
		boolean rc = false;
		ContentValues cv = new ContentValues();
		cv.put("steuerung", this.steuerung);
		cv.put("name", this.name);
		cv.put("ip", this.ip);
		cv.put("aktiv", this.aktiv);
		cv.put("classname", this.classname);
		
		MainActivity.connection.insertWithOnConflict(Steuerungen.dbtable, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		return rc;
	}
	
}
