package com.tt.reminder.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tt.reminder.R;
import com.tt.reminder.fragment.TasksContainWithDrawerViewFragment;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.listener.OnFragmentInteractionListener;
import com.tt.sharedbaseclass.fragment.RenderFragmentBase;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;
    private RenderFragmentBase mSelectedFragment;

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
        transaction.add(R.id.main_activity_frame_layout, mTasksContainWithDrawerViewFragment
                , Constant.FRAGMENT_TYPE.TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT.toString());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mSelectedFragment != null) {
            mSelectedFragment.onBackPressed();
            mSelectedFragment = null;
        } else {
            if (!mTasksContainWithDrawerViewFragment.onBackPressed())
            super.onBackPressed();
        }
    }

    public static void navigateTo(android.app.Fragment fragment, android.app.FragmentManager fragmentManager) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_in_bottom,R.animator.fragment_slide_out_top);
        Fragment f = fragmentManager.findFragmentByTag(Constant.FRAGMENT_TYPE.TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT.toString());
        if (f != null && f.isAdded()) {
            fragmentTransaction.hide(f);
        }
        fragmentTransaction.add(R.id.main_activity_frame_layout, fragment, fragment.getTag());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void navigateToForResultCode(android.app.Fragment fragment, android.app.FragmentManager fragmentManager, int requestCode) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_in_bottom,R.animator.fragment_slide_out_top);
        Fragment f = fragmentManager.findFragmentByTag(Constant.FRAGMENT_TYPE.TASKS_CONTAIN_WITH_DRAWER_VIEW_FRAGMENT.toString());
        if (f != null && f.isAdded()) {
            fragmentTransaction.hide(f);
        }
        ((FragmentBaseWithSharedHeaderView)fragment).navigateToFragmentForResultCode(mTasksContainWithDrawerViewFragment, requestCode);
        fragmentTransaction.add(R.id.main_activity_frame_layout, fragment, fragment.getTag());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentSelected(RenderFragmentBase context) {
        mSelectedFragment = context;
    }

}
