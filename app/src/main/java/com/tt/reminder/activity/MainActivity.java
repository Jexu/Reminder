package com.tt.reminder.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tt.reminder.R;
import com.tt.reminder.fragment.TasksContaintWithDrawerViewFragment;
import com.tt.sharedutils.DeviceUtil;

public class MainActivity extends AppCompatActivity implements TasksContaintWithDrawerViewFragment.OnFragmentInteractionListener{

    private FrameLayout mMainActivityFrameLayout;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private TasksContaintWithDrawerViewFragment mTasksContaintWithDrawerViewFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            //do something
        }
        mMainActivityFrameLayout = (FrameLayout) findViewById(R.id.main_activity_frame_layout);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        onCreated();
    }

    private void onCreated() {
        initMainActivityFragment();
    }

    private void initMainActivityFragment() {

        mTasksContaintWithDrawerViewFragment = new TasksContaintWithDrawerViewFragment();
        mTransaction.add( R.id.main_activity_frame_layout, mTasksContaintWithDrawerViewFragment );
        mTransaction.addToBackStack(null);
        mTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {

    }
}
