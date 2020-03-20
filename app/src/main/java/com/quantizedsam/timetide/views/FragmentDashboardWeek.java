package com.quantizedsam.timetide.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quantizedsam.timetide.R;
import com.quantizedsam.timetide.interfaces.InterfaceDashboardWeek;
import com.quantizedsam.timetide.presenters.PresenterDashboardWeek;

import java.util.ArrayList;

public class FragmentDashboardWeek extends Fragment implements InterfaceDashboardWeek.View {

    // dev classes
    private InterfaceDashboardWeek.Presenter mPresenter;

    // UI elements
    private View mFragment;
    private LinearLayout llHabits, llTasks, llAlarms;
    private ImageView ivHabits, ivTasks, ivAlarms;
    private TextView tvHabits, tvTasks, tvAlarms;
    private ListView lvDetails;

    // native classes
    private Context mContext;
    private Activity mActivity;

    // constants
    private String className = getClass().getCanonicalName();

    public FragmentDashboardWeek() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_dashboard_weekly, viewGroup, false);
        mContext = getContext();
        mActivity = getActivity();

        initView();

        mPresenter = new PresenterDashboardWeek(this, mContext);
        mPresenter.initialize();

        return mFragment;
    }

    @Override
    public void initView() {
        llHabits = mFragment.findViewById(R.id.fragment_dashboard_weekly_ll_habits);
        ivHabits = mFragment.findViewById(R.id.fragment_dashboard_weekly_iv_habits);
        tvHabits = mFragment.findViewById(R.id.fragment_dashboard_weekly_tv_habits);
        llHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onHabitsTapped();
            }
        });

        llTasks = mFragment.findViewById(R.id.fragment_dashboard_weekly_ll_tasks);
        ivTasks = mFragment.findViewById(R.id.fragment_dashboard_weekly_iv_tasks);
        tvTasks = mFragment.findViewById(R.id.fragment_dashboard_weekly_tv_tasks);
        llTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onTasksTapped();
            }
        });

        llAlarms = mFragment.findViewById(R.id.fragment_dashboard_weekly_ll_alarms);
        ivAlarms = mFragment.findViewById(R.id.fragment_dashboard_weekly_iv_alarms);
        tvAlarms = mFragment.findViewById(R.id.fragment_dashboard_weekly_tv_alarms);
        llAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onAlarmsTapped();
            }
        });

        lvDetails = mFragment.findViewById(R.id.fragment_dashboard_weekly_lv_details);
    }

    @Override
    public void resetView() {
        toggleHabitsStatus(false);
        toggleTasksStatus(false);
        toggleAlarmsStatus(false);
    }

    @Override
    public void toggleHabitsStatus(boolean selected) {
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
    public void toggleAlarmsStatus(boolean selected) {
        if(selected) {
            ivAlarms.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.textPrimary)));
            tvAlarms.setTextColor(ContextCompat.getColor(mContext, R.color.textPrimary));
        }
        else {
            ivAlarms.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.backgroundDark)));
            tvAlarms.setTextColor(ContextCompat.getColor(mContext, R.color.backgroundDark));
        }
    }

    @Override
    public void updateHabits(ArrayList<ArrayList<String>> alHabitWeeklyProgresses) {
        lvDetails.setAdapter(new ListViewAdapterHabitWeeklyProgress(alHabitWeeklyProgresses));
    }

    @Override
    public void updateTasks(ArrayList<ArrayList<String>> alTasks) {
        lvDetails.setAdapter(new ListViewAdapterHabitWeeklyProgress(new ArrayList<ArrayList<String>>()));
    }

    @Override
    public void updateAlarms(ArrayList<ArrayList<String>> alAlarms) {
        lvDetails.setAdapter(new ListViewAdapterHabitWeeklyProgress(new ArrayList<ArrayList<String>>()));
    }

    @Override
    public void displayShortToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private class ListViewAdapterHabitWeeklyProgress extends ArrayAdapter<ArrayList<String>> {
        private ListViewAdapterHabitWeeklyProgress(ArrayList<ArrayList<String>> alHabitWeeklyProgresses) {
            super(mContext, 0, alHabitWeeklyProgresses);
        }

        @NonNull
        @Override
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            ArrayList<String> alHabitWeeklyProgress = getItem(position);

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_dashboard_week_habit, parent, false);
            }
            TextView tvName = view.findViewById(R.id.item_dashboard_week_habit_tv_name);
            tvName.setText(alHabitWeeklyProgress.get(0));

            ProgressBar pbSun = view.findViewById(R.id.item_dashboard_week_habit_pb_sun);
            pbSun.setMax(100);
            pbSun.setProgress(Integer.parseInt(alHabitWeeklyProgress.get(1)));

            ProgressBar pbMon = view.findViewById(R.id.item_dashboard_week_habit_pb_mon);
            pbMon.setMax(100);
            pbMon.setProgress(Integer.parseInt(alHabitWeeklyProgress.get(2)));

            ProgressBar pbTue = view.findViewById(R.id.item_dashboard_week_habit_pb_tue);
            pbTue.setMax(100);
            pbTue.setProgress(Integer.parseInt(alHabitWeeklyProgress.get(3)));

            ProgressBar pbWed = view.findViewById(R.id.item_dashboard_week_habit_pb_wed);
            pbWed.setMax(100);
            pbWed.setProgress(Integer.parseInt(alHabitWeeklyProgress.get(4)));

            ProgressBar pbThu = view.findViewById(R.id.item_dashboard_week_habit_pb_thu);
            pbThu.setMax(100);
            pbThu.setProgress(Integer.parseInt(alHabitWeeklyProgress.get(5)));

            ProgressBar pbFri = view.findViewById(R.id.item_dashboard_week_habit_pb_fri);
            pbFri.setMax(100);
            pbFri.setProgress(Integer.parseInt(alHabitWeeklyProgress.get(6)));

            ProgressBar pbSat = view.findViewById(R.id.item_dashboard_week_habit_pb_sat);
            pbSat.setMax(100);
            pbSat.setProgress(Integer.parseInt(alHabitWeeklyProgress.get(7)));

            return view;
        }
    }
}
