package com.tt.reminder.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.support.v7.app.AppCompatActivity;

import com.tt.reminder.R;
import com.tt.reminder.fragment.TasksContainWithDrawerViewFragment;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.listener.OnFragmentRegisterListener;
import com.tt.sharedbaseclass.fragment.RenderFragmentBase;

public class MainActivity extends AppCompatActivity implements OnFragmentRegisterListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;
    private static RenderFragmentBase mSelectedFragment;

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
                , mTasksContainWithDrawerViewFragment.getFragmentTag());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mSelectedFragment != null) {
            if (!mSelectedFragment.onBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    public static void navigateTo(RenderFragmentBase fragment, android.app.FragmentManager fragmentManager) {
        navigateToForResultCode(fragment, fragmentManager, Constant.BundelExtra.FINISH_REQUEST_CODE_DEFAULT);
    }

    public static void navigateToForResultCode(RenderFragmentBase fragment, android.app.FragmentManager fragmentManager, int requestCode) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_in_bottom,R.animator.fragment_slide_out_top);
        Fragment f = fragmentManager.findFragmentByTag(mSelectedFragment.getFragmentTag());
        if (f != null && f.isAdded()) {
            fragmentTransaction.hide(f);
        }
        fragment.navigateToFragmentForResultCode(mSelectedFragment, requestCode);
        fragmentTransaction.add(R.id.main_activity_frame_layout, fragment, fragment.getFragmentTag());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentRegistered(RenderFragmentBase context) {
        mSelectedFragment = context;
    }

}
