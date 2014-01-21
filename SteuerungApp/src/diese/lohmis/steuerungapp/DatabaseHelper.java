package diese.lohmis.steuerungapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private Context context;

	DatabaseHelper(Context context){
	    super(
	        context,
	        context.getResources().getString(R.string.dbname),
	        null,
	        Integer.parseInt(context.getResources().getString(R.string.version)));
	    this.context=context;
	  }

	  @Override
	  public void onCreate(SQLiteDatabase db) {
	    for(String sql : context.getResources().getStringArray(R.array.createTables))
	      db.execSQL(sql);
	    //db.close();
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  }
}

