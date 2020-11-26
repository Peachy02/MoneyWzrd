package ispy.corp.moneywzrd.accounts.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ispy.corp.moneywzrd.accounts.objects.Account;

public class DAO extends SQLiteOpenHelper {
    public DAO(Context context){
        super(context,"banco",null, 3);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE account(name TEXT,value Integer);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS account;";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insertAccount(Account account) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();

        data.put("name", account.getName());
        data.put("value", account.getValue());
        db.insert("account", null, data);
    }
    public List<Account> searchAccount(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM account;";

        Cursor c = db.rawQuery(sql,null);

        List<Account> accounts = new ArrayList<Account>();

        while(c.moveToNext()){

            Account account = new Account();
            account.setName(c.getString(c.getColumnIndex("name")));
            account.setValue(Integer.valueOf(c.getString(c.getColumnIndex("value"))));
            accounts.add(account);

        }
        return accounts;

    }
    public void deleteAccount(String name){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM account WHERE name = " + "'" + name + "'";
        db.execSQL(sql);
    }

}
