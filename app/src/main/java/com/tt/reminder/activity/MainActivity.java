package com.tt.reminder.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tt.reminder.R;
import com.tt.reminder.fragment.TasksContainWithDrawerViewFragment;
import com.tt.sharedbaseclass.listener.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static long INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK = 500;
    private static int IS_SEED_FRAGMENT = 0;
    private static int mFragmentCounter = 0;
    private FrameLayout mMainActivityFrameLayout;
    private TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;
    private long mFirstBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            //do something
        }
        mMainActivityFrameLayout = (FrameLayout) findViewById(R.id.main_activity_frame_layout);
        onCreated();
    }

    private void onCreated() {
        initMainActivityFragment();
    }

    private void initMainActivityFragment() {

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mTasksContainWithDrawerViewFragment = TasksContainWithDrawerViewFragment.newInstance();
        transaction.add(R.id.main_activity_frame_layout, mTasksContainWithDrawerViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentCounter == IS_SEED_FRAGMENT) {
            if (mFirstBackPressedTime == 0 || (System.currentTimeMillis() - mFirstBackPressedTime) >= INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK) {
                mFirstBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, R.string.toast_double_click_to_exit, Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        } else {
            mFragmentCounter--;
            super.onBackPressed();
        }
    }

    public static void navigateTo(android.app.Fragment fragment, android.app.FragmentManager fragmentManager) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_in_bottom,R.animator.fragment_slide_out_top);
        fragmentTransaction.replace(R.id.main_activity_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mFragmentCounter++;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
