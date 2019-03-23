package nitishpoddar1812.calculator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_VARIABLES = "variables";
    private static final String KEY_ID = "id";
    private static final String KEY_VARIABLE = "variable";
    private static final String KEY_VALUE = "value";

    private static final String DATABASE_NAME = "variables.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_VARIABLES + "( " + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_VARIABLE + " TEXT, "
            + KEY_VALUE + " TEXT" + ");";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLES);
        onCreate(db);
    }

    void addVariable(Variable variable) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VARIABLE, variable.getVariable());
        values.put(KEY_VALUE, variable.getValue());

        database.insert(TABLE_VARIABLES, null, values);
        database.close();
    }

    ArrayList<Variable> getAllVariables() {
        ArrayList<Variable> varList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_VARIABLES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Variable variable = new Variable();
                variable.setId(cursor.getLong(0));
                variable.setVariable(cursor.getString(1));
                variable.setValue(cursor.getString(2));
                varList.add(variable);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return varList;
    }

    int updateVariable(Variable variable) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VARIABLE, variable.getVariable());
        values.put(KEY_VALUE, variable.getValue());

        return database.update(TABLE_VARIABLES, values, KEY_VARIABLE + " =?",
                new String[]{String.valueOf(variable.getVariable())});
    }

    void deleteVariable(Variable variable) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_VARIABLES, KEY_VARIABLE + " =?",
                new String[]{String.valueOf(variable.getVariable())});
        database.close();
        SQLiteDatabase.releaseMemory();
    }
}