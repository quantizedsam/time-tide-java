package com.quantizedsam.timetide.interfaces;

import java.util.ArrayList;

public interface InterfaceDashboardWeek {

    interface Presenter {
        void initialize();

        void onHabitsTapped();
        void onTasksTapped();
        void onAlarmsTapped();
    }

    interface View {
        void initView();
        void resetView();

        void toggleHabitsStatus(boolean selected);
        void toggleTasksStatus(boolean selected);
        void toggleAlarmsStatus(boolean selected);

        void updateHabits(ArrayList<ArrayList<String>> alHabitWeeklyProgresses);
        void updateTasks(ArrayList<ArrayList<String>> alTasks);
        void updateAlarms(ArrayList<ArrayList<String>> alAlarms);

        void displayShortToast(String message);
    }
}
