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
import android.widget.TextView;

import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.reminder.adapter.RenderRecycleViewAdapter;
import com.tt.reminder.notification.RenderAlarm;
import com.tt.sharedbaseclass.adapter.RenderRecycleViewAdapterBase;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.fragment.TaskContainFragmentBase;
import com.tt.sharedbaseclass.model.GroupBean;
import com.tt.sharedbaseclass.model.RenderBeanBase;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;

import java.util.Calendar;

public class TasksContainWithDrawerViewFragment extends TaskContainFragmentBase
        implements DrawerLayout.DrawerListener,
        RenderRecycleViewAdapter.OnItemClickListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;

    private DrawerLayout mDrawerLayout;
    private ScrollView mLeftDrawer;
    private TextView mNoTaskPage;
    private boolean mIsLeftDrawerOpened = false;
    private RecyclerView mTasksContainerRecycleView;
    private RecyclerView mLeftDrawerCategoryRecycleView;
    private RenderRecycleViewAdapter mTasksContainerAdapter;
    private RenderRecycleViewAdapter mLeftDrawerGroupsAdapter;
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
        mTasksContainerAdapter = new RenderRecycleViewAdapter(getActivity(), Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER);
        mLeftDrawerGroupsAdapter = new RenderRecycleViewAdapter(getActivity(), Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY);
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
        mTasksContainerRecycleView = (RecyclerView) contentView.findViewById(R.id.list);
        mNoTaskPage = (TextView) contentView.findViewById(R.id.no_task_page);
        mLeftDrawerCategoryRecycleView = (RecyclerView) contentView.findViewById(R.id.left_drawer_category_recycleview);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerLayout.addDrawerListener(this);
        //mTasksContainerRecycleView.setOnItemClickListener(this);
        mHeaderViewMainMenu.setOnClickListener(this);
        mHeaderViewLeftArrow.setVisibility(View.GONE);
        mHeaderViewVoiceInput.setOnClickListener(this);
        mHeaderViewAddNewTask.setOnClickListener(this);
        mTasksContainerRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTasksContainerRecycleView.setItemAnimator(new DefaultItemAnimator());
        mTasksContainerRecycleView.setAdapter(mTasksContainerAdapter);

        mLeftDrawerCategoryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLeftDrawerCategoryRecycleView.setItemAnimator(new DefaultItemAnimator());
        mLeftDrawerCategoryRecycleView.setAdapter(mLeftDrawerGroupsAdapter);

        mTasksContainerAdapter.setOnItemClickListener(this);
        mLeftDrawerGroupsAdapter.setOnItemClickListener(this);
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
                + (requestCode == RenderRecycleViewAdapter.POSITION_GROUP_FINISHED ?
                Constant.RenderDbHelper.GROUP_NAME_FINISHED : mRenderObjectBeansGroups.get(requestCode).toString())
                , renderObjectBeans);
        if (mStartFrom == Constant.BundelExtra.START_FROM_NOTIFICATION) {
            navigateToEditFragment((TaskBean) getArguments().getSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN));
            mStartFrom = Constant.BundelExtra.START_FROM_DEFAULT;
        }
        updateRecycleView(renderObjectBeans);
    }

    private void updateRecycleView(RenderObjectBeans renderObjectBeans) {
        if (renderObjectBeans != null) {
            mTasksContainerAdapter.addAllBeans(renderObjectBeans);
        }
    }

    @Override
    protected void getGroupsSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
        // TODO: 5/25/16 update lrucache; update groups view; have to check size
        mLruCache.put(Constant.BundelExtra.EXTRAL_GROUPS_BEANS, renderObjectBeans);
        mRenderObjectBeansGroups = renderObjectBeans;
        mLeftDrawerGroupsAdapter.addAllBeans(mRenderObjectBeansGroups);
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
    protected void onLeftDrawerGroupFinishedClick() {
        //left drawer group finished
        if (mLeftDrawerGroupsAdapter.getPositionClickedBefore() != RenderRecycleViewAdapter.POSITION_GROUP_FINISHED) {
            mHeaderViewTitle.setText(Constant.RenderDbHelper.GROUP_NAME_FINISHED);
            RenderObjectBeans taskBeans = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+Constant.RenderDbHelper.GROUP_NAME_FINISHED);
            if (taskBeans == null) {
                getTasksByGroupName(Constant.RenderDbHelper.GROUP_NAME_FINISHED, RenderRecycleViewAdapter.POSITION_GROUP_FINISHED);
            } else {
                mTasksContainerAdapter.addAllBeans(taskBeans);
            }
            mLeftDrawerGroupsAdapter.setPositionClicke(RenderRecycleViewAdapterBase.POSITION_GROUP_FINISHED);
        }
        onMainMenuClick();
    }

    @Override
    public void onItemClickListener(View view, Constant.RENDER_ADAPTER_TYPE adapterType, int positionClickedBefore, int position) {
       if (adapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
           TaskBean taskBean = (TaskBean) mTasksContainerAdapter.getBean(position);
           navigateToEditFragment(Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value(), taskBean, position);
       } else if (adapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY) {
            //left drawer category
           if (position != positionClickedBefore) {
               String groupName = ((GroupBean)mRenderObjectBeansGroups.get(position)).getGroup();
               mHeaderViewTitle.setText(groupName);
               RenderObjectBeans taskBeans = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+groupName);
               if (taskBeans == null) {
                    getTasksByGroupName(groupName, position);
               } else {
                   mTasksContainerAdapter.addAllBeans(taskBeans);
               }
           }
           onMainMenuClick();
       }
    }

    @Override
    public void onItemLongClickListener(View view, Constant.RENDER_ADAPTER_TYPE adapterType, final int position) {
        String alertTitle = getResources().getString(R.string.alert_dialog_title_are_you_sure);
        String alertMessage = "";
        int id = -1;
        int action = Constant.RenderServiceHelper.ACTION.ACTION_DEFAULT.value();
        String[] wheres = null;
        if (adapterType == Constant.RENDER_ADAPTER_TYPE.TASKS_CONTAINER) {
           alertMessage = getResources().getString(R.string.alert_dialog_message_delete_this_task);
           action = Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.value();
           id = ((TaskBean)mTasksContainerAdapter.getBean(position)).getId();
            wheres = new String[]{id+""};
        } else if (adapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY) {
            GroupBean groupBean = (GroupBean)mRenderObjectBeansGroups.get(position);
            if (groupBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
                return;
            }
            id = groupBean.getId();
            alertMessage = "Delete this group?";
            action = Constant.RenderServiceHelper.ACTION.ACTION_DELETE_GROUP.value();
            wheres = new String[]{id+"", groupBean.getGroup()};
        }
        showDeleteAlertDialog(action, alertTitle, alertMessage, wheres, position);
    }

    private void showDeleteAlertDialog(final int action, String alertTitle, String alertMessage, final String[]wheres, final int position) {
        AlertDialog.Builder builder = getDefaultAlertDialogBuilder(alertTitle
                , alertMessage);
        builder.setNegativeButton(R.string.alert_dialog_negative_button_cancel, null)
                .setPositiveButton(R.string.alert_dialog_negative_button_delete
                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBean(action
                                , action == Constant.RenderServiceHelper.ACTION.ACTION_DELETE_TASK.value()
                                ? Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS:Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP
                                , wheres
                                , position);
                    }
                }).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = Integer.parseInt(buttonView.getTag(R.id.shared_list_item_right_checkbox).toString());
        //Log.i("Render", position+"");
        TaskBean oldTaskBean = (TaskBean) mTasksContainerAdapter.getBean(position);
        TaskBean newTaskBean = new TaskBean();
        newTaskBean.copy(oldTaskBean);
        if (isChecked && newTaskBean.getRepeatIntervalTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_INTERVAL) {
            //repeat --> reset alarm time
           resetTaskBeanAlarmDate(newTaskBean);
        } else {
            //no repeat
            newTaskBean.setIsFinished(isChecked ? TaskBean.VALUE_FINISHED : TaskBean.VALUE_NOT_FINISHED);
        }
        mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_TASK.value()
                , Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                , oldTaskBean
                , newTaskBean
                , null
                , position);
    }

    private void resetTaskBeanAlarmDate(TaskBean taskBean) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + taskBean.getRepeatIntervalTimeInMillis());
        taskBean.setYear(calendar.get(Calendar.YEAR));
        taskBean.setMonth(calendar.get(Calendar.MONTH));
        taskBean.setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        taskBean.setHour(calendar.get(Calendar.HOUR));
        taskBean.setMinuse(calendar.get(Calendar.MINUTE));
    }

    @Override
    public void onAdapterEmpty(Constant.RENDER_ADAPTER_TYPE adapterType, boolean isAdapterEmpty) {
        if (isAdapterEmpty) {
            mTasksContainerRecycleView.setVisibility(View.GONE);
            mNoTaskPage.setVisibility(View.VISIBLE);
        } else {
            mTasksContainerRecycleView.setVisibility(View.VISIBLE);
            mNoTaskPage.setVisibility(View.GONE);
        }
    }

    private void deleteBean(int action, String tableName, String[] wheres, int requestCode) {
        mRenderService.getOrUpdate(action
                , tableName
                , null
                , null
                , wheres
                , requestCode);
    }

    private void onUpdateBeanSuccess(long row, int requestCode, int resultCode) {

        switch (resultCode) {
            case Constant.RenderServiceHelper.RESULT_CODE_DELETE_TASK_SUCCESS:
                if( requestCode >= 0) {
                    // TODO: 2016/5/29 remove task bean
                    taskDeleted(row, requestCode);
                }
                break;
            case Constant.RenderServiceHelper.RESULT_CODE_DELETE_GROUP_SUCCESS:
                if (requestCode >= 0) {
                // TODO: 2016/5/29 remove group bean
                    groupDeleted(row, requestCode);
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

    private void taskDeleted(long row, int requestCode) {
        if (requestCode >= 0) {
            TaskBean taskBean = (TaskBean) mTasksContainerAdapter.getBean(requestCode);
            if (taskBean.isFinished() == TaskBean.VALUE_FINISHED) {
                mTasksContainerAdapter.removeBean(requestCode, false);
            } else {
                RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+taskBean.getGroup());
                if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
                    RenderObjectBeans myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
                    if (myTasks != null) {
                        myTasks.remove(myTasks.indexOf(taskBean));
                    }
                }
                if (tasks != null) {
                    tasks.remove(tasks.indexOf(taskBean));
                }
                // TODO: 5/31/16 remove notification alarm
                if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
                    RenderAlarm.removeAlarm(getActivity(), taskBean);
                }
            }
            mTasksContainerAdapter.notifyDataSetChanged();
        }
    }

    private void groupDeleted(long row, int requestCode) {
        GroupBean groupBean = (GroupBean) mRenderObjectBeansGroups.get(requestCode);
        RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+groupBean.getGroup());
        if (mLeftDrawerGroupsAdapter.getPositionClickedBefore() == requestCode) {
            mTasksContainerAdapter.addAllBeans(new RenderObjectBeans());
        }
        if (tasks != null) {
            mLruCache.remove(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+groupBean.getGroup());
        }
        getTasksByGroupName(Constant.RenderDbHelper.GROUP_NAME_MY_TASK
                , Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED);
        mLeftDrawerGroupsAdapter.removeBean(requestCode, true);
        mLeftDrawerGroupsAdapter.setPositionClicke(RenderRecycleViewAdapter.POSITION_GROUP_FINISHED-1);
    }

    private void taskFinishStatueChanged(long row, int requestCode) {
        if (requestCode >= 0) {
            TaskBean taskBean = (TaskBean) mTasksContainerAdapter.getBean(requestCode);
            if (taskBean.isFinished() == TaskBean.VALUE_FINISHED) {
                //finished --> unfinished
                if (mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+taskBean.getGroup()) != null) {
                    TaskBean newTaskBean = new TaskBean();
                    newTaskBean.copy(taskBean);
                    newTaskBean.setIsFinished(TaskBean.VALUE_NOT_FINISHED);
                    //应该根据组信息add两次；myTasks和自身groupName
                    RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+taskBean.getGroup());
                    if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
                        RenderObjectBeans myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
                        if (myTasks != null) {
                            myTasks.addBeanInOrder(newTaskBean);
                        }
                    }
                    if (tasks != null) {
                        tasks.addBeanInOrder(newTaskBean);
                    }
                }
                mTasksContainerAdapter.removeBean(requestCode, false);
                // TODO: 5/31/16 create notification alarm again
                if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
                    RenderAlarm.createAlarm(getActivity(), taskBean);
                }
            } else {
                //unfinished --> finished
                boolean isRepeat = taskBean.getRepeatIntervalTimeInMillis()==TaskBean.DEFAULT_VALUE_OF_INTERVAL?false:true;
                if (!isRepeat && mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+Constant.RenderDbHelper.GROUP_NAME_FINISHED) != null) {
                    TaskBean newTaskBean = new TaskBean();
                    newTaskBean.copy(taskBean);
                    newTaskBean.setIsFinished(TaskBean.VALUE_FINISHED);
                    mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+Constant.RenderDbHelper.GROUP_NAME_FINISHED).addBeanInOrder(newTaskBean);
                }
                //应该根据组信息update or remove两次；myTasks和自身groupName
                RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+taskBean.getGroup());
                RenderObjectBeans myTasks = null;
                if (!taskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
                    myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
                    if (myTasks != null) {
                        myTasks.remove(myTasks.indexOf(taskBean));
                    }
                }
                if (tasks != null) {
                    tasks.remove(tasks.indexOf(taskBean));
                }

                if (isRepeat) {
                    resetTaskBeanAlarmDate(taskBean);
                    if (tasks != null) {
                        tasks.addBeanInOrder(taskBean);
                    }
                    if (myTasks != null) {
                        myTasks.addBeanInOrder(taskBean);
                    }
                    // TODO: 5/31/16 reset notification alarm
                    if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
                        RenderAlarm.updateAlarm(getActivity(), taskBean);
                    }
                } else {
                    // TODO: 5/31/16 remove notification alarm
                    if (taskBean.getTimeInMillis() != TaskBean.DEFAULT_VALUE_OF_DATE_TIME) {
                        RenderAlarm.removeAlarm(getActivity(), taskBean);
                    }
                }
            }
            mTasksContainerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onAddNewGroupSuccess(long row, int requestCode, int resultCode) {
        Log.i("Render", "success to add new group");
        mLeftDrawerGroupsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onAddNewGroupFail(int requestCode, int resultCode) {
        Log.i("Render", "fail to add new group");
        mRenderObjectBeansGroups.remove(mRenderObjectBeansGroups.size()-1);
        mLeftDrawerGroupsAdapter.notifyDataSetChanged();
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
        int position = mTasksContainerAdapter.findBeanPosition(taskBean);
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
            RenderObjectBeans myTasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN + Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
            RenderObjectBeans tasks;
            if (myTasks != null) {
                myTasks.addBeanInOrder(newTaskBean);
            }
            if (!newTaskBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
                tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+newTaskBean.getGroup());
                if (tasks != null) {
                    tasks.addBeanInOrder(newTaskBean);
                }
            }

        } else if (resultCode == Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS
                && requestCode >= 0) {
            //request code is old task position
            TaskBean taskBean = (TaskBean) bundle.get(Constant.BundelExtra.EXTRA_TASK_BEAN);
            mTasksContainerAdapter.removeBean(requestCode, false);
            mTasksContainerAdapter.addBeanInOrder(taskBean, true);
        }
        if (bundle != null && bundle.getBoolean(Constant.BundelExtra.EXTRA_IS_ADD_NEW_GROUP)) {
            mLeftDrawerGroupsAdapter.notifyDataSetChanged();
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
