package com.tt.sharedbaseclass.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.GroupBean;
import com.tt.sharedbaseclass.model.RenderCallback;
import com.tt.sharedbaseclass.model.RenderObjectBeans;
import com.tt.sharedbaseclass.model.TaskBean;
import com.tt.sharedutils.DeviceUtil;
import com.tt.sharedutils.IntentUtil;

import java.util.ArrayList;

/**
 * Created by zhengguo on 2016/5/27.
 */
public abstract class TaskContainFragmentBase extends FragmentBaseWithSharedHeaderView
        implements View.OnClickListener {

    private static long INTERVAL_OF_DOUBLE_BACK_PRESSED_DOUBLE_CLICK = 500;
    private long mFirstBackPressedTime = 0;

    protected LinearLayout mLeftDrawerGroupFinished;
    protected LinearLayout mLeftDrawerCreateNewGroup;
    protected LinearLayout mLeftDrawerSetting;
    protected LinearLayout mLeftDrawerFeedback;

    private AddNewBeanCallBack mAddNewBeanCallBack;



    protected LruCache<String, RenderObjectBeans> mLruCache;
    protected boolean mIsGroupCached = false;
    protected boolean mIsMyTasksCached = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLruCache = new LruCache<>((int) DeviceUtil.getMaxMemory()/8);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            mLeftDrawerGroupFinished = (LinearLayout) view.findViewById(R.id.left_drawer_group_finished);
            mLeftDrawerCreateNewGroup = (LinearLayout) view.findViewById(R.id.left_drawer_create_new_group);
            mLeftDrawerSetting = (LinearLayout) view.findViewById(R.id.left_drawer_setting);
            mLeftDrawerFeedback = (LinearLayout) view.findViewById(R.id.left_drawer_feedback_help);

            mHeaderViewVoiceInput.setOnClickListener(this);
            mLeftDrawerGroupFinished.setOnClickListener(this);
            mLeftDrawerCreateNewGroup.setOnClickListener(this);
        }
    }

    @Override
    public void initServices() {
        super.initServices();
        mAddNewBeanCallBack = new AddNewBeanCallBack(this);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION__ADD_NEW_GROUP.toString(),
          mAddNewBeanCallBack);
        mRenderService.addHandler(Constant.RenderServiceHelper.ACTION.ACTION_ADD_NEW_TASK.toString(),
          mAddNewBeanCallBack);
    }

    @Override
    public void fetchData() {
        Log.i("Render", "fetchData");
        mIsGroupCached = mLruCache.get(Constant.BundelExtra.EXTRAL_GROUPS_BEANS) == null ? false : true;
        mIsMyTasksCached = mLruCache.get(Constant.BundelExtra.EXTRA_RENDER_OBJECT_BEAN+Constant.RenderDbHelper.GROUP_NAME_MY_TASK) == null ? false : true;
        getGroupsExceptFinished(Constant.RenderServiceHelper.REQUEST_CODE_DEFAULT);
    }

    protected void getTasksByGroupName(String GroupName, int requestCode) {
        if (!mIsMyTasksCached) {
            mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_TASKS_BY_GROUP_NAME.value(),
              Constant.RenderDbHelper.EXTRA_TABLE_NAME_TASKS,
              null,
              null,
              new String[]{GroupName},
              requestCode);
        }
    }

    protected void getGroupsExceptFinished(int requestCode) {
        if (!mIsGroupCached) {
            mRenderService.getOrUpdate(Constant.RenderServiceHelper.ACTION.ACTION_GET_ALL_GROUPS.value(),
                    Constant.RenderDbHelper.EXTRA_TABLE_NAME_GROUP,
                    null,
                    null,
                    null,
                    requestCode);
        }
        //must getGroups firstly here, then get tasks
        getTasksByGroupName(Constant.RenderDbHelper.GROUP_NAME_MY_TASK
          , Constant.RenderServiceHelper.REQUEST_CODE_GET_ALL_TASKS_BEANS_EXCEPT_FINISHED);
    }

    protected abstract void getTasksSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);

    protected abstract void getGroupsSuccess(RenderObjectBeans renderObjectBeans, int requestCode, int resultCode);

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

        int viewId = view.getId();
        if (viewId == R.id.header_view_main_menu) {
            onMainMenuClick();
        } else if (viewId == R.id.header_view_add_new_task) {
            navigateToEditFragment(Constant.FRAGMENT_TYPE.NEW_EDIT_TASK_FRAGMENT.value()
                    , null
                    , Constant.BundelExtra.FINISH_REQUEST_CODE_NEW_TASK);
        } else if (viewId == R.id.header_view_voice_input) {
            if (!IntentUtil.voiceInput(getActivity(), "en-US")) {
                Log.i("Render", "device does not support speech to text");
                Toast t = Toast.makeText(getActivity(),
                  R.string.toast_content_your_device_is_not_support_speech_to_text,
                  Toast.LENGTH_SHORT);
                t.show();
            }
        } else if (viewId == R.id.left_drawer_group_finished ) {
            onLeftDrawerGroupFinishedClick();
        } else if (viewId == R.id.left_drawer_create_new_group) {
            onLeftDrawerCreateNewGroupClick();
        } else if (viewId == R.id.left_drawer_setting) {

        } else if (viewId == R.id.left_drawer_feedback_help) {

        }
    }

    protected abstract void onMainMenuClick();

    protected abstract void onLeftDrawerGroupFinishedClick();

    private void onLeftDrawerCreateNewGroupClick() {
        String title = getResources().getString(R.string.alert_dialog_title_new_group);
        String message = getResources().getString(R.string.alert_dialog_message_add_new_group);
        AlertDialog.Builder builder = getDefaultAlertDialogBuilder(title, message);
        final EditText editText = new EditText(getActivity());
        editText.setSingleLine(true);
        builder.setView(editText)
                .setNegativeButton(R.string.edit_task_fragment_alert_dialog_discard, null)
                .setPositiveButton(R.string.edit_task_fragment_alert_dialog_save, new DialogInterface.OnClickListener() {
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

    protected abstract void onAddNewGroupSuccess(long row, int requestCode, int resultCode);

    protected abstract void onAddNewGroupFail(int requestCode, int resultCode);

    protected abstract void navigateToEditFragment(int fragmentType, TaskBean taskBean, int requestCode);

    protected void onMainMenuAnimation(View mainMenu, float drawerViewSlideOffset) {
        mainMenu.setRotation(drawerViewSlideOffset * 90);
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
    public void onDestroy() {
        super.onDestroy();
        if (mLruCache != null) {
            mLruCache.evictAll();
            mLruCache = null;
        }
    }

    private static class AddNewBeanCallBack extends RenderCallback {

        TaskContainFragmentBase mContext;
        private AddNewBeanCallBack(TaskContainFragmentBase context) {
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
