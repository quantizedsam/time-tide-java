package com.quantizedsam.timetide.interfaces;

public interface InterfaceDashboard {

    interface Presenter {
        void initialize();
    }

    interface View {
        void initView();
        void resetView();

        void displayShortToast(String message);
    }

}