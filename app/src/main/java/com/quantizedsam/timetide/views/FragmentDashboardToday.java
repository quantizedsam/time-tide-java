package com.quantizedsam.timetide.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.quantizedsam.timetide.R;
import com.quantizedsam.timetide.interfaces.InterfaceDashboardToday;
import com.quantizedsam.timetide.models.Habit;
import com.quantizedsam.timetide.models.Task;
import com.quantizedsam.timetide.presenters.PresenterDashboardToday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class FragmentDashboardToday extends Fragment implements InterfaceDashboardToday.View {

    // dev classes
    private InterfaceDashboardToday.Presenter mPresenter;

    // UI elements
    private View mFragment;
    private ImageView ivHabits, ivTasks;
    private TextView tvHabits, tvTasks;
    private ListView lvDetails;

    // native classes
    private Context mContext;
    private Activity mActivity;

    // constants
    private String className = getClass().getCanonicalName();

    // variables
    private ListViewAdapterHabits lvaHabits;
    private boolean isHabitsSelected, isTasksSelected;

    public FragmentDashboardToday() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_dashboard_today, viewGroup, false);
        mContext = getContext();
        mActivity = getActivity();

        initView();

        mPresenter = new PresenterDashboardToday(this, mContext);
        mPresenter.initialize();

        return mFragment;
    }

    @Override
    public void initView() {


        LinearLayout llHabits = mFragment.findViewById(R.id.fragment_dashboard_today_ll_habits);
        ivHabits = mFragment.findViewById(R.id.fragment_dashboard_today_iv_habits);
        tvHabits = mFragment.findViewById(R.id.fragment_dashboard_today_tv_habits);
        llHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onHabitsTapped();
            }
        });

        LinearLayout llTasks = mFragment.findViewById(R.id.fragment_dashboard_today_ll_tasks);
        ivTasks = mFragment.findViewById(R.id.fragment_dashboard_today_iv_tasks);
        tvTasks = mFragment.findViewById(R.id.fragment_dashboard_today_tv_tasks);
        llTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onTasksTapped();
            }
        });

        lvDetails = mFragment.findViewById(R.id.fragment_dashboard_today_lv_details);

        FloatingActionButton fab = mFragment.findViewById(R.id.fragment_dashboard_today_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHabitsSelected) {
                    onAddHabitTapped();
                }
                if (isTasksSelected) {
                    onAddTaskTapped();
                }
            }
        });
    }

    private void onHabitTapped(String habitName) {
        final Habit habit = mPresenter.getHabit(habitName);
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(mContext);

        adBuilder.setTitle(R.string.fragment_dashboard_today_edit_habit_prompt_title);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View viewAlertDialog = inflater.inflate(R.layout.dialog_edit_habit, null);
        adBuilder.setView(viewAlertDialog);

        final EditText etName = viewAlertDialog.findViewById(R.id.dialog_edit_habit_et_name);
        etName.setText(habit.getName());

        final CheckBox cbAlarm = viewAlertDialog.findViewById(R.id.dialog_edit_habit_cb_alarm);
        cbAlarm.setChecked(habit.isAlarmOn());

        final TimePicker tpTime = viewAlertDialog.findViewById(R.id.dialog_edit_habit_tp_time);
        tpTime.setIs24HourView(true);
        tpTime.setHour(Integer.parseInt(habit.getTime().split(":")[0]));
        tpTime.setMinute(Integer.parseInt(habit.getTime().split(":")[1]));

        final EditText etDuration = viewAlertDialog.findViewById(R.id.dialog_edit_habit_et_duration);
        final Spinner spDurationUnit = viewAlertDialog.findViewById(R.id.dialog_edit_habit_sp_duration_unit);
        final TextView etTimer = viewAlertDialog.findViewById(R.id.dialog_edit_habit_tv_timer);
        final CheckBox cbTimer = viewAlertDialog.findViewById(R.id.dialog_edit_habit_cb_timer);
        cbTimer.setChecked(habit.isTimerOn());

        Switch swDuration = viewAlertDialog.findViewById(R.id.dialog_edit_habit_sw_duration);
        final boolean[] isSwitchChecked = {false};
        swDuration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etDuration.setEnabled(true);
                    etDuration.setVisibility(View.VISIBLE);
                    spDurationUnit.setEnabled(true);
                    spDurationUnit.setVisibility(View.VISIBLE);
                    etTimer.setEnabled(true);
                    etTimer.setVisibility(View.VISIBLE);
                    cbTimer.setEnabled(true);
                    cbTimer.setVisibility(View.VISIBLE);
                    isSwitchChecked[0] = true;
                }
                else {
                    etDuration.setEnabled(false);
                    etDuration.setVisibility(View.GONE);
                    spDurationUnit.setEnabled(false);
                    spDurationUnit.setVisibility(View.GONE);
                    etTimer.setEnabled(false);
                    etTimer.setVisibility(View.GONE);
                    cbTimer.setEnabled(false);
                    cbTimer.setVisibility(View.GONE);
                    isSwitchChecked[0] = false;
                }
            }
        });

        int duration = habit.getDuration();
        String[] arrDurationUnits = mContext.getResources().getStringArray(R.array.dialog_edit_habit_sp_duration_unit_items);
        if (duration == -1 || duration == 0) {
            swDuration.setChecked(false);
        }
        else {
            swDuration.setChecked(true);
        }
        if (duration % 3600 == 0) {
            etDuration.setText(String.valueOf(duration/3600));
            spDurationUnit.setSelection(Arrays.asList(arrDurationUnits).indexOf("hour(s)"));
        }
        else if (duration % 60 == 0) {
            etDuration.setText(String.valueOf(duration/60));
            spDurationUnit.setSelection(Arrays.asList(arrDurationUnits).indexOf("minute(s)"));
        }
        else {
            etDuration.setText(String.valueOf(duration));
            spDurationUnit.setSelection(Arrays.asList(arrDurationUnits).indexOf("seconds(s)"));
        }

        adBuilder.setNegativeButton(R.string.fragment_dashboard_today_edit_habit_prompt_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adBuilder.setNeutralButton(R.string.fragment_dashboard_today_edit_habit_prompt_neutral_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deleteHabit(habit);

                dialog.dismiss();
            }
        });

        adBuilder.setPositiveButton(R.string.fragment_dashboard_today_edit_habit_prompt_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int duration = 0;
                if (isSwitchChecked[0]) {
                    duration = Integer.parseInt(etDuration.getText().toString());
                    switch(spDurationUnit.getSelectedItem().toString()) {
                        case "hour(s)":
                            duration *= 60 * 60;
                            break;
                        case "minute(s)":
                            duration *= 60;
                            break;
                        default:
                            break;
                    }
                }

                habit.setName(etName.getText().toString());
                habit.setTime(String.format("%02d", tpTime.getHour()) + ":" + String.format("%02d", tpTime.getMinute()));
                habit.setAlarmOn(cbAlarm.isChecked());
                habit.setDuration(duration);
                habit.setTimerOn(cbTimer.isChecked());
                habit.setStatus(0);

                mPresenter.updateHabit(habit);

                dialog.dismiss();
            }
        });

        adBuilder.create().show();
    }

    private void onAddHabitTapped() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(mContext);

        adBuilder.setTitle(R.string.fragment_dashboard_today_add_habit_prompt_title);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View viewAlertDialog = inflater.inflate(R.layout.dialog_add_habit, null);
        adBuilder.setView(viewAlertDialog);

        final EditText etName = viewAlertDialog.findViewById(R.id.dialog_add_habit_et_name);
        final TimePicker tpTime = viewAlertDialog.findViewById(R.id.dialog_add_habit_tp_time);
        final CheckBox cbAlarm = viewAlertDialog.findViewById(R.id.dialog_add_habit_cb_alarm);
        final Switch swDuration = viewAlertDialog.findViewById(R.id.dialog_add_habit_sw_duration);
        final EditText etDuration = viewAlertDialog.findViewById(R.id.dialog_add_habit_et_duration);
        final Spinner spDurationUnit = viewAlertDialog.findViewById(R.id.dialog_add_habit_sp_duration_unit);
        final TextView etTimer = viewAlertDialog.findViewById(R.id.dialog_add_habit_tv_timer);
        final CheckBox cbTimer = viewAlertDialog.findViewById(R.id.dialog_add_habit_cb_timer);

        tpTime.setIs24HourView(true);

        final boolean[] isSwitchChecked = {false};
        swDuration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etDuration.setEnabled(true);
                    etDuration.setVisibility(View.VISIBLE);
                    etDuration.setText("0");
                    spDurationUnit.setEnabled(true);
                    spDurationUnit.setVisibility(View.VISIBLE);
                    etTimer.setEnabled(true);
                    etTimer.setVisibility(View.VISIBLE);
                    cbTimer.setEnabled(true);
                    cbTimer.setVisibility(View.VISIBLE);
                    cbTimer.setChecked(false);
                    isSwitchChecked[0] = true;
                }
                else {
                    etDuration.setEnabled(false);
                    etDuration.setVisibility(View.GONE);
                    spDurationUnit.setEnabled(false);
                    spDurationUnit.setVisibility(View.GONE);
                    etTimer.setEnabled(false);
                    etTimer.setVisibility(View.GONE);
                    cbTimer.setEnabled(false);
                    cbTimer.setVisibility(View.GONE);
                    isSwitchChecked[0] = false;
                }
            }
        });

        adBuilder.setNegativeButton(R.string.fragment_dashboard_today_add_habit_prompt_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adBuilder.setPositiveButton(R.string.fragment_dashboard_today_add_habit_prompt_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int duration = 0;
                if (isSwitchChecked[0]) {
                    duration = Integer.parseInt(etDuration.getText().toString());
                    switch(spDurationUnit.getSelectedItem().toString()) {
                        case "hour(s)":
                            duration *= 60 * 60;
                            break;
                        case "minute(s)":
                            duration *= 60;
                            break;
                        default:
                            break;
                    }
                }

                mPresenter.insertHabit(etName.getText().toString(), String.format("%02d", tpTime.getHour()) + ":" + String.format("%02d", tpTime.getMinute()), cbAlarm.isChecked(), duration, cbTimer.isChecked());

                dialog.dismiss();
            }
        });

        adBuilder.create().show();
    }

    private void onTaskTapped(String taskName) {
        final Task task = mPresenter.getTask(taskName);
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(mContext);

        adBuilder.setTitle(R.string.fragment_dashboard_today_edit_task_prompt_title);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View viewAlertDialog = inflater.inflate(R.layout.dialog_edit_task, null);
        adBuilder.setView(viewAlertDialog);

        final EditText etName = viewAlertDialog.findViewById(R.id.dialog_edit_task_et_name);
        final EditText etDescription = viewAlertDialog.findViewById(R.id.dialog_edit_task_et_description);
        final Switch swDueDate = viewAlertDialog.findViewById(R.id.dialog_edit_task_sw_due_date);
        final DatePicker dpDueDate = viewAlertDialog.findViewById(R.id.dialog_edit_task_dp_due_date);
        final TextView tvAlarmOnDue = viewAlertDialog.findViewById(R.id.dialog_edit_task_tv_alarm_on_due);
        final CheckBox cbAlarmOnDue = viewAlertDialog.findViewById(R.id.dialog_edit_task_cb_alarm_on_due);
        final Switch swStartDate = viewAlertDialog.findViewById(R.id.dialog_edit_task_sw_start_date);
        final DatePicker dpStartDate = viewAlertDialog.findViewById(R.id.dialog_edit_task_dp_start_date);
        final TextView tvAlarmOnStart = viewAlertDialog.findViewById(R.id.dialog_edit_task_tv_alarm_on_start);
        final CheckBox cbAlarmOnStart = viewAlertDialog.findViewById(R.id.dialog_edit_task_cb_alarm_on_start);
        final Spinner spParent = viewAlertDialog.findViewById(R.id.dialog_edit_task_sp_parent);
        final Spinner spColor = viewAlertDialog.findViewById(R.id.dialog_edit_task_sp_color);
        final SeekBar sbProgress = viewAlertDialog.findViewById(R.id.dialog_edit_task_sb_progress);
        final TextView tvPercent = viewAlertDialog.findViewById(R.id.dialog_edit_task_tv_percent);

        etName.setText(task.getName());

        etDescription.setText(task.getDescription());

        final boolean[] isDueDateSwitchChecked = {false};
        swDueDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dpDueDate.setEnabled(true);
                    dpDueDate.setVisibility(View.VISIBLE);
                    tvAlarmOnDue.setEnabled(true);
                    tvAlarmOnDue.setVisibility(View.VISIBLE);
                    cbAlarmOnDue.setEnabled(true);
                    cbAlarmOnDue.setVisibility(View.VISIBLE);
                    isDueDateSwitchChecked[0] = true;
                }
                else {
                    dpDueDate.setEnabled(false);
                    dpDueDate.setVisibility(View.GONE);
                    tvAlarmOnDue.setEnabled(false);
                    tvAlarmOnDue.setVisibility(View.GONE);
                    cbAlarmOnDue.setEnabled(false);
                    cbAlarmOnDue.setVisibility(View.GONE);
                    isDueDateSwitchChecked[0] = false;
                }
            }
        });

        final boolean[] isStartDateSwitchChecked = {false};
        swStartDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dpStartDate.setEnabled(true);
                    dpStartDate.setVisibility(View.VISIBLE);
                    tvAlarmOnStart.setEnabled(true);
                    tvAlarmOnStart.setVisibility(View.VISIBLE);
                    cbAlarmOnStart.setEnabled(true);
                    cbAlarmOnStart.setVisibility(View.VISIBLE);
                    isStartDateSwitchChecked[0] = true;
                }
                else {
                    dpStartDate.setEnabled(false);
                    dpStartDate.setVisibility(View.GONE);
                    tvAlarmOnStart.setEnabled(false);
                    tvAlarmOnStart.setVisibility(View.GONE);
                    cbAlarmOnStart.setEnabled(false);
                    cbAlarmOnStart.setVisibility(View.GONE);
                    isStartDateSwitchChecked[0] = false;
                }
            }
        });

        String[] dueDate = task.getDueDate().split("/");
        if(dueDate.length == 1) {
            swDueDate.setChecked(false);
        }
        else {
            swDueDate.setChecked(true);
            dpDueDate.updateDate(Integer.parseInt(dueDate[0]), Integer.parseInt(dueDate[1]) - 1, Integer.parseInt(dueDate[2]));
        }

        cbAlarmOnDue.setChecked(task.isAlarmOnDue());

        String[] startDate = task.getStartDate().split("/");
        if(startDate.length == 1) {
            swStartDate.setChecked(false);
        }
        else {
            swStartDate.setChecked(true);
            dpStartDate.updateDate(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1]) - 1, Integer.parseInt(startDate[2]));
        }

        cbAlarmOnStart.setChecked(task.isAlarmOnStart());

        String[] arrParents = mContext.getResources().getStringArray(R.array.dialog_edit_task_sp_parent_items);
        spParent.setSelection(Arrays.asList(arrParents).indexOf(task.getParent()));

        String[] arrColors = mContext.getResources().getStringArray(R.array.dialog_edit_task_sp_color_items);
        spColor.setSelection(Arrays.asList(arrColors).indexOf(task.getColor()));

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPercent.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbProgress.setProgress(task.getProgress());

        adBuilder.setNegativeButton(R.string.fragment_dashboard_today_edit_habit_prompt_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adBuilder.setNeutralButton(R.string.fragment_dashboard_today_edit_habit_prompt_neutral_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deleteTask(task);

                dialog.dismiss();
            }
        });

        adBuilder.setPositiveButton(R.string.fragment_dashboard_today_edit_habit_prompt_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskDueDate = "";
                if (isDueDateSwitchChecked[0]) {
                    taskDueDate = String.format("%04d", dpDueDate.getYear()) + "/" + String.format("%02d", dpDueDate.getMonth() + 1) + "/" + String.format("%02d", dpDueDate.getDayOfMonth());
                }

                String taskStartDate = "";
                if (isStartDateSwitchChecked[0]) {
                    taskStartDate = String.format("%04d", dpStartDate.getYear()) + "/" + String.format("%02d", dpStartDate.getMonth() + 1) + "/" + String.format("%02d", dpStartDate.getDayOfMonth());
                }

                task.setName(etName.getText().toString());
                task.setDescription(etDescription.getText().toString());
                task.setDueDate(taskDueDate);
                task.setAlarmOnDue(cbAlarmOnDue.isChecked());
                task.setStartDate(taskStartDate);
                task.setAlarmOnStart(cbAlarmOnStart.isChecked());
                task.setParent(spParent.getSelectedItem().toString());
                task.setColor(spColor.getSelectedItem().toString());
                task.setProgress(sbProgress.getProgress());

                mPresenter.updateTask(task);
                mPresenter.onTasksTapped();

                dialog.dismiss();
            }
        });

        adBuilder.create().show();
    }

    private void onAddTaskTapped() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(mContext);

        adBuilder.setTitle(R.string.fragment_dashboard_today_add_task_prompt_title);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View viewAlertDialog = inflater.inflate(R.layout.dialog_add_task, null);
        adBuilder.setView(viewAlertDialog);

        final EditText etName = viewAlertDialog.findViewById(R.id.dialog_add_task_et_name);
        final EditText etDescription = viewAlertDialog.findViewById(R.id.dialog_add_task_et_description);
        final Switch swDueDate = viewAlertDialog.findViewById(R.id.dialog_add_task_sw_due_date);
        final DatePicker dpDueDate = viewAlertDialog.findViewById(R.id.dialog_add_task_dp_due_date);
        final TextView tvAlarmOnDue = viewAlertDialog.findViewById(R.id.dialog_add_task_tv_alarm_on_due);
        final CheckBox cbAlarmOnDue = viewAlertDialog.findViewById(R.id.dialog_add_task_cb_alarm_on_due);
        final Switch swStartDate = viewAlertDialog.findViewById(R.id.dialog_add_task_sw_start_date);
        final DatePicker dpStartDate = viewAlertDialog.findViewById(R.id.dialog_add_task_dp_start_date);
        final TextView tvAlarmOnStart = viewAlertDialog.findViewById(R.id.dialog_add_task_tv_alarm_on_start);
        final CheckBox cbAlarmOnStart = viewAlertDialog.findViewById(R.id.dialog_add_task_cb_alarm_on_start);
        final Spinner spParent = viewAlertDialog.findViewById(R.id.dialog_add_task_sp_parent);
        final Spinner spColor = viewAlertDialog.findViewById(R.id.dialog_add_task_sp_color);
        final SeekBar sbProgress = viewAlertDialog.findViewById(R.id.dialog_add_task_sb_progress);
        final TextView tvPercent = viewAlertDialog.findViewById(R.id.dialog_add_task_tv_percent);

        final boolean[] isDueDateSwitchChecked = {false};
        swDueDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dpDueDate.setEnabled(true);
                    dpDueDate.setVisibility(View.VISIBLE);
                    tvAlarmOnDue.setEnabled(true);
                    tvAlarmOnDue.setVisibility(View.VISIBLE);
                    cbAlarmOnDue.setEnabled(true);
                    cbAlarmOnDue.setVisibility(View.VISIBLE);
                    isDueDateSwitchChecked[0] = true;
                }
                else {
                    dpDueDate.setEnabled(false);
                    dpDueDate.setVisibility(View.GONE);
                    tvAlarmOnDue.setEnabled(false);
                    tvAlarmOnDue.setVisibility(View.GONE);
                    cbAlarmOnDue.setEnabled(false);
                    cbAlarmOnDue.setVisibility(View.GONE);
                    isDueDateSwitchChecked[0] = false;
                }
            }
        });

        final boolean[] isStartDateSwitchChecked = {false};
        swStartDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dpStartDate.setEnabled(true);
                    dpStartDate.setVisibility(View.VISIBLE);
                    tvAlarmOnStart.setEnabled(true);
                    tvAlarmOnStart.setVisibility(View.VISIBLE);
                    cbAlarmOnStart.setEnabled(true);
                    cbAlarmOnStart.setVisibility(View.VISIBLE);
                    isStartDateSwitchChecked[0] = true;
                }
                else {
                    dpStartDate.setEnabled(false);
                    dpStartDate.setVisibility(View.GONE);
                    tvAlarmOnStart.setEnabled(false);
                    tvAlarmOnStart.setVisibility(View.GONE);
                    cbAlarmOnStart.setEnabled(false);
                    cbAlarmOnStart.setVisibility(View.GONE);
                    isStartDateSwitchChecked[0] = false;
                }
            }
        });

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPercent.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbProgress.setProgress(0);
        tvPercent.setText("0%");

        adBuilder.setNegativeButton(R.string.fragment_dashboard_today_add_habit_prompt_negative_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adBuilder.setPositiveButton(R.string.fragment_dashboard_today_add_habit_prompt_positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskDueDate = "";
                if (isDueDateSwitchChecked[0]) {
                    taskDueDate = String.format("%04d", dpDueDate.getYear()) + "/" + String.format("%02d", dpDueDate.getMonth() + 1) + "/" + String.format("%02d", dpDueDate.getDayOfMonth());
                }

                String taskStartDate = "";
                if (isStartDateSwitchChecked[0]) {
                    taskStartDate = String.format("%04d", dpStartDate.getYear()) + "/" + String.format("%02d", dpStartDate.getMonth() + 1) + "/" + String.format("%02d", dpStartDate.getDayOfMonth());
                }

                mPresenter.insertTask(
                        etName.getText().toString(),
                        etDescription.getText().toString(),
                        taskDueDate,
                        cbAlarmOnDue.isChecked(),
                        taskStartDate,
                        cbAlarmOnStart.isChecked(),
                        spParent.getSelectedItem().toString(),
                        spColor.getSelectedItem().toString(),
                        sbProgress.getProgress()
                );

                dialog.dismiss();
            }
        });

        adBuilder.create().show();
    }

    @Override
    public void resetView() {
        isHabitsSelected = false;
        isTasksSelected = false;
        toggleHabitsStatus(false);
        toggleTasksStatus(false);
    }

    @Override
    public void toggleHabitsStatus(boolean selected) {
        isHabitsSelected = selected;
        if(selected) {
            ivHabits.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.textPrimary)));
            tvHabits.setTextColor(ContextCompat.getColor(mContext, R.color.textPrimary));
        }
        else {
            ivHabits.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.backgroundDark)));
            tvHabits.setTextColor(ContextCompat.getColor(mContext, R.color.backgroundDark));
        }
    }

    @Override
    public void toggleTasksStatus(boolean selected) {
        isTasksSelected = selected;
        if(selected) {
            ivTasks.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.textPrimary)));
            tvTasks.setTextColor(ContextCompat.getColor(mContext, R.color.textPrimary));
        }
        else {
            ivTasks.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.backgroundDark)));
            tvTasks.setTextColor(ContextCompat.getColor(mContext, R.color.backgroundDark));
        }
    }

    @Override
    public void updateHabits(ArrayList<Habit> alHabits) {
        Log.d(className, "Updating Habits");

        if (lvaHabits != null) {
            lvaHabits.clear();
        }
        lvaHabits = new ListViewAdapterHabits(alHabits);

        lvDetails.setAdapter(lvaHabits);
    }

    @Override
    public void updateTasks(ArrayList<Task> alTasks) {
        ListViewAdapterTasks lvaTasks = new ListViewAdapterTasks(alTasks);
        lvDetails.setAdapter(lvaTasks);
    }

    @Override
    public void displayShortToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private class ListViewAdapterHabits extends ArrayAdapter<Habit> {
        private ListViewAdapterHabits(ArrayList<Habit> alHabits) {
            super(mContext, 0, alHabits);
        }

        @NonNull
        @Override
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            final Habit habit = getItem(position);
            Log.d(className, habit.getName() + ": " + Integer.toString(habit.getStatus()));
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_dashboard_today_habit, parent, false);
            }

            TextView tvTime = view.findViewById(R.id.item_dashboard_today_habit_tv_time);
            LinearLayout llItem = view.findViewById(R.id.item_dashboard_today_habit_ll_item);
            final TextView tvName = view.findViewById(R.id.item_dashboard_today_habit_tv_name);
            TextView tvDuration = view.findViewById(R.id.item_dashboard_today_habit_tv_duration);
            final ImageView ivAlarm = view.findViewById(R.id.item_dashboard_today_habit_iv_alarm);
            final ImageView ivTimer = view.findViewById(R.id.item_dashboard_today_habit_iv_timer);
            final CheckBox cbItem = view.findViewById(R.id.item_dashboard_today_habit_cb_item);

            tvTime.setText(habit.getTime());

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onHabitTapped(tvName.getText().toString());
                }
            });

            tvName.setText(habit.getName());

            int duration = habit.getDuration();
            if (duration == -1 || duration == 0) {
                tvDuration.setText("");
            }
            else if (duration % 3600 == 0) {
                duration /= 3600;
                String text = String.valueOf(duration) + " hour";
                if (duration != 1) {
                    text = text + "s";
                }
                tvDuration.setText(text);
            }
            else if (duration % 60 == 0) {
                duration /= 60;
                String text = String.valueOf(duration) + " minute";
                if (duration != 1) {
                    text = text + "s";
                }
                tvDuration.setText(text);
            }
            else {
                String text = String.valueOf(duration) + " second";
                if (duration != 1) {
                    text = text + "s";
                }
                tvDuration.setText(text);
            }

            if (habit.isAlarmOn()) {
                ivAlarm.setVisibility(View.VISIBLE);
            }
            else {
                ivAlarm.setVisibility(View.INVISIBLE);
            }

            if (habit.isTimerOn()) {
                ivTimer.setVisibility(View.VISIBLE);
            }
            else {
                ivTimer.setVisibility(View.INVISIBLE);
            }

            if (habit.getStatus() == 1) {
                cbItem.setChecked(true);
            }
            else {
                cbItem.setChecked(false);
            }

            cbItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!cbItem.isChecked()) {
                        cbItem.setChecked(false);
                        habit.setStatus(0);
                        mPresenter.updateHabit(habit);
                        Log.d(className, habit.getName() + ": Unchecked");
                    }
                    else {
                        cbItem.setChecked(true);
                        habit.setStatus(1);
                        mPresenter.updateHabit(habit);
                        Log.d(className, habit.getName() + ": Checked");
                    }
                }
            });

            return view;
        }
    }

    private class ListViewAdapterTasks extends ArrayAdapter<Task> {
        private ListViewAdapterTasks(ArrayList<Task> alTasks) {
            super(mContext, 0, alTasks);
        }

        @NonNull
        @Override
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            final Task task = getItem(position);
            Log.d(className, task.getName());

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_dashboard_today_task, parent, false);
            }

            TextView tvProgress = view.findViewById(R.id.item_dashboard_today_task_tv_progress);
            ConstraintLayout clItem = view.findViewById(R.id.item_dashboard_today_task_cl_item);
            final TextView tvName = view.findViewById(R.id.item_dashboard_today_task_tv_name);
            TextView tvDescription = view.findViewById(R.id.item_dashboard_today_task_tv_description);
            TextView tvStartDate = view.findViewById(R.id.item_dashboard_today_task_tv_start_date);
            TextView tvDueDate = view.findViewById(R.id.item_dashboard_today_task_tv_due_date);

            tvProgress.setText(Integer.toString(task.getProgress()) + "%");

            int color = getContext().getColor(R.color.primaryDark);
            switch(task.getColor()) {
                case "Gray":
                    color = getContext().getColor(R.color.gray600);
                    break;
                case "Red":
                    color = ColorUtils.blendARGB(getContext().getColor(R.color.red500), getContext().getColor(R.color.gray600), 0.5F);
                    break;
                case "Blue":
                    color = ColorUtils.blendARGB(getContext().getColor(R.color.blue800), getContext().getColor(R.color.gray600), 0.5F);
                    break;
                case "Orange":
                    color = ColorUtils.blendARGB(getContext().getColor(R.color.yellow900), getContext().getColor(R.color.gray600), 0.5F);
                    break;
                case "Green":
                    color = ColorUtils.blendARGB(getContext().getColor(R.color.green700), getContext().getColor(R.color.gray600), 0.5F);
                    break;
            }

            clItem.setBackgroundTintList(ColorStateList.valueOf(color));

            clItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTaskTapped(tvName.getText().toString());
                }
            });

            tvName.setText(task.getName());

            tvDescription.setText(task.getDescription());

            String[] startDate = task.getStartDate().split("/");
            if (startDate.length > 1) {
                boolean isOngoing = false;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date dateStart = sdf.parse(task.getStartDate());
                    if (new Date().after(dateStart)) {
                        isOngoing = true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (isOngoing) {
                    tvStartDate.setText("Ongoing");
                }
                else {
                    String month = "";
                    switch (startDate[1]) {
                        case "01":
                            month = "Jan";
                            break;
                        case "02":
                            month = "Feb";
                            break;
                        case "03":
                            month = "Mar";
                            break;
                        case "04":
                            month = "Apr";
                            break;
                        case "05":
                            month = "May";
                            break;
                        case "06":
                            month = "Jun";
                            break;
                        case "07":
                            month = "Jul";
                            break;
                        case "08":
                            month = "Aug";
                            break;
                        case "09":
                            month = "Sep";
                            break;
                        case "10":
                            month = "Oct";
                            break;
                        case "11":
                            month = "Nov";
                            break;
                        case "12":
                            month = "Dec";
                            break;
                    }
                    tvStartDate.setText("Starts " + startDate[2] + " " + month + ", " + startDate[0]);
                }
            }
            else {
                tvStartDate.setText("No Start Date");
            }

            String[] dueDate = task.getDueDate().split("/");
            if (dueDate.length > 1) {
                String month = "";
                switch (dueDate[1]) {
                    case "01":
                        month = "Jan";
                        break;
                    case "02":
                        month = "Feb";
                        break;
                    case "03":
                        month = "Mar";
                        break;
                    case "04":
                        month = "Apr";
                        break;
                    case "05":
                        month = "May";
                        break;
                    case "06":
                        month = "Jun";
                        break;
                    case "07":
                        month = "Jul";
                        break;
                    case "08":
                        month = "Aug";
                        break;
                    case "09":
                        month = "Sep";
                        break;
                    case "10":
                        month = "Oct";
                        break;
                    case "11":
                        month = "Nov";
                        break;
                    case "12":
                        month = "Dec";
                        break;
                }
                tvDueDate.setText("Due " + dueDate[2] + " " + month + ", " + dueDate[0]);
            }
            else {
                tvDueDate.setText("No Due Date");
            }

            return view;
        }
    }
}
