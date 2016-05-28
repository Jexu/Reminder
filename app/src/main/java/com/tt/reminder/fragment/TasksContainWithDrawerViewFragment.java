package com.tt.reminder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.reminder.adapter.RenderRecycleViewAdapter;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.FragmentBaseWithSharedHeaderView;
import com.tt.sharedbaseclass.fragment.TaskContainFragmentBase;
import com.tt.sharedbaseclass.model.GroupBean;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedutils.DeviceUtil;

public class TasksContainWithDrawerViewFragment extends TaskContainFragmentBase
        implements DrawerLayout.DrawerListener,
        RenderRecycleViewAdapter.OnItemClickListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private boolean mIsLeftDrawerOpened = false;
    private RecyclerView mRenderRecycleView;
    private RenderRecycleViewAdapter mRenderRecycleViewAdapter;
    private GetTasksByGroupNameCallback mGetTasksByGroupNameCallback;
    private GetGroupsCallback mGetGroupsCallback;
    private RenderObjectBeans mRenderObjectBeansGroups;

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
        mRenderRecycleViewAdapter = new RenderRecycleViewAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = inflater.inflate(R.layout.fragment_tasks_containt_with_drawer_view, container, false);
        mDrawerLayout = (DrawerLayout) contentView.findViewById(R.id.drawer_layout);
        mLeftDrawer = (LinearLayout) contentView.findViewById(R.id.left_drawer);
        mRenderRecycleView = (RecyclerView) contentView.findViewById(R.id.list);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerLayout.addDrawerListener(this);
        //mRenderRecycleView.setOnItemClickListener(this);
        mHeaderViewMainMenu.setOnClickListener(this);
        mHeaderViewLeftArrow.setVisibility(View.GONE);
        mHeaderViewVoiceInput.setOnClickListener(this);
        mHeaderViewAddNewTask.setOnClickListener(this);
        mRenderRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRenderRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRenderRecycleView.setAdapter(mRenderRecycleViewAdapter);
        mRenderRecycleViewAdapter.setmOnItemClickLitener(this);
        testListView();
    }

    @Override
    public void initServices() {
        super.initServices();
        mGetTasksByGroupNameCallback = new GetTasksByGroupNameCallback(this);
        mGetGroupsCallback = new GetGroupsCallback(this);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.toString(),
                mGetTasksByGroupNameCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.toString(),
                mGetGroupsCallback);
    }

    @Override
    protected void getTasksSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
        // TODO: 5/25/16 update lrucache; update listview; have to check size
        //key = extra_render_object_bean+group_name
        if (mRenderObjectBeansGroups == null) {
            getGroupsExceptFinished(Constant.RenderServiceHelper.REQUEST_CODE_DEFAULT);
        }
        mLruCache.put(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN
                + mRenderObjectBeansGroups.get(requestCode).toString()
                , renderObjectBeans);
        updateRecycleView((GroupBean) mRenderObjectBeansGroups.get(requestCode));
    }

    private void updateRecycleView(GroupBean groupBean) {
        RenderObjectBeans renderObjectBeans
                = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN
                    + groupBean.toString());
        if (renderObjectBeans != null) {
            if (renderObjectBeans.isEmpty()) {

            } else {
                mRenderRecycleViewAdapter.addAllBeans(renderObjectBeans);
            }
        } else {
            getTasksByGroupName(groupBean.toString()
                    , mRenderObjectBeansGroups.indexOf(groupBean));
        }
    }

    @Override
    protected void getGroupsSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
        // TODO: 5/25/16 update lrucache; update groups view; have to check size
        mLruCache.put(Constant.BundelExtra.EXTRAL_GROUPS_BEANS, renderObjectBeans);
        mRenderObjectBeansGroups = renderObjectBeans;
    }

    private void testListView() {
//        TaskBean taskBean = new TaskBean();
//        taskBean.setTaskContent("Task");
//        taskBean.setGroup("My Tasks");
//        taskBean.setYear(2015);
//        taskBean.setMonth(12);
//        taskBean.setDayOfMonth(24);
//        taskBean.setHour(23);
//        taskBean.setMinuse(5);
//        RenderObjectBeans renderObjectBeans = new RenderObjectBeans();
//
//        TaskBean taskBean2 = new TaskBean();
//        taskBean2.setTaskContent("Task22222222222222222222222222222222222222222");
//        taskBean2.setGroup("My Tasks2");
//        taskBean2.setYear(2017);
//        taskBean2.setMonth(12);
//        taskBean2.setDayOfMonth(24);
//        taskBean2.setHour(23);
//        taskBean2.setMinuse(5);
//
//        renderObjectBeans.add(taskBean);
//        renderObjectBeans.add(taskBean2);
//        mRenderRecycleViewAdapter.addAllBeans(renderObjectBeans);
    }

    @Override
    protected void onMainMenuClick() {
        if (mDrawerLayout != null) {
            if (mIsLeftDrawerOpened) {
                mDrawerLayout.closeDrawer(mLeftDrawer);
            } else {
                mDrawerLayout.openDrawer(mLeftDrawer);
            }
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {

    }

    @Override
    public void onItemLongClickListener(View view, int position) {

    }

    @Override
    protected void navigateToEditFragment(int fragmentType) {
        EditTaskFragment editTaskFragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE, fragmentType);
        // TODO: 2016/5/25 have to check null, if null then get groups again
        args.putSerializable(Constant.BundelExtra.EXTRAL_GROUPS_BEANS,
          mLruCache.get(Constant.BundelExtra.EXTRAL_GROUPS_BEANS));
        editTaskFragment.setArguments(args);
        MainActivity.navigateToForResultCode(editTaskFragment
                , getFragmentManager()
                , Constant.BundelExtra.FINISH_REQUEST_CODE_NEW_TASK);
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

    @Override
    public void onFinishedWithResult(int requestCode, int resultCode, Bundle bundle) {
        super.onFinishedWithResult(requestCode, resultCode, bundle);
        Log.i("Render", "onFinishedWithResult");
        if (resultCode != Constant.BundelExtra.FINISH_RESULT_CODE_DEFAULT
            && requestCode == Constant.BundelExtra.FINISH_REQUEST_CODE_NEW_TASK) {
            TaskBean newTaskBean = (TaskBean) bundle.get(Constant.BundelExtra.EXTRA_TASK_BEAN);
            mRenderRecycleViewAdapter.addBean(newTaskBean);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDrawerLayout != null) {
            mDrawerLayout.removeDrawerListener(this);
        }
    }

    private static class GetTasksByGroupNameCallback extends RenderCallback {

        private TasksContainWithDrawerViewFragment mContext;
        private GetTasksByGroupNameCallback(TasksContainWithDrawerViewFragment context) {
            mContext = context;
        }
        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
            Log.i("Render", "get tasks successfully");
            mContext.getTasksSuccess(renderObjectBeans, requestCode, resultCode);
        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {

        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {
            Log.e("Render", "fail to get tasks");
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
            Log.e("Render", "fail to get group");
        }
    }

}
