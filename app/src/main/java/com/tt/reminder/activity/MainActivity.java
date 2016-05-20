package com.tt.reminder.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.tt.reminder.R;
import com.tt.reminder.fragment.TasksContainWithDrawerViewFragment;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.listener.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static long INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK = 500;
    private long mFirstBackPressedTime = 0;

    private TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;
    private FragmentBaseWithSharedHeaderView mSelectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            //do something
        }
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
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            if (mSelectedFragment != null) {
                mSelectedFragment.onBackPressed();
            } else {
                super.onBackPressed();
            }
        } else {
            if (mFirstBackPressedTime == 0
              || System.currentTimeMillis() - mFirstBackPressedTime >= INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK) {
                mFirstBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, R.string.toast_double_click_to_exit, Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    public static void navigateTo(android.app.Fragment fragment, android.app.FragmentManager fragmentManager) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_in_bottom,R.animator.fragment_slide_out_top);
        fragmentTransaction.add(R.id.main_activity_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentSelected(FragmentBaseWithSharedHeaderView context) {
        mSelectedFragment = context;
    }
}
