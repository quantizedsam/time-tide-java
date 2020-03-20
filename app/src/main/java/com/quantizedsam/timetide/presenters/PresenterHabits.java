package com.quantizedsam.timetide.presenters;

import android.content.Context;

import com.quantizedsam.timetide.databases.DBHabits;
import com.quantizedsam.timetide.interfaces.InterfaceHabits;
import com.quantizedsam.timetide.models.Habit;

import java.util.ArrayList;

public class PresenterHabits implements InterfaceHabits.Presenter {

    // dev classes
    private InterfaceHabits.View mView;
    private ArrayList<Habit> mHabits;
    private DBHabits mDBHabits;

    // native classes
    private Context mContext;

    // constants
    private String className = getClass().getCanonicalName();

    public PresenterHabits (InterfaceHabits.View view, Context context) {
        mView = view;
        mContext = context;
        mDBHabits = new DBHabits(mContext);
    }

    @Override
    public void initialize() {
        resetViews();
        mHabits = mDBHabits.getHabits();
        mView.updateHabits(mHabits);
    }

    private void resetViews() {
        mView.resetView();
    }

}
