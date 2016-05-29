package com.tt.sharedbaseclass.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderService;
import com.tt.sharedutils.StringUtil;

import static com.tt.sharedbaseclass.R.id.header_view_save_task;

/**
 * Created by zhengguo on 2016/5/17.
 */
public abstract class FragmentBaseWithSharedHeaderView extends RenderFragmentBase {

    protected ImageView mHeaderViewMainMenu, mHeaderViewLeftArrow, mHeaderViewVoiceInput, mHeaderViewAddNewTask, mHeaderViewSaveTask;
    protected TextView mHeaderViewTitle;
    protected ImageView mHeaderViewSearch;
    protected RenderService mRenderService;

    public FragmentBaseWithSharedHeaderView() {
        super();
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initServices();
        Log.e("Render", "Oncreate");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeaderViewMainMenu = (ImageView) view.findViewById(R.id.header_view_main_menu);
        mHeaderViewLeftArrow = (ImageView) view.findViewById(R.id.header_view_left_arrow);
        mHeaderViewTitle = (TextView) view.findViewById(R.id.header_view_title);
        mHeaderViewVoiceInput = (ImageView) view.findViewById(R.id.header_view_voice_input);
        mHeaderViewSearch = (ImageView) view.findViewById(R.id.header_view_search);
        mHeaderViewAddNewTask = (ImageView) view.findViewById(R.id.header_view_add_new_task);
        mHeaderViewSaveTask = (ImageView) view.findViewById(header_view_save_task);
        Log.e("Render", "onViewCreated");

    }

    public void initServices() {
        mRenderService = new RenderService(getActivity());
    }

    public void destroyServices() {
        if (mRenderService != null) {
            mRenderService.removeAllHandlers();
            mRenderService.destroyService();
        }
    }

    protected AlertDialog.Builder getDefaultAlertDialogBuilder(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        SpannableString ssTitle = null;
        if (!StringUtil.isEmpty(title)) {
            ssTitle = new SpannableString(title);
            ssTitle.setSpan(new ForegroundColorSpan(getResources()
                            .getColor(android.R.color.holo_green_dark)),
                    0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setTitle(ssTitle);
        }
        SpannableString ssMessage = null;
        if (!StringUtil.isEmpty(message)) {
            ssMessage = new SpannableString(message);
            ssMessage.setSpan(new ForegroundColorSpan(getResources()
                            .getColor(android.R.color.holo_green_dark)),
                    0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setMessage(ssMessage);
        }
        return builder;
    }



    @Override
    public void onResume() {
        super.onResume();
        Log.e("Render", "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Render", "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Render", "onStop");

    }

    @Override
    public void finish() {
        finishWithResultCode(Constant.BundelExtra.FINISH_RESULT_CODE_DEFAULT, null);
    }

    @Override
    public void finishWithResultCode(int resultCode, Bundle bundle) {
        if (mRenderFragment != null) {
            mRenderFragment.onFinishedWithResult(mRequestCode, resultCode, bundle);
            if (mFragmentRegister != null) {
                mRenderFragment.getmFragmentRegister().onFragmentRegistered(mRenderFragment);
            }
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            }
        } else {
            Log.e("Render", "Fragment does not implement on mRenderFragment" +
                    " or navigate to fragment with function setContextAndReqCode(" +
                    "Fragment context, int requestCode)");
        }
    }

    @Override
    public void onFinishedWithResult(int requestCode, int resultCode, Bundle bundle) {
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyServices();
    }

}
