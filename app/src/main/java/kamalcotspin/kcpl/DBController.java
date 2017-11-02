package kamalcotspin.kcpl;

/**
 * Created by Archit on 03-09-2017.
 */

        import java.io.File;
        import java.util.ArrayList;
        import java.util.HashMap;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.Environment;

public class DBController  extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, Environment.getExternalStorageDirectory()+ File.separator + "kcpl"+File.separator+"kcpl.db", null, 2);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query1,query2,query3,query4,query5,query6;
        query1 = "CREATE TABLE countDetails ( countType TEXT, countVariant TEXT, countPrice REAL , PRIMARY KEY (countType, countVariant))";
        query2 = "CREATE TABLE countTypes ( countType TEXT , PRIMARY KEY (countType))";
        query3 = "INSERT into  countTypes (countType) values ('KW'),('KH'),('CW'),('CH'),('CCW'),('CCH'),('OE 1700'),('OE 1850'),('OE 1900'),('OE others'),('Others'),('ALL')";
        query4 = "CREATE TABLE headnfoot ( title TEXT, content TEXT, horf TEXT , active INTEGER ,PRIMARY KEY (title))";
        query5 = "CREATE TABLE flags ( flagname TEXT, flagvalue TEXT, PRIMARY KEY (flagname))";
        query6 = "INSERT into  flags (flagname,flagvalue) values ('timestamp','0'),('header','0'),('footer','0')";
        database.execSQL(query1);
        database.execSQL(query2);
        database.execSQL(query3);
        database.execSQL(query4);
        database.execSQL(query5);
        database.execSQL(query6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS countDetails";
        database.execSQL(query);
        onCreate(database);
    }

    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertCount(HashMap<String, String> queryValues) {
        //System.out.println(queryValues);
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("countType", queryValues.get("countType"));
        values.put("countVariant", queryValues.get("countVariant"));
        values.put("countPrice", queryValues.get("countPrice"));
        database.replace("countDetails", null, values);
        database.close();
    }

    public void updateTypes(HashMap<String, String> queryValues) {
        //System.out.println(queryValues);
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "INSERT into  countTypes (countType) values ('" + queryValues.get("countType") +"')" ;
        database.execSQL(query);
    }


    public void deleteTable (String tablename){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM " + tablename ;
        database.execSQL(query);
    }


    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllCounts(String countType) {
        ArrayList<HashMap<String, String>> countsList;
        countsList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "";
        if (countType.equals("ALL")){
            selectQuery = "SELECT  * FROM countDetails where countType in (select * from countTypes)order by countType,countVariant";
        }
        else {
            selectQuery = "SELECT  * FROM countDetails where countType like '" + countType+"'";
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("countType", cursor.getString(0));
                map.put("countVariant", cursor.getString(1));
                map.put("countPrice", cursor.getString(2));
                countsList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return countsList;
    }


    public String[] getCountTypes() {
        String selectQuery = "SELECT  * FROM countTypes order by countType";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int i = 0;
        String[] countTypes = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                countTypes[i] = cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        database.close();
        return countTypes;
    }

    public String[] getTitle(String horf) {
        String selectQuery = "SELECT  title FROM headnfoot where horf = '"+horf+"'order by title";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int i = 0;
        String[] Titles = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                Titles[i] = cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        database.close();
        return Titles;
    }


    public String[] getContent(String horf) {
        String selectQuery = "SELECT  content FROM headnfoot where title = '"+horf+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int i = 0;
        String[] Titles = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                Titles[i] = cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        database.close();
        return Titles;
    }

    public void updateContent(String oldTitle, String title, String content, String horf) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM headnfoot where title = '"+oldTitle+"'";
        database.execSQL(deleteQuery);
        String insertQuery = "INSERT INTO headnfoot (title, content, horf, active) values ('"+title+"','"+content+"','"+horf+"','"+0+"')";
        database.execSQL(insertQuery);
    }

    public ArrayList<HashMap<String, String>> getFlags() {
        ArrayList<HashMap<String, String>> flagList;
        flagList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "";
        selectQuery = "SELECT * FROM flags";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("flagname", cursor.getString(0));
                map.put("flagvalue", cursor.getString(1));
                flagList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return flagList;
    }

    public void updateFormatFlags(String flagname, String flagvalue) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE flags SET flagvalue = '"+flagvalue+"' WHERE flagname = '"+flagname+"'";
        database.execSQL(updateQuery);
    }

    public void updateActivehorf(String title, String horf) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE headnfoot SET active = '"+0+"' WHERE horf = '"+horf+"' and active = '"+1+"'";
        database.execSQL(updateQuery);
        String updateQuery2 = "UPDATE headnfoot SET active = '"+1+"' WHERE title = '"+title+"' and horf = '"+horf+"'";
        database.execSQL(updateQuery2);
    }

    public ArrayList<HashMap<String, String>> getActivehorf(String horf) {
        ArrayList<HashMap<String, String>> flagList;
        flagList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT title,content FROM headnfoot where active = '1' and horf = '"+horf+"'";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", cursor.getString(0));
                map.put("content", cursor.getString(1));
                flagList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return flagList;
    }
}