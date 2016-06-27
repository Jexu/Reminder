package com.tt.sharedbaseclass.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.constant.Constant;
import com.tt.sharedbaseclass.model.RenderDbService;
import com.tt.sharedutils.StringUtil;

import static com.tt.sharedbaseclass.R.id.header_view_save_task;

/**
 * Created by zhengguo on 2016/5/17.
 */
public abstract class FragmentBaseWithSharedHeaderView extends RenderFragmentBase {

    protected ImageView mHeaderViewMainMenu, mHeaderViewLeftArrow, mHeaderViewVoiceInput, mHeaderViewAddNewTask, mHeaderViewSaveTask;
    protected SearchView mHeaderViewSearch;
    protected TextView mHeaderViewTitle;
    protected ImageView mHeaderViewSearchBtn;
    protected RenderDbService mRenderDbService;

    protected int mFragmentType;


    public FragmentBaseWithSharedHeaderView() {
        super();
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mFragmentType = args.getInt(Constant.BundelExtra.EXTRA_FRAGMENT_TYPE);
        }
        initServices();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeaderViewMainMenu = (ImageView) view.findViewById(R.id.header_view_main_menu);
        mHeaderViewLeftArrow = (ImageView) view.findViewById(R.id.header_view_left_arrow);
        mHeaderViewTitle = (TextView) view.findViewById(R.id.header_view_title);
        mHeaderViewVoiceInput = (ImageView) view.findViewById(R.id.header_view_voice_input);
        mHeaderViewSearchBtn = (ImageView) view.findViewById(R.id.header_view_search_btn);
        mHeaderViewAddNewTask = (ImageView) view.findViewById(R.id.header_view_add_new_task);
        mHeaderViewSaveTask = (ImageView) view.findViewById(header_view_save_task);
        mHeaderViewSearch = (SearchView) view.findViewById(R.id.header_view_search);

    }

    public void initServices() {
        mRenderDbService = new RenderDbService(getActivity());
    }

    public void destroyServices() {
        if (mRenderDbService != null) {
            Log.i("Render", "service destroyed");
            mRenderDbService.removeAllHandlers();
            mRenderDbService.destroyService();
        }
    }

    protected AlertDialog.Builder getDefaultAlertDialogBuilder(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        SpannableString ssTitle = null;
        if (!StringUtil.isEmpty(title)) {
            ssTitle = new SpannableString(title);
            ssTitle.setSpan(new ForegroundColorSpan(getResources()
                            .getColor(R.color.colorPrimaryDark)),
                    0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setTitle(ssTitle);
        }
        SpannableString ssMessage = null;
        if (!StringUtil.isEmpty(message)) {
            ssMessage = new SpannableString(message);
            ssMessage.setSpan(new ForegroundColorSpan(getResources()
                            .getColor(R.color.colorPrimaryDark)),
                    0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setMessage(ssMessage);
        }
        return builder;
    }

    @Override
    public Transition enterTransition() {
        Transition transition = super.enterTransition();
        return transition.setDuration(0xc8L).setInterpolator(new AccelerateInterpolator());
    }

    @Override
    public Transition exitTransition() {
        return super.exitTransition().setDuration(0xc8L).setInterpolator(new AccelerateInterpolator());
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
