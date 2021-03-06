package ie.dit.societiesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SocDBOpenHelper extends SQLiteOpenHelper {

    public SocDBOpenHelper(Context context) {
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
        db.execSQL("CREATE TABLE user_details("
                + "member_id INTEGER PRIMARY KEY,"
                + "name TEXT,"
                + "email TEXT,"
                + "dob TEXT,"
                + "mobile TEXT,"
                + "emergency_ph TEXT,"
                + "full_part_time TEXT);"
        );
        Log.d("DBDEBUG", "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    /* Methods for querying */

    // Get details of a single society by id
    public Cursor getSociety(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM society WHERE(society_id = " + id + ");", null);
        return res;
    }

    public ArrayList<String> getSocietyNames() {
        ArrayList<String> socs = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT name FROM society;", null);
        res.moveToFirst();
        for(int i = 0; i < res.getCount(); i++) {
            socs.add(res.getString(0));
            res.moveToNext();
        }

        return socs;
    }

    // Returns -1 if society name is not in database
    public int getSocietyIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT society_id FROM society WHERE name = '" + name + "';", null);
        if(res.getCount() != 0) {
            res.moveToFirst();
            return res.getInt(0);
        } else {
            return -1;
        }
    }

    public Cursor getUserDetails(int member_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM user_details WHERE(member_id = " + member_id + ");", null);
        return res;
    }

    /* Check if user is a member, committee member, or chair of a society */

    public boolean checkMember(int id) { return checkField(id, "is_member"); }
    public boolean checkCommittee(int id) { return checkField(id, "is_committee"); }
    public boolean checkChair(int id) { return checkField(id, "is_chair" ); }

    // Called by the other check methods
    private boolean checkField(int id, String field) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM society WHERE(society_id = " + id + " AND " + field + " = 1);", null);
        return res.getCount() == 1;
    }

    // Get the list of societies that user is a committee member of
    //public Cursor getCommittee


    /* Methods for inserting and updating */


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
        long result = db.replace("society", null, cv);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateUserDetails(int member_id, String name, String email, String dob,
                                     String mobile, String emergency_ph, String full_part_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("member_id", member_id);
        cv.put("name", name);
        cv.put("email", email);
        cv.put("dob", dob);
        cv.put("mobile", mobile);
        cv.put("emergency_ph", emergency_ph);
        cv.put("full_part_time", full_part_time);
        long result = db.replace("user_details", null, cv);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Used to update user details through the user details fragment
    public boolean partialUpdateUserDetails(int member_id, String name, String mobile, String emergency_ph) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("mobile", mobile);
        cv.put("emergency_ph", emergency_ph);
        long result = db.update("user_details", cv, "member_id = " + member_id, null);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Clear the user from being a member of any society
    public boolean clearMember() {
        return clearField("is_member");
    }

    // Clear the user from being a member of any society
    public boolean clearCommittee() {
        return clearField("is_committee");
    }

    // Clear the user from being a member of any society
    public boolean clearChair() {
        return clearField("is_chair");
    }

    private boolean clearField(String field) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(field, 0);
        long res = db.update("society", cv, null, null);

        return res != -1;
    }

    // Set this user's member status for a society
    public boolean setMember(int id, int val) {
        return setField(id, val, "is_member");
    }

    // Set this user's committee status for a society
    public boolean setCommittee(int id, int val) {
        return setField(id, val, "is_committee");
    }

    // Set this user's chair status for a society
    public boolean setChair(int id, int val) {
        return setField(id, val, "is_chair");
    }

    private boolean setField(int id, int val, String field) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(field, val);
        long res = db.update("society", cv, "society_id = " + id, null);

        return res != -1;
    }
}
