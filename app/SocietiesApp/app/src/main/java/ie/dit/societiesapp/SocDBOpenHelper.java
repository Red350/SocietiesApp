package ie.dit.societiesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SocDBOpenHelper extends SQLiteOpenHelper {

    SocDBOpenHelper(Context context) {
        super(context, "soc", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE society( "
                + "society_id INTEGER PRIMARY KEY,"
                + "name TEXT,"
                + "email TEXT,"
                + "description TEXT,"
                + "is_member INTEGER,"
                + "is_committee INTEGER,"
                + "is_chair INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean addSociety(int society_id, String name, String email, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("society_id", society_id);
        cv.put("name", name);
        cv.put("email", email);
        cv.put("description", description);
        cv.put("is_member", 0);
        cv.put("is_committee", 0);
        cv.put("is_chair", 0);
        long result = db.insert("society", null, cv);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getSociety(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM society WHERE(society_id = " + id + ");", null);
        return res;
    }

    // Set this user's member status for a society
    public boolean setMember(int id, int val) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("is_member", val);
        long res = db.update("society", cv, "id = " + id, null);

        if(res == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Set this user's committee status for a society
    public boolean setCommittee(int id, int val) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("is_committee", val);
        long res = db.update("society", cv, "id = " + id, null);

        if(res == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Set this user's chair status for a society
    public boolean setChair(int id, int val) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("is_chair", val);
        long res = db.update("society", cv, "id = " + id, null);

        if(res == -1) {
            return false;
        } else {
            return true;
        }
    }
}
