package com.tt.reminder.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.listener.OnFragmentInteractionListener;

public class TasksContainWithDrawerViewFragment extends Fragment implements View.OnClickListener, DrawerLayout.DrawerListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;

    private OnFragmentInteractionListener mListener;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private boolean mIsLeftDrawerOpened = false;
    private ListView mListView;
    private ImageView mHeaderViewMainMenu, mHeaderViewLeftArrow, mHeaderViewVoiceInput, mHeaderViewAddNewTask;
    private TextView mHeaderViewTitle;
    private SearchView mHeaderViewSearch;

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
        View contentView = inflater.inflate(R.layout.fragment_tasks_containt_with_drawer_view, container, false);
        mDrawerLayout = (DrawerLayout) contentView.findViewById(R.id.drawer_layout);
        mLeftDrawer = (LinearLayout) contentView.findViewById(R.id.left_drawer);
        mListView = (ListView) contentView.findViewById(R.id.list);
        mHeaderViewMainMenu = (ImageView) contentView.findViewById(R.id.header_view_main_menu);
        mHeaderViewLeftArrow = (ImageView) contentView.findViewById(R.id.header_view_left_arrow);
        mHeaderViewTitle = (TextView) contentView.findViewById(R.id.header_view_title);
        mHeaderViewVoiceInput = (ImageView) contentView.findViewById(R.id.header_view_voice_input);
        mHeaderViewSearch = (SearchView) contentView.findViewById(R.id.header_view_search);
        mHeaderViewAddNewTask = (ImageView) contentView.findViewById(R.id.header_view_add_new_task);
        mHeaderViewLeftArrow.setVisibility(View.GONE);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerLayout.addDrawerListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mHeaderViewMainMenu.setOnClickListener(this);
        mHeaderViewVoiceInput.setOnClickListener(this);
        mHeaderViewAddNewTask.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_view_main_menu:
                onMainMenuClick();
                break;
            case R.id.header_view_add_new_task:
                navivateToEditFragment(Constant.FragmentType.NEW_EDIT_TASK_FRAGMENT);
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

    private void navivateToEditFragment(int fragmentType) {
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
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mDrawerLayout != null) {
            mDrawerLayout.removeDrawerListener(this);
        }
    }

}
