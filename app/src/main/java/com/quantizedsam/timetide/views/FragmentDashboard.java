package com.quantizedsam.timetide.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quantizedsam.timetide.R;
import com.quantizedsam.timetide.interfaces.InterfaceDashboard;
import com.quantizedsam.timetide.presenters.PresenterDashboard;

import java.util.ArrayList;
import java.util.List;

public class FragmentDashboard extends Fragment implements InterfaceDashboard.View {

    // dev classes
    private InterfaceDashboard.Presenter mPresenter;

    // UI elements
    private View mFragment;

    // native classes
    private Context mContext;

    // constants
    private String className = getClass().getCanonicalName();

    public FragmentDashboard() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mContext = getContext();

        mPresenter = new PresenterDashboard(this, mContext);
        mPresenter.initialize();

        return mFragment;
    }

    @Override
    public void initView() {

        ViewPager viewPager = mFragment.findViewById(R.id.fragment_dashboard_vp);
        TabsAdapter tabsAdapter = new TabsAdapter(getChildFragmentManager());
        tabsAdapter.addFragment(new FragmentDashboardToday(), "Today");
        //tabsAdapter.addFragment(new FragmentDashboardWeek(), "Week");
        viewPager.setAdapter(tabsAdapter);

        TabLayout tabLayout = mFragment.findViewById(R.id.fragment_dashboard_tl);
        tabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.textSecondary)));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void resetView() {

    }

    @Override
    public void displayShortToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


    static class TabsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private TabsAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
