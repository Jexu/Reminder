package com.tt.reminder.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.reminder.adapter.RenderRecycleViewAdapter;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.TaskContainFragmentBase;
import com.tt.sharedbaseclass.model.GroupBean;


import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;

import java.util.ConcurrentModificationException;

public class TasksContainWithDrawerViewFragment extends TaskContainFragmentBase
        implements DrawerLayout.DrawerListener,
        RenderRecycleViewAdapter.OnItemClickListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;

    private DrawerLayout mDrawerLayout;
    private ScrollView mLeftDrawer;
    private boolean mIsLeftDrawerOpened = false;
    private RecyclerView mRenderRecycleView;
    private RenderRecycleViewAdapter mRenderRecycleViewAdapter;
    private GetTasksByGroupNameCallback mGetTasksByGroupNameCallback;
    private GetGroupsCallback mGetGroupsCallback;
    private UpdateBeanCallback mUpdateBeanCallback;
    private RenderObjectBeans mRenderObjectBeansGroups;
    private int mStartFrom;

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
        mRenderRecycleViewAdapter = new RenderRecycleViewAdapter(getActivity(), Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER);
        Bundle args = getArguments();
        if(args != null) {
            mStartFrom = args.getInt(Constant.BundelExtra.EXTRA_START_FROM);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = inflater.inflate(R.layout.fragment_tasks_containt_with_drawer_view, container, false);
        mDrawerLayout = (DrawerLayout) contentView.findViewById(R.id.drawer_layout);
        mLeftDrawer = (ScrollView) contentView.findViewById(R.id.left_drawer);
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
    }

    @Override
    public void initServices() {
        super.initServices();
        mGetTasksByGroupNameCallback = new GetTasksByGroupNameCallback(this);
        mGetGroupsCallback = new GetGroupsCallback(this);
        mUpdateBeanCallback = new UpdateBeanCallback(this);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.toString(),
                mGetTasksByGroupNameCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.toString(),
                mGetGroupsCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.toString()
                , mUpdateBeanCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.toString(),
                mUpdateBeanCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_GROUP_NAME.toString(),
                mUpdateBeanCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_DELETE_GROUP.toString(),
                mUpdateBeanCallback);
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
        if (mStartFrom == Constant.BundelExtra.START_FROM_NOTIFICATION) {
            navigateToEditFragment((TaskBean) getArguments().getSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN));
            mStartFrom = Constant.BundelExtra.START_FROM_DEFAULT;
        }
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
        TaskBean taskBean = (TaskBean) mRenderRecycleViewAdapter.getBean(position);
        navigateToEditFragment(Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value(), taskBean, position);
    }

    @Override
    public void onItemLongClickListener(View view, final int position) {
        AlertDialog.Builder builder = getDefaultAlertDialogBuilder(getResources().getString(R.string.alert_dialog_title_are_you_sure)
          , getResources().getString(R.string.alert_dialog_message_delete_this_task));
        builder.setNegativeButton(R.string.alert_dialog_negative_button_cancel, null)
                .setPositiveButton(R.string.alert_dialog_negative_button_delete
                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBean(Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.value()
                            , Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                            , ((TaskBean)mRenderRecycleViewAdapter.getBean(position)).getId()
                            , position);
                    }
                }).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = Integer.parseInt(buttonView.getTag(R.id.shared_list_item_right_checkbox).toString());
        //Log.i("Render", position+"");
        TaskBean oldTaskBean = (TaskBean) mRenderRecycleViewAdapter.getBean(position);
        TaskBean newTaskBean = new TaskBean();
        newTaskBean.copy(oldTaskBean);
        newTaskBean.setIsFinished(isChecked? TaskBean.VALUE_FINISHED: TaskBean.VALUE_NOT_FINISHED);
        mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.value()
                , Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                , oldTaskBean
                , newTaskBean
                , null
                , position);
    }

    private void deleteBean(int action, String tableName, int id, int requestCode) {
        mRenderService.getOrUpdate(action
                , tableName
                , null
                , null
                , new String[]{id + ""}
                , requestCode);
    }

    private void onUpdateBeanSuccess(long row, int requestCode, int resultCode) {

        switch (resultCode) {
            case Constant.RenderServiceHelper.RESULT_CODE_DELETE_TASK_SUCCESS:
                if( requestCode >= 0) {
                    // TODO: 2016/5/29 remove task bean
                    mRenderRecycleViewAdapter.removeBean(requestCode);
                }
                break;
            case Constant.RenderServiceHelper.RESULT_CODE_DELETE_GROUP_SUCCESS:
                if (requestCode >= 0) {
                // TODO: 2016/5/29 remove group bean
                }
                break;
            case Constant.RenderServiceHelper.RESULT_CODE_UPDATE_TASK_SUCCESS:
                // TODO: 5/31/16 task finished status changed
                taskFinishStatueChanged(row, requestCode);
                break;
            case Constant.RenderServiceHelper.RESULT_CODE_UPDATE_GROUP_SUCCESS:
                if (resultCode >= 0 ) {
                // TODO: 5/31/16 group name changed
                }
                break;
        }

    }

    private void taskFinishStatueChanged(long row, int requestCode) {
        if (requestCode >= 0) {
            TaskBean taskBean = (TaskBean) mRenderRecycleViewAdapter.getBean(requestCode);
            if (taskBean.isFinished() == TaskBean.VALUE_FINISHED) {
                if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)
                    && mLruCache.get(taskBean.getGroup()) != null) {
                    TaskBean newTaskBean = new TaskBean();
                    newTaskBean.copy(taskBean);
                    newTaskBean.setIsFinished(TaskBean.VALUE_NOT_FINISHED);
                    mLruCache.get(taskBean.getGroup()).addBeanInOrder(newTaskBean);
                    // TODO: 5/31/16 create notification alarm again
                } else if (taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)
                  && mLruCache.get(Constant.RenderDbHelper.GROUP_NAME_MY_TASK) != null) {
                    TaskBean newTaskBean = new TaskBean();
                    newTaskBean.copy(taskBean);
                    newTaskBean.setIsFinished(TaskBean.VALUE_NOT_FINISHED);
                    mLruCache.get(Constant.RenderDbHelper.GROUP_NAME_MY_TASK).addBeanInOrder(newTaskBean);
                    // TODO: 5/31/16 create notification alarm again
                }
            } else {
                if (mLruCache.get(Constant.RenderDbHelper.GROUP_NAME_FINISHED) != null) {
                    TaskBean newTaskBean = new TaskBean();
                    newTaskBean.copy(taskBean);
                    newTaskBean.setIsFinished(TaskBean.VALUE_FINISHED);
                    mLruCache.get(taskBean.getGroup()).addBeanInOrder(newTaskBean);
                    // TODO: 5/31/16 remove notification alarm
                }
            }
            mRenderRecycleViewAdapter.removeBean(requestCode);
        }
    }

    @Override
    protected void navigateToEditFragment(int fragmentType, TaskBean taskBean, int requestCode) {
        EditTaskFragment editTaskFragment = new EditTaskFragment();
        Bundle args = new Bundle();
        if (fragmentType == Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value()) {
            args.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, taskBean);
        }
        args.putInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE, fragmentType);
        // TODO: 2016/5/25 have to check null, if null then get groups again
        args.putSerializable(Constant.BundelExtra.EXTRAL_GROUPS_BEANS,
          mRenderObjectBeansGroups);
        editTaskFragment.setArguments(args);
        MainActivity.navigateToForResultCode(editTaskFragment
          , getFragmentManager()
          , requestCode);
    }

    private void navigateToEditFragment(TaskBean taskBean) {
        int position = mRenderRecycleViewAdapter.findBeanPosition(taskBean);
        navigateToEditFragment(Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value(), taskBean, position);
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
        if (resultCode == Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS
            && requestCode == Constant.BundelExtra.FINISH_REQUEST_CODE_NEW_TASK) {
            TaskBean newTaskBean = (TaskBean) bundle.get(Constant.BundelExtra.EXTRA_TASK_BEAN);
            mRenderRecycleViewAdapter.addBean(newTaskBean);
        } else if (resultCode == Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS
                && requestCode >= 0) {
            //request code is old task position
            TaskBean taskBean = (TaskBean) bundle.get(Constant.BundelExtra.EXTRA_TASK_BEAN);
            mRenderRecycleViewAdapter.removeBean(requestCode);
            mRenderRecycleViewAdapter.addBean(taskBean);
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

    private static class UpdateBeanCallback extends RenderCallback {
        private TasksContainWithDrawerViewFragment mContext;
        private UpdateBeanCallback(TasksContainWithDrawerViewFragment context) {
            mContext = context;
        }

        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {

        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {
            Log.i("Render", "update successfully");
            //delete and update
            mContext.onUpdateBeanSuccess(row, requestCode, resultCode);
        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {
            Log.e("Render", "fail to update");
        }
    }

}
