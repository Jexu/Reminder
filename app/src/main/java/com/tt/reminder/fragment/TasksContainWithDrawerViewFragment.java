package com.tt.reminder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedutils.DeviceUtil;

public class TasksContainWithDrawerViewFragment extends FragmentBaseWithSharedHeaderView
        implements View.OnClickListener, DrawerLayout.DrawerListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;

    private static long INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK = 500;
    private long mFirstBackPressedTime = 0;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private boolean mIsLeftDrawerOpened = false;
    private ListView mListView;
    private LruCache<String, RenderObjectBeans> mLruCache;
    private GetTasksByGroupNameCallback mGetTasksByGroupNameCallback;
    private GetGroupsCallback mGetGroupsCallback;

    public TasksContainWithDrawerViewFragment() {
        // Required empty public constructor
        super();
    }

    public static TasksContainWithDrawerViewFragment newInstance() {
        if (mTasksContainWithDrawerViewFragment == null) {
            mTasksContainWithDrawerViewFragment = new TasksContainWithDrawerViewFragment();
        }
        return mTasksContainWithDrawerViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLruCache = new LruCache<>((int)DeviceUtil.getMaxMemory()/8);
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
        testListView();
    }

    @Override
    public void initServices() {
        super.initServices();
        mGetTasksByGroupNameCallback = new GetTasksByGroupNameCallback();
        mGetGroupsCallback = new GetGroupsCallback(this);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.toString(),
          mGetTasksByGroupNameCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.toString(),
          mGetGroupsCallback);
    }

    @Override
    public void fetchData() {
        Log.i("Render", "fetchData");
        mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.value(),
          Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS,
          null,
          null,
          null,
          Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED);
        mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.value(),
          Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP,
          null,
          null,
          null,
          0);
    }
    
    private void getTasksSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
        // TODO: 5/25/16 update lrucache; update listview; have to check size
    }
    
    private void getGroupsSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
        // TODO: 5/25/16 update lrucache; update groups view; have to check size
        mLruCache.put(Constant.BundelExtra.EXTRAL_GROUPS_BEANS, renderObjectBeans);
    }

    private void testListView() {
        mListView.setDivider(null);
        mListView.setDividerHeight(-1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.shared_list_item_view,R.id.shared_list_item_task_group_name, new String[]{"Work", "Shop"});
        mListView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onBackPressed() {
        if (mFirstBackPressedTime == 0
                || System.currentTimeMillis() - mFirstBackPressedTime >= INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK) {
            mFirstBackPressedTime = System.currentTimeMillis();
            Toast.makeText(getActivity(), R.string.toast_double_click_to_exit, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_view_main_menu:
                onMainMenuClick();
                break;
            case R.id.header_view_add_new_task:
                navigateToEditFragment(Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value());
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
        args.putInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE, fragmentType);
        // TODO: 2016/5/25 have to check null, if null then get groups again
        args.putSerializable(Constant.BundelExtra.EXTRAL_GROUPS_BEANS,
          mLruCache.get(Constant.BundelExtra.EXTRAL_GROUPS_BEANS));
        editTaskFragment.setArguments(args);
        MainActivity.navigateToForResultCode(editTaskFragment, getFragmentManager(), 1);
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

    @Override
    public void onFinishedWithResult(int requestCode, int resultCode, Bundle bundle) {
        super.onFinishedWithResult(requestCode, resultCode, bundle);
        Log.i("Render", "finishedWithRequestCode");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDrawerLayout != null) {
            mDrawerLayout.removeDrawerListener(this);
        }
    }

    private static class GetTasksByGroupNameCallback extends RenderCallback {

        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {

        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {

        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {

        }
    }

    private static class GetGroupsCallback extends RenderCallback {
        private TasksContainWithDrawerViewFragment mContext;
        private GetGroupsCallback(TasksContainWithDrawerViewFragment context) {
            mContext = context;
        }

        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
            Log.i("Render", "get group successfully");
            mContext.getGroupsSuccess(renderObjectBeans, requestCode, resultCode);
        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {

        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {
            Log.i("Render", "fail to get group");
        }
    }

}
