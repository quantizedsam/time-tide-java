package com.quantizedsam.timetide.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.quantizedsam.timetide.models.Habit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DBHabits extends SQLiteOpenHelper {

    // constants
    private static final String DATABASE_HABITS_NAME = "databaseHabits";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HABITS_NAME = "tableHabits";
    private static final String TABLE_WEEKLY_PROGRESSES_NAME = getTableWeeklyProgressesName();
    private static final String TABLE_MONTHLY_PROGRESSES_NAME = getTableMonthlyProgressesName();
    private static final String KEY_ID = "id";
    private static final String KEY_HABIT_ID = "habit_id";
    private static final String KEY_HABIT_NAME = "habit_name";
    private static final String KEY_HABIT_TIME = "habit_time";
    private static final String KEY_HABIT_DURATION = "habit_duration";
    private static final String KEY_HABIT_PROGRESS = "habit_progress";
    private static final String KEY_HABIT_WEEKLY_SCHEDULE = "habit_weekly_schedule";
    private static final String KEY_HABIT_DATE_TIME_CREATED = "habit_date_time_created";
    private static final String KEY_HABIT_DATE_TIME_MODIFIED = "habit_date_time_modified";

    private String className = getClass().getCanonicalName();

    public DBHabits(Context context) {
        super(context, DATABASE_HABITS_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getTableHabitsTag());

        //db.execSQL(getTableWeeklyProgressesTag());

        //db.execSQL(getTableMonthlyProgressesTag());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEEKLY_PROGRESSES_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_PROGRESSES_NAME);

        onCreate(db);
    }

    private String getTableHabitsTag() {
        String TABLE_HABITS_TAG = "CREATE TABLE " + TABLE_HABITS_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HABIT_NAME + " TEXT,"
                + KEY_HABIT_TIME + " TEXT,"
                + KEY_HABIT_DURATION + " INTEGER,"
                + KEY_HABIT_PROGRESS + " INTEGER,"
                + KEY_HABIT_WEEKLY_SCHEDULE + " TEXT,"
                + KEY_HABIT_DATE_TIME_CREATED + " TEXT,"
                + KEY_HABIT_DATE_TIME_MODIFIED + " TEXT)";

        return TABLE_HABITS_TAG;
    }

    private static final String getTableWeeklyProgressesName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyww");
        String strYearWeek = sdf.format(new Date());

        return "tableWeeklyProgresses" + strYearWeek;
    }

    private String getTableWeeklyProgressesTag() {
        String TABLE_WEEKLY_TAG = "CREATE TABLE " + TABLE_WEEKLY_PROGRESSES_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HABIT_ID + " INTEGER,"
                + "Sun" + " INTEGER,"
                + "Mon" + " INTEGER,"
                + "Tue" + " INTEGER,"
                + "Wed" + " INTEGER,"
                + "Thu" + " INTEGER,"
                + "Fri" + " INTEGER,"
                + "Sat" + " INTEGER"
                + ")";

        return TABLE_WEEKLY_TAG;
    }

    private static final String getTableMonthlyProgressesName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String strYearMonth = sdf.format(new Date());

        return "tableMonthlyProgresses" + strYearMonth;
    }

    private String getTableMonthlyProgressesTag() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        StringBuilder sbTag = new StringBuilder("CREATE TABLE " + TABLE_MONTHLY_PROGRESSES_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HABIT_ID + " INTEGER");
        for (int i = 1; i <= numDays; i++) {
            sbTag.append(",day_");
            sbTag.append(String.format("%02d", i));
            sbTag.append(" INTEGER");
        }
        sbTag.append(")");
        Log.d(className, sbTag.toString());

        return sbTag.toString();
    }

    public long addHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date dateNow = new Date();

        Log.d(className, "Adding Habit: " + habit.toString());

        ContentValues cvHabit = new ContentValues();
        cvHabit.put(KEY_HABIT_NAME, habit.getName());
        cvHabit.put(KEY_HABIT_TIME, habit.getTime().toString());
        cvHabit.put(KEY_HABIT_DURATION, habit.getDuration());
        cvHabit.put(KEY_HABIT_PROGRESS, habit.getStatus());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String strDateTime = sdf.format(dateNow);
        cvHabit.put(KEY_HABIT_DATE_TIME_CREATED, strDateTime);
        cvHabit.put(KEY_HABIT_DATE_TIME_MODIFIED, strDateTime);
        long habitID = db.insert(TABLE_HABITS_NAME, null, cvHabit);

        /*ContentValues cvWeeklyProgress = new ContentValues();
        cvWeeklyProgress.put(KEY_HABIT_ID, habitID);
        sdf = new SimpleDateFormat("EEE");
        String strDay = sdf.format(dateNow);
        cvWeeklyProgress.put(strDay, 0);
        db.insert(TABLE_WEEKLY_PROGRESSES_NAME, null, cvWeeklyProgress);

        ContentValues cvMonthlyProgress = new ContentValues();
        cvMonthlyProgress.put(KEY_HABIT_ID, habitID);
        sdf = new SimpleDateFormat("dd");
        String strDayOfMonth = "day_" + sdf.format(dateNow);
        cvMonthlyProgress.put(strDayOfMonth, 0);
        db.insert(TABLE_MONTHLY_PROGRESSES_NAME, null, cvMonthlyProgress);*/

        db.close();

        return habitID;
    }

    public Habit getHabit(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HABITS_NAME, new String[] { KEY_ID,
                        KEY_HABIT_NAME, KEY_HABIT_TIME, KEY_HABIT_DURATION, KEY_HABIT_PROGRESS}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Habit habit = new Habit();
        habit.setId(Integer.parseInt(cursor.getString(0)));
        habit.setName(cursor.getString(1));
        habit.setTime(cursor.getString(2));
        habit.setDuration(Integer.parseInt(cursor.getString(3)));
        habit.setStatus(Integer.parseInt(cursor.getString(4)));

        db.close();

        return habit;
    }

    public ArrayList<Habit> getHabits() {
        ArrayList<Habit> alHabits = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_HABITS_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit();
                habit.setId(Integer.parseInt(cursor.getString(0)));
                habit.setName(cursor.getString(1));
                habit.setTime(cursor.getString(2));
                habit.setDuration(Integer.parseInt(cursor.getString(3)));
                habit.setStatus(Integer.parseInt(cursor.getString(4)));
                alHabits.add(habit);
            } while (cursor.moveToNext());
        }

        db.close();

        return alHabits;
    }

    public void updateHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date dateNow = new Date();

        ContentValues cvHabit = new ContentValues();
        cvHabit.put(KEY_HABIT_NAME, habit.getName());
        cvHabit.put(KEY_HABIT_TIME, habit.getTime().toString());
        cvHabit.put(KEY_HABIT_DURATION, habit.getDuration());
        cvHabit.put(KEY_HABIT_PROGRESS, habit.getStatus());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String strDateTime = sdf.format(dateNow);
        cvHabit.put(KEY_HABIT_DATE_TIME_MODIFIED, strDateTime);
        db.update(TABLE_HABITS_NAME, cvHabit, KEY_ID + " = ?",
                new String[] { String.valueOf(habit.getId()) });

        /*int progressPercent = habit.getDailyProgress()*100/habit.getDailyFrequency();
        ContentValues cvWeeklyProgress = new ContentValues();
        cvWeeklyProgress.put(KEY_HABIT_ID, habit.getID());
        sdf = new SimpleDateFormat("EEE");
        String strDay = sdf.format(dateNow);
        cvWeeklyProgress.put(strDay, progressPercent);
        db.update(TABLE_WEEKLY_PROGRESSES_NAME, cvWeeklyProgress,KEY_HABIT_ID + " = ?",
                new String[] { String.valueOf(habit.getID()) });

        ContentValues cvMonthlyProgress = new ContentValues();
        cvMonthlyProgress.put(KEY_HABIT_ID, habit.getID());
        sdf = new SimpleDateFormat("dd");
        String strDayOfMonth = "day_" + sdf.format(dateNow);
        cvMonthlyProgress.put(strDayOfMonth, progressPercent);
        db.update(TABLE_MONTHLY_PROGRESSES_NAME, cvMonthlyProgress,KEY_HABIT_ID + " = ?",
                new String[] { String.valueOf(habit.getID()) });*/

        db.close();
    }

    public void deleteHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_HABITS_NAME, KEY_ID + " = ?", new String[] { String.valueOf(habit.getId()) });
        //db.delete(TABLE_WEEKLY_PROGRESSES_NAME, KEY_HABIT_ID + " = ?", new String[] { String.valueOf(habit.getID()) });
        //db.delete(TABLE_MONTHLY_PROGRESSES_NAME, KEY_HABIT_ID + " = ?", new String[] { String.valueOf(habit.getID()) });

        db.close();
    }

    public int getHabitCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HABITS_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        db.close();

        return cursor.getCount();
    }

    public ArrayList<String> getHabitWeeklyProgress(int id) {
        ArrayList<String> alWeeklyProgress = new ArrayList<>();

        alWeeklyProgress.add(getHabit(id).getName());

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEEKLY_PROGRESSES_NAME + " WHERE " + KEY_HABIT_ID + " = " + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        for (int i = 2; i < cursor.getColumnCount(); i++) {
            String value = cursor.getString(i);
            if (value == null) {
                alWeeklyProgress.add(Integer.toString(0));
            }
            else {
                alWeeklyProgress.add(value);
            }
        }

        db.close();

        return alWeeklyProgress;
    }

}
