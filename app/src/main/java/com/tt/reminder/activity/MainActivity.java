package com.tt.reminder.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;

import com.tt.reminder.R;
import com.tt.reminder.fragment.TasksContainWithDrawerViewFragment;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.RenderFragmentBase;
import com.tt.sharedbaseclass.listener.OnFragmentRegisterListener;

public class MainActivity extends AppCompatActivity implements OnFragmentRegisterListener {

    private TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initMainActivityFragment() {

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //mTasksContainWithDrawerViewFragment = TasksContainWithDrawerViewFragment.newInstance();
        mTasksContainWithDrawerViewFragment = new TasksContainWithDrawerViewFragment();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null
          && bundle.getInt(Constant.BundelExtra.EXTRA_START_FROM) == Constant.BundelExtra.START_FROM_NOTIFICATION
          && bundle.getSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN)!= null) {

            Bundle args = mTasksContainWithDrawerViewFragment.getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putInt(Constant.BundelExtra.EXTRA_START_FROM, Constant.BundelExtra.START_FROM_NOTIFICATION);
            args.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, bundle.getSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN));
            mTasksContainWithDrawerViewFragment.setArguments(args);
            transaction.replace(R.id.main_activity_frame_layout, mTasksContainWithDrawerViewFragment
                    , mTasksContainWithDrawerViewFragment.getFragmentTag());
        } else {
            transaction.add(R.id.main_activity_frame_layout, mTasksContainWithDrawerViewFragment
                    , mTasksContainWithDrawerViewFragment.getFragmentTag());
        }
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mSelectedFragment != null) {
            if (!mSelectedFragment.onBackPressed()) {
                finish();
            }
        }
    }

    public static void navigateTo(RenderFragmentBase fragment, android.app.FragmentManager fragmentManager) {
        navigateToForResultCode(fragment, fragmentManager, Constant.BundelExtra.FINISH_REQUEST_CODE_DEFAULT);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void navigateToForResultCode(RenderFragmentBase fragment, android.app.FragmentManager fragmentManager, int requestCode) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_in_bottom,R.animator.fragment_slide_out_top);
        Fragment f = fragmentManager.findFragmentByTag(mSelectedFragment.getFragmentTag());
        if (f != null && f.isAdded()) {
            fragmentTransaction.hide(f);
        }
        fragment.setEnterTransition(new Slide().setDuration(300L));
        fragment.setContextAndReqCode(mSelectedFragment, requestCode);
        fragmentTransaction.add(R.id.main_activity_frame_layout, fragment, fragment.getFragmentTag());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentRegistered(RenderFragmentBase context) {
        mSelectedFragment = context;
    }

}
