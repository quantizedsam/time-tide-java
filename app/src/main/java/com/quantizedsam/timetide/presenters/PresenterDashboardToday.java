package com.quantizedsam.timetide.presenters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.quantizedsam.timetide.databases.DatabaseHabit;
import com.quantizedsam.timetide.databases.DatabaseTask;
import com.quantizedsam.timetide.interfaces.InterfaceDashboardToday;
import com.quantizedsam.timetide.models.Habit;
import com.quantizedsam.timetide.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PresenterDashboardToday implements InterfaceDashboardToday.Presenter {

    // dev classes
    private DatabaseHabit mDatabaseHabit;
    private DatabaseTask mDatabaseTask;
    private InterfaceDashboardToday.View mView;

    // variables
    private boolean isHabitsSelected, isTasksSelected, isAlarmsSelected;

    // constants
    private String className = getClass().getCanonicalName();
    private static final String DATABASE_HABITS_NAME = "database-habits";
    private static final String DATABASE_TASKS_NAME = "database-tasks";

    public PresenterDashboardToday (InterfaceDashboardToday.View view, Context context) {
        mView = view;
        mDatabaseHabit = Room.databaseBuilder(context.getApplicationContext(), DatabaseHabit.class, DATABASE_HABITS_NAME).allowMainThreadQueries().build();
        mDatabaseTask = Room.databaseBuilder(context.getApplicationContext(), DatabaseTask.class, DATABASE_TASKS_NAME).allowMainThreadQueries().build();
    }

    @Override
    public void initialize() {
        onTasksTapped();
    }

    @Override
    public void onHabitsTapped() {
        Log.d(className, "Habits tapped");

        resetViews();
        isHabitsSelected = !isHabitsSelected;
        mView.toggleHabitsStatus(isHabitsSelected);

        mView.updateHabits(getAllHabits());
    }

    @Override
    public void onTasksTapped() {
        Log.d(className, "Tasks tapped");

        resetViews();
        isTasksSelected = !isTasksSelected;
        mView.toggleTasksStatus(isTasksSelected);

        mView.updateTasks(getAllTasks());
    }

    @Override
    public void insertHabit(String habitName, String habitTime, boolean habitAlarmOn, int habitDuration, boolean habitTimerOn) {
        Log.d(className, "Creating habit: " + habitName);

        Date currentTime = Calendar.getInstance().getTime();
        final Habit habit = new Habit();
        habit.setName(habitName);
        habit.setTime(habitTime);
        habit.setAlarmOn(habitAlarmOn);
        habit.setDuration(habitDuration);
        habit.setTimerOn(habitTimerOn);
        habit.setStatus(0);
        habit.setSchedule("1111110");
        habit.setCreatedAt(currentTime);
        habit.setModifiedAt(currentTime);

        insertHabit(habit);
    }

    @Override
    public void insertHabit(final Habit habit) {
        Log.d(className, "Inserting habit");

        mDatabaseHabit.daoHabit().insertHabit(habit);
        onHabitsTapped();
    }

    @Override
    public ArrayList<Habit> getAllHabits() {
        Log.d(className, "Getting all habits");

        return new ArrayList<>(mDatabaseHabit.daoHabit().getAllHabits());
    }

    @Override
    public Habit getHabit(String habitName) {
        Log.d(className, "Getting habit: " + habitName);

        return mDatabaseHabit.daoHabit().getHabit(habitName);
    }

    @Override
    public void updateHabit(final Habit habit) {
        Log.d(className, "Updating habit: " + habit.getName());
        habit.setModifiedAt(Calendar.getInstance().getTime());

        mDatabaseHabit.daoHabit().updateHabit(habit);
        onHabitsTapped();
    }

    @Override
    public void deleteHabit(final Habit habit) {
        Log.d(className, "Deleting habit: " + habit.getName());

        mDatabaseHabit.daoHabit().deleteHabit(habit);
        onHabitsTapped();
    }

    @Override
    public void deleteHabit(final int habitId) {
        Log.d(className, "Deleting habit with id: " + habitId);

        mDatabaseHabit.daoHabit().deleteHabit(habitId);
        onHabitsTapped();
    }

    @Override
    public void deleteHabit(final String habitName) {
        Log.d(className, "Deleting habit with name: " + habitName);


        mDatabaseHabit.daoHabit().deleteHabit(habitName);
        onHabitsTapped();
    }

    @Override
    public void insertTask(String taskName, String taskDescription, String taskDueDate, boolean taskAlarmOnDue, String taskStartDate, boolean taskAlarmOnStart, String taskParent, String taskColor, int taskProgress) {
        Log.d(className, "Creating task: " + taskName);

        Date currentTime = Calendar.getInstance().getTime();
        final Task task = new Task();
        task.setName(taskName);
        task.setDescription(taskDescription);
        task.setDueDate(taskDueDate);
        task.setAlarmOnDue(taskAlarmOnDue);
        task.setStartDate(taskStartDate);
        task.setAlarmOnStart(taskAlarmOnStart);
        task.setParent(taskParent);
        task.setColor(taskColor);
        task.setProgress(taskProgress);
        task.setStatus(0);
        task.setCreatedAt(currentTime);
        task.setModifiedAt(currentTime);

        insertTask(task);
    }

    @Override
    public void insertTask(final Task task) {
        Log.d(className, "Inserting task");

        mDatabaseTask.daoTask().insertTask(task);
        onTasksTapped();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        Log.d(className, "Getting all tasks");

        return new ArrayList<>(mDatabaseTask.daoTask().getAllTasks());
    }

    @Override
    public Task getTask(String taskName) {
        Log.d(className, "Getting task: " + taskName);

        return mDatabaseTask.daoTask().getTask(taskName);
    }

    @Override
    public void updateTask(final Task task) {
        Log.d(className, "Updating task: " + task.getName());
        task.setModifiedAt(Calendar.getInstance().getTime());

        mDatabaseTask.daoTask().updateTask(task);
        onTasksTapped();
    }

    @Override
    public void deleteTask(final Task task) {
        Log.d(className, "Deleting task: " + task.getName());

        mDatabaseTask.daoTask().deleteTask(task);
        onTasksTapped();
    }

    @Override
    public void deleteTask(final int taskId) {
        Log.d(className, "Deleting task with id: " + taskId);

        mDatabaseTask.daoTask().deleteTask(taskId);
        onTasksTapped();
    }

    @Override
    public void deleteTask(final String taskName) {
        Log.d(className, "Deleting task with name: " + taskName);

        mDatabaseTask.daoTask().deleteTask(taskName);
        onTasksTapped();
    }

    private void resetViews() {
        isHabitsSelected = false;
        isTasksSelected = false;
        mView.resetView();
    }

}
