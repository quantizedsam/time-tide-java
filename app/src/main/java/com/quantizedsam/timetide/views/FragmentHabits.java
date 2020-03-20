package com.quantizedsam.timetide.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quantizedsam.timetide.R;
import com.quantizedsam.timetide.interfaces.InterfaceHabits;
import com.quantizedsam.timetide.models.Habit;
import com.quantizedsam.timetide.presenters.PresenterHabits;

import java.util.ArrayList;

public class FragmentHabits extends Fragment implements InterfaceHabits.View {

    // dev classes
    private InterfaceHabits.Presenter mPresenter;

    // UI elements
    private View mFragment;
    private ListView lvDetails;

    // native classes
    private Context mContext;

    // constants
    private String className = getClass().getCanonicalName();

    public FragmentHabits() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_habits, container, false);
        mContext = getContext();

        initView();

        mPresenter = new PresenterHabits(this, mContext);
        mPresenter.initialize();

        return mFragment;
    }

    @Override
    public void initView() {
        lvDetails = mFragment.findViewById(R.id.fragment_habits_lv_details);
    }

    @Override
    public void resetView() {

    }

    @Override
    public void updateHabits(ArrayList<Habit> alHabits) {
        ListViewAdapterHabits lvaHabits = new ListViewAdapterHabits(alHabits);
        lvDetails.setAdapter(lvaHabits);
    }

    private void onItemClicked(View view, int position) {

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
            Habit habit = getItem(position);

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_habits_habit, parent, false);
            }

            LinearLayout llItem = view.findViewById(R.id.item_habits_habit_ll_item);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked(v, position);
                }
            });

            TextView tvName = view.findViewById(R.id.item_habits_habit_tv_name);
            TextView tvDuration = view.findViewById(R.id.item_habits_habit_tv_duration);
            if (habit != null) {
                tvName.setText(habit.getName());
                int duration = habit.getDuration();
                if (duration == -1 || duration == 0) {
                    tvDuration.setText("");
                    tvDuration.setVisibility(View.GONE);
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
            }

            return view;
        }
    }
}
