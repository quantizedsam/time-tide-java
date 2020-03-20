package com.quantizedsam.timetide.presenters;

import android.content.Context;

import com.quantizedsam.timetide.databases.DBHabits;
import com.quantizedsam.timetide.interfaces.InterfaceDashboardWeek;
import com.quantizedsam.timetide.models.Alarm;
import com.quantizedsam.timetide.models.Habit;
import com.quantizedsam.timetide.models.Task;

import java.util.ArrayList;

public class PresenterDashboardWeek implements InterfaceDashboardWeek.Presenter {

    // dev classes
    private InterfaceDashboardWeek.View mView;
    private ArrayList<Habit> mHabits;
    private ArrayList<Task> mTasks;
    private ArrayList<Alarm> mAlarms;
    private DBHabits mDBHabits;

    // native classes
    private Context mContext;

    // variables
    private boolean isHabitsSelected, isTasksSelected, isAlarmsSelected;

    // constants
    private String className = getClass().getCanonicalName();

    public PresenterDashboardWeek(InterfaceDashboardWeek.View view, Context context) {
        mView = view;
        mContext = context;
        mDBHabits = new DBHabits(mContext);
    }

    @Override
    public void initialize() {
        onHabitsTapped();
    }

    @Override
    public void onHabitsTapped() {
        resetViews();
        isHabitsSelected = !isHabitsSelected;
        mView.toggleHabitsStatus(isHabitsSelected);

        ArrayList<ArrayList<String>> alHabitWeeklyProgresses = new ArrayList<>();
        mHabits = mDBHabits.getHabits();
        for (int i = 0; i < mHabits.size(); i++) {
            alHabitWeeklyProgresses.add(mDBHabits.getHabitWeeklyProgress(mHabits.get(i).getId()));
        }
        mView.updateHabits(alHabitWeeklyProgresses);
    }

    @Override
    public void onTasksTapped() {
        resetViews();
        isTasksSelected = !isTasksSelected;
        mView.toggleTasksStatus(isTasksSelected);

        mView.updateTasks(new ArrayList<ArrayList<String>>());
    }

    @Override
    public void onAlarmsTapped() {
        resetViews();
        isAlarmsSelected = !isAlarmsSelected;
        mView.toggleAlarmsStatus(isAlarmsSelected);

        mView.updateAlarms(new ArrayList<ArrayList<String>>());
    }

    private void resetViews() {
        isHabitsSelected = false;
        isTasksSelected = false;
        isAlarmsSelected = false;
        mView.resetView();
    }

}
