package ispy.corp.moneywzrd.accounts.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ispy.corp.moneywzrd.accounts.objects.Account;
//ISpy Corp
//Data access object
public class DAO extends SQLiteOpenHelper {
    //Creates a data bank in SQLite
    public DAO(Context context){
        super(context,"banco",null, 11);
    }

    //Creates a table account in the database to save the name and value of the accounts
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE account(name TEXT UNIQUE,value Integer);";
        db.execSQL(sql);
    }
    //Updates the table if it already exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS account;";
        db.execSQL(sql);
        onCreate(db);
    }
    //Inserts account name and value if the parameter passed to whichAcc is not null, that works with the edit button to edit the account name you selected, if
    //it is null it just gets the name that you put
    public void insertAccount(Account account, String whichAcc) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = new ContentValues();
        if(whichAcc != null){
            data.put("name", whichAcc);
        }
        else{
            data.put("name", account.getName());
        }

        data.put("value", account.getValue());
        try{

            db.insertOrThrow("account", null, data);}
        catch (SQLiteConstraintException e){
            data.put("name", account.getName());
            db.update("account",data,"name = ?", new String[]{whichAcc});
        }
    }
    //This method goes over the values stored in the table account, it return accounts, with the names and values in that list
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
    //This deletes the account from the table which has the same name as the parameter passed
    public void deleteAccount(String name){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM account WHERE name = " + "'" + name + "'";
        db.execSQL(sql);
    }

}
