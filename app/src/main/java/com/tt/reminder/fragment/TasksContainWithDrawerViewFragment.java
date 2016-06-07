package com.tt.reminder.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.tt.reminder.R;
import com.tt.reminder.activity.MainActivity;
import com.tt.reminder.adapter.RenderRecycleViewAdapter;
import com.tt.sharedbaseclass.adapter.RenderRecycleViewAdapterBase;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.GroupBean;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedutils.IntentUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class TasksContainWithDrawerViewFragment extends TasksContainerFragmentWithLoading
        implements DrawerLayout.DrawerListener {

    private static TasksContainWithDrawerViewFragment mTasksContainWithDrawerViewFragment;

    protected LinearLayout mLeftDrawerGroupFinished;
    protected LinearLayout mLeftDrawerCreateNewGroup;
    protected LinearLayout mLeftDrawerSetting;
    protected LinearLayout mLeftDrawerFeedback;


    private DrawerLayout mDrawerLayout;
    private ScrollView mLeftDrawer;
    private boolean mIsLeftDrawerOpened = false;
    private RecyclerView mLeftDrawerCategoryRecycleView;
    private RenderRecycleViewAdapter mLeftDrawerGroupsAdapter;

    private AddNewBeanCallBack mAddNewBeanCallBack;

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
        mLeftDrawerGroupsAdapter = new RenderRecycleViewAdapter(getActivity(), Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY);
        Bundle args = getArguments();
        if(args != null) {
            mStartFrom = args.getInt(Constant.BundelExtra.EXTRA_START_FROM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLeftDrawerGroupFinished = (LinearLayout) view.findViewById(com.tt.sharedbaseclass.R.id.left_drawer_group_finished);
        mLeftDrawerCreateNewGroup = (LinearLayout) view.findViewById(com.tt.sharedbaseclass.R.id.left_drawer_create_new_group);
        mLeftDrawerSetting = (LinearLayout) view.findViewById(com.tt.sharedbaseclass.R.id.left_drawer_setting);
        mLeftDrawerFeedback = (LinearLayout) view.findViewById(com.tt.sharedbaseclass.R.id.left_drawer_feedback_help);

        mHeaderViewVoiceInput.setOnClickListener(this);
        mHeaderViewSearch.setOnClickListener(this);
        mLeftDrawerGroupFinished.setOnClickListener(this);
        mLeftDrawerCreateNewGroup.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mLeftDrawer = (ScrollView) view.findViewById(R.id.left_drawer);
        mLeftDrawerCategoryRecycleView = (RecyclerView) view.findViewById(R.id.left_drawer_category_recycleview);

        mDrawerLayout.addDrawerListener(this);
        //mTasksContainerRecycleView.setOnItemClickListener(this);
        mHeaderViewMainMenu.setOnClickListener(this);
        mHeaderViewLeftArrow.setVisibility(View.GONE);
        mHeaderViewVoiceInput.setOnClickListener(this);
        mHeaderViewAddNewTask.setOnClickListener(this);

        mLeftDrawerCategoryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLeftDrawerCategoryRecycleView.setItemAnimator(new DefaultItemAnimator());
        mLeftDrawerCategoryRecycleView.setAdapter(mLeftDrawerGroupsAdapter);

        mLeftDrawerGroupsAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initServices() {
        super.initServices();
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_UPDATE_GROUP_NAME.toString(),
          mUpdateBeanCallback);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_DELETE_GROUP.toString(),
          mUpdateBeanCallback);
        mAddNewBeanCallBack = new AddNewBeanCallBack(this);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.toString(),
          mAddNewBeanCallBack);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_ADD_NEW_TASK.toString(),
          mAddNewBeanCallBack);
    }

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

    protected void getGroupsSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode) {
        // TODO: 5/25/16 update lrucache; update groups view; have to check size
        mLruCache.put(Constant.BundelExtra.EXTRAL_GROUPS_BEANS, renderObjectBeans);
        mRenderObjectBeansGroups = renderObjectBeans;
        mLeftDrawerGroupsAdapter.addAllBeans(mRenderObjectBeansGroups);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int viewId = view.getId();
        switch (viewId) {
            case com.tt.sharedbaseclass.R.id.header_view_main_menu:
                onMainMenuClick();
                break;
            case com.tt.sharedbaseclass.R.id.header_view_add_new_task:
                navigateToEditFragment(Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()
                  , null
                  , Constant.BundelExtra.FINISH_REQUEST_CODE_NEW_TASK);
                break;
            case com.tt.sharedbaseclass.R.id.header_view_voice_input:
                if (!IntentUtil.voiceInput(getActivity(), "en-US")) {
                    Log.i("Render", "device does not support speech to text");
                    Toast t = Toast.makeText(getActivity(),
                      com.tt.sharedbaseclass.R.string.toast_content_your_device_is_not_support_speech_to_text,
                      Toast.LENGTH_SHORT);
                    t.show();
                }
                break;
            case R.id.header_view_search:
                navigateToSearchFragment();
                break;
            case com.tt.sharedbaseclass.R.id.left_drawer_group_finished:
                onLeftDrawerGroupFinishedClick();
                break;
            case com.tt.sharedbaseclass.R.id.left_drawer_create_new_group:
                onLeftDrawerCreateNewGroupClick();
                break;
            case com.tt.sharedbaseclass.R.id.left_drawer_setting:

                break;
            case com.tt.sharedbaseclass.R.id.left_drawer_feedback_help:

                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void navigateToSearchFragment() {
        TasksContainFragment searchFragment = new TasksContainFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE, Constant.FRAGMENT_TYPE.TASKS_CONTAIN_SEARCH_FRAGMENT.value());
        searchFragment.setArguments(args);
        searchFragment.setEnterTransition(new Fade().setDuration(250L));
        MainActivity.navigateTo(searchFragment, getFragmentManager());
    }

    protected void onMainMenuClick() {
        if (mDrawerLayout != null) {
            if (mIsLeftDrawerOpened) {
                mDrawerLayout.closeDrawer(mLeftDrawer);
            } else {
                mDrawerLayout.openDrawer(mLeftDrawer);
            }
        }
    }

    protected void onMainMenuAnimation(View mainMenu, float drawerViewSlideOffset) {
        mainMenu.setRotation(drawerViewSlideOffset * 90);
    }

    private void onLeftDrawerCreateNewGroupClick() {
        String title = getResources().getString(com.tt.sharedbaseclass.R.string.alert_dialog_title_new_group);
        String message = getResources().getString(com.tt.sharedbaseclass.R.string.alert_dialog_message_add_new_group);
        AlertDialog.Builder builder = getDefaultAlertDialogBuilder(title, message);
        final EditText editText = new EditText(getActivity());
        editText.setSingleLine(true);
        builder.setView(editText)
          .setNegativeButton(com.tt.sharedbaseclass.R.string.edit_task_fragment_alert_dialog_discard, null)
          .setPositiveButton(com.tt.sharedbaseclass.R.string.edit_task_fragment_alert_dialog_save, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  addNewGroup(editText);
              }
          }).show();
    }

    protected void addNewGroup(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            GroupBean groupBean = new GroupBean();
            groupBean.setGroup(editText.getText().toString());
            mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.value(),
              Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP, null, groupBean, null,
              Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP);
            if (mLruCache.get(Constant.BundelExtra.EXTRAL_GROUPS_BEANS) != null) {
                mLruCache.get(Constant.BundelExtra.EXTRAL_GROUPS_BEANS).add(groupBean);
            }
            // TODO: 2016/5/23 show loading view
        } else {
            Toast.makeText(getActivity(), com.tt.sharedbaseclass.R.string.edit_task_add_new_group_please_input_new_group_name,
              Toast.LENGTH_SHORT).show();
        }
    }

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
        super.onItemClickListener(view, adapterType, positionClickedBefore, position);
       if (adapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY) {
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
        super.onItemLongClickListener(view, adapterType, position);
        if (adapterType == Constant.RENDER_ADAPTER_TYPE.LEFT_DRAWER_TASKS_CATEGORY) {
            GroupBean groupBean = (GroupBean)mRenderObjectBeansGroups.get(position);

            if (groupBean.getGroup().equals(Constant.RenderDbHelper.GROUP_NAME_MY_TASK)) {
                return;
            }
            String alertTitle = getResources().getString(R.string.alert_dialog_title_are_you_sure);
            int id = groupBean.getId();
            String alertMessage = "Delete this group?";
            int action = Constant.RenderServiceHelper.ACTION.ACTION_DELETE_GROUP.value();
            String[] wheres = new String[]{id+"", groupBean.getGroup()};
            showDeleteAlertDialog(action, alertTitle, alertMessage, wheres, position);
        }
    }

    @Override
    protected void onUpdateBeanSuccess(long row, int requestCode, int resultCode) {
        super.onUpdateBeanSuccess(row, requestCode, resultCode);
        switch (resultCode) {
            case Constant.RenderServiceHelper.RESULT_CODE_DELETE_GROUP_SUCCESS:
                if (requestCode >= 0) {
                // TODO: 2016/5/29 remove group bean
                    groupDeleted(row, requestCode);
                }
                break;
            case Constant.RenderServiceHelper.RESULT_CODE_UPDATE_GROUP_SUCCESS:
                if (resultCode >= 0 ) {
                // TODO: 5/31/16 group name changed
                }
                break;
        }

    }

    private void groupDeleted(long row, int requestCode) {
        GroupBean groupBean = (GroupBean) mRenderObjectBeansGroups.get(requestCode);
        RenderObjectBeans tasks = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+groupBean.getGroup());
        if (mLeftDrawerGroupsAdapter.getPositionClickedBefore() == requestCode) {
            mTasksContainerAdapter.addAllBeans(new RenderObjectBeans());
            mHeaderViewTitle.setText(Constant.RenderDbHelper.GROUP_NAME_MY_TASK);
        }
        if (tasks != null) {
            mLruCache.remove(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+groupBean.getGroup());
        }
        getTasksByGroupName(Constant.RenderDbHelper.GROUP_NAME_MY_TASK
          , Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED);
        mLeftDrawerGroupsAdapter.removeBean(requestCode, true);
        mLeftDrawerGroupsAdapter.setPositionClicke(RenderRecycleViewAdapter.POSITION_GROUP_FINISHED - 1);
    }

    protected void onAddNewGroupSuccess(long row, int requestCode, int resultCode) {
        Log.i("Render", "success to add new group");
        mLeftDrawerGroupsAdapter.notifyDataSetChanged();
    }

    protected void onAddNewGroupFail(int requestCode, int resultCode) {
        Log.i("Render", "fail to add new group");
        mRenderObjectBeansGroups.remove(mRenderObjectBeansGroups.size() - 1);
        mLeftDrawerGroupsAdapter.notifyDataSetChanged();
    }

    private void navigateToEditFragment(TaskBean taskBean) {
        int position = mTasksContainerAdapter.findBeanPosition(taskBean);
        navigateToEditFragment(Constant.FRAGMENT_TYPE.EDIT_TASK_FRAGMENT.value(), taskBean, position);
    }

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

    protected TaskBean mVoiceInputBean;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentUtil.REQUEST_CODE_VOICE_INPUT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> text = data
                      .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // TODO: 6/7/16 add new task to db
                    TaskBean newBean = new TaskBean();
                    newBean.setTaskContent(text.get(0));
                    mVoiceInputBean = newBean;
                    mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_ADD_NEW_TASK.value()
                      ,Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS
                      ,null
                      ,newBean
                      ,null
                      ,Constant.RenderServiceHelper.REQUEST_CODE_INSERT_TASK_BEAN);
                }
                break;
        }
    }

    @Override
    public void onFinishedWithResult(int requestCode, int resultCode, Bundle bundle) {
        super.onFinishedWithResult(requestCode, resultCode, bundle);
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

    private static class AddNewBeanCallBack extends RenderCallback {

        TasksContainWithDrawerViewFragment mContext;
        private AddNewBeanCallBack(TasksContainWithDrawerViewFragment context) {
            mContext = context;
        }
        @Override
        public void onHandleSelectSuccess(RenderObjectBeans renderObjectBeans,
                                          int requestCode, int resultCode) {

        }

        @Override
        public void onHandleUpdateSuccess(long row, int requestCode, int resultCode) {
            if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP
              && resultCode == Constant.RenderServiceHelper.RESULT_CODE_INSERT_GROUP_SUCCESS) {
                mContext.onAddNewGroupSuccess(row, requestCode, resultCode);
            } else if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE_INSERT_TASK_BEAN
              && resultCode == Constant.RenderServiceHelper.RESULT_CODE_INSERT_TASK_SUCCESS) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.BundelExtra.EXTRA_TASK_BEAN, mContext.mVoiceInputBean);
                mContext.onFinishedWithResult(Constant.BundelExtra.FINISH_REQUEST_CODE_NEW_TASK
                  , Constant.BundelExtra.FINISH_RESULT_CODE_SUCCESS, bundle);
                mContext.mVoiceInputBean = null;
            }
        }

        @Override
        public void onHandleFail(int requestCode, int resultCode) {
            if (requestCode == Constant.RenderServiceHelper.REQUEST_CODE__INSERT_NEW_GROUP
              && resultCode == Constant.RenderServiceHelper.RESULT_CODE_FAIL) {
                // TODO: 2016/5/23 dismiss loading view
                mContext.onAddNewGroupFail(requestCode, resultCode);
            } else {
                Log.e("Render", "error request code or result code");
            }
        }
    }

}
