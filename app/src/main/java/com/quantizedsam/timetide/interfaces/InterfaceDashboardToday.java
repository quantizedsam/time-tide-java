package com.quantizedsam.timetide.interfaces;

import com.quantizedsam.timetide.models.Habit;
import com.quantizedsam.timetide.models.Task;

import java.util.ArrayList;

public interface InterfaceDashboardToday {

    interface Presenter {
        void initialize();

        void onHabitsTapped();
        void onTasksTapped();

        void insertHabit(String habitName, String habitTime, boolean habitAlarm, int habitDuration, boolean habitTimer);
        void insertHabit(final Habit habit);

        ArrayList<Habit> getAllHabits();
        Habit getHabit(String habitName);

        void updateHabit(final Habit habit);

        void deleteHabit(final Habit habit);
        void deleteHabit(final int habitId);
        void deleteHabit(final String habitName);

        void insertTask(String taskName, String taskDescription, String taskDueDate, boolean taskAlarmOnDue, String taskStartDate, boolean taskAlarmOnStart, String taskParent, String taskColor, int taskProgress);
        void insertTask(final Task task);

        ArrayList<Task> getAllTasks();
        Task getTask(String habitName);

        void updateTask(final Task task);

        void deleteTask(final Task task);
        void deleteTask(final int taskId);
        void deleteTask(final String taskName);
    }

    interface View {
        void initView();
        void resetView();

        void toggleHabitsStatus(boolean selected);
        void toggleTasksStatus(boolean selected);

        void updateHabits(ArrayList<Habit> alHabits);
        void updateTasks(ArrayList<Task> alTasks);

        void displayShortToast(String message);
    }

}