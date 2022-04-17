package com.orangeelephant.sobriety.managecounters;

import android.content.Context;
import android.widget.Toast;

import com.orangeelephant.sobriety.database.CountersDatabase;
import com.orangeelephant.sobriety.database.ReasonsDatabase;
import com.orangeelephant.sobriety.database.helpers.DBOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

public class DeleteCounter {
    private final int counterID;
    private final Context context;
    private final String name;

    public DeleteCounter(Context context, int counterID, String name) {
        this.context = context;
        this.counterID = counterID;
        this.name = name;

        deleteRecord();
    }

    private void deleteRecord() {
        String sqlCounterRecord = "DELETE FROM " + CountersDatabase.TABLE_NAME_COUNTERS +
                " WHERE _id = " + this.counterID;

        SQLiteDatabase db = new DBOpenHelper(this.context).getWritableDatabase();
        db.execSQL(sqlCounterRecord);

        String sqlReasonRecords = "DELETE FROM " + ReasonsDatabase.TABLE_NAME_REASONS +
                " WHERE counter_id = " + this.counterID;
        db.execSQL(sqlReasonRecords);
        db.close();
    }

    public void printDeletionMessage(String toastString) {
        CharSequence message = String.format(toastString, this.name);
        Toast deletionMessage = Toast.makeText(this.context, message, Toast.LENGTH_LONG);
        deletionMessage.show();
    }
}
