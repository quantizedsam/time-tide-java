package com.quantizedsam.timetide.presenters;

import android.content.Context;

import com.quantizedsam.timetide.interfaces.InterfaceDashboard;

public class PresenterDashboard implements InterfaceDashboard.Presenter {

    // dev classes
    private InterfaceDashboard.View mView;

    // native classes
    private Context mContext;

    // constants
    private String className = getClass().getCanonicalName();

    public PresenterDashboard (InterfaceDashboard.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void initialize() {
        mView.initView();

        updateView();
    }

    private void updateView() {
    }

}
