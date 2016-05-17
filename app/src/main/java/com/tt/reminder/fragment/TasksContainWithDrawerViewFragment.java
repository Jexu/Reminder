package com.tt.reminder.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;

public class TasksContainWithDrawerViewFragment extends FragmentBaseWithSharedHeaderView implements View.OnClickListener, DrawerLayout.DrawerListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private boolean mIsLeftDrawerOpened = false;
    private ListView mListView;

    public TasksContainWithDrawerViewFragment() {
        // Required empty public constructor
    }

    public static TasksContainWithDrawerViewFragment newInstance() {
        if (mTasksContainWithDrawerViewFragment == null) {
            mTasksContainWithDrawerViewFragment = new TasksContainWithDrawerViewFragment();
        }
        return mTasksContainWithDrawerViewFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = inflater.inflate(R.layout.fragment_tasks_containt_with_drawer_view, container, false);
        mDrawerLayout = (DrawerLayout) contentView.findViewById(R.id.drawer_layout);
        mLeftDrawer = (LinearLayout) contentView.findViewById(R.id.left_drawer);
        mListView = (ListView) contentView.findViewById(R.id.list);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerLayout.addDrawerListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mHeaderViewMainMenu.setOnClickListener(this);
        mHeaderViewLeftArrow.setVisibility(View.GONE);
        mHeaderViewVoiceInput.setOnClickListener(this);
        mHeaderViewAddNewTask.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_view_main_menu:
                onMainMenuClick();
                break;
            case R.id.header_view_add_new_task:
                navigateToEditFragment(Constant.FragmentType.NEW_EDIT_TASK_FRAGMENT);
                break;
        }
    }

    private void onMainMenuClick() {
        if (mDrawerLayout != null) {
            if (mIsLeftDrawerOpened) {
                mDrawerLayout.closeDrawer(mLeftDrawer);
            } else {
                mDrawerLayout.openDrawer(mLeftDrawer);
            }
        }
    }

    private void navigateToEditFragment(int fragmentType) {
        EditTaskFragment editTaskFragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.FragmentType.FRAGMENT_TYPE, fragmentType);
        editTaskFragment.setArguments(args);
        MainActivity.navigateTo(editTaskFragment, getFragmentManager());
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (slideOffset <= 0.4) {
            onMainMenuAnimation(mHeaderViewMainMenu, (float) (slideOffset * 2.5));
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mHeaderViewMainMenu.setRotation(90);
        mIsLeftDrawerOpened = true;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mHeaderViewMainMenu.setRotation(0);
        mIsLeftDrawerOpened = false;
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private void onMainMenuAnimation(View mainMenu, float drawerViewSlideOffset) {
        mainMenu.setRotation(drawerViewSlideOffset * 90);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    public void onButtonPressed(Uri uri) {
        super.onButtonPressed(uri);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDrawerLayout != null) {
            mDrawerLayout.removeDrawerListener(this);
        }
    }

}
