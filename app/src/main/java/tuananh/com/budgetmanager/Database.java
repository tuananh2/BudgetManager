package tuananh.com.budgetmanager;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.renderscript.Sampler;

/**
 * Created by anh.letuan2 on 11/14/2016.
 */

public class Database extends SQLiteOpenHelper {

    public static class Entries implements BaseColumns{
        public static final String TABLE_TRANSACTIONS="transfers";
        public static final String TABLE_PLANS="plans";
        public static final String TABLE_CATEGORIES="categories";
        public static final String NAME="name";
        public static final String VALUE="value";
        public static final String CATEGORY="category";
        public static final String NOTE="note";
        public static final String TYPE="type";
    }


    public static final String SQL_CREATE_TABLE_TRANSFERS       = "Create table "+Entries.TABLE_TRANSACTIONS+" (" +
                                                                Entries.VALUE+" float, " +
                                                                Entries.NOTE +" varchar, "+
                                                                Entries.CATEGORY+" varchar "+
                                                                " );";
    public static final String SQL_CREATE_TABLE_CATEGORIES      = "Create table "+Entries.TABLE_CATEGORIES+" (" +
                                                                Entries.NAME+" varchar, " +
                                                                Entries.TYPE+" varchar "+
                                                                " );";
    public static final String SQL_CREATE_TABLE_PLANS           = "Create table "+Entries.TABLE_PLANS+" (" +
                                                                Entries.VALUE+" float, " +
                                                                Entries.NOTE+" varchar, "+
                                                                Entries.CATEGORY+ " varchar "+
                                                                ");";

    public static final String SQL_CREATE_ENTRIES =SQL_CREATE_TABLE_CATEGORIES+SQL_CREATE_TABLE_PLANS+SQL_CREATE_TABLE_TRANSFERS;
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS" +Entries.TABLE_CATEGORIES+" ;"
                                                    + "DROP TABLE IF EXISTS" +Entries.TABLE_PLANS+" ;"
                                                    +"DROP TABLE IF EXISTS" +Entries.TABLE_TRANSACTIONS+" ;";

    public static final int DATABASE_VERSION    = 1;
    public static final String DATABASE_NAME    = "BudgetManager";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(SQL_DELETE_ENTRIES);
        onCreate(database);
    }

    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onUpgrade(database, oldVersion, newVersion);
    }

    public void addCategory(String categoryName, String type)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Entries.NAME,categoryName.toLowerCase());
        values.put(Entries.TYPE,type.toLowerCase());
        database.insert(Entries.TABLE_CATEGORIES,null,values);
    }

    public Cursor readDatabase(String tableName)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "select * from "+ tableName;
        Cursor cursor =  database.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor;
    }

    public void removeData(String tableName)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(tableName,null,null);
    }

    public void updateData(String type, String tableName)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Entries.TYPE, type);
        String selection = Entries.NAME + " like ?";
        String[] selcectionArgs = {"food"};
        database.update(Entries.TABLE_CATEGORIES,values,selection,selcectionArgs);
    }

    public float getTotalIncome()
    {
        float totalIncome=0;
        Cursor cursor = readDatabase(Entries.TABLE_TRANSACTIONS);
        if(cursor.moveToFirst())
        {
            do {
                if (cursor.getString(2).toLowerCase() == "income") {
                    totalIncome += cursor.getFloat(0);
                }
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
        return totalIncome;
    }

    public float getTotalExpense()
    {
        float totalExpense=0;
        Cursor cursor = readDatabase(Entries.TABLE_TRANSACTIONS);
        if(cursor.moveToFirst())
        {
            do {
                if (cursor.getString(2).toLowerCase() == "expense") {
                    totalExpense += cursor.getFloat(0);
                }
                cursor.moveToNext();
            }while(!cursor.isAfterLast());
        }
        return totalExpense;
    }
}
