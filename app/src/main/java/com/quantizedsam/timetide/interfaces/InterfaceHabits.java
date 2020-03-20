package com.quantizedsam.timetide.interfaces;

import com.quantizedsam.timetide.models.Habit;

import java.util.ArrayList;

public interface InterfaceHabits {

    interface Presenter {
        void initialize();
    }

    interface View {
        void initView();
        void resetView();

        void updateHabits(ArrayList<Habit> alHabits);

        void displayShortToast(String message);
    }
}
