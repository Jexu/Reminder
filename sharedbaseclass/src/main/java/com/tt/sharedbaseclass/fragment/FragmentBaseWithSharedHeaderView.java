package com.tt.sharedbaseclass.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import com.tt.sharedbaseclass.listener.OnFragmentFinishedListener;
import com.tt.sharedbaseclass.listener.OnFragmentInteractionListener;
import com.tt.sharedbaseclass.service.RenderService;
import com.tt.sharedutils.StringUtil;

import static com.tt.sharedbaseclass.R.id.header_view_save_task;
import static com.tt.sharedbaseclass.R.id.submit_area;

/**
 * Created by zhengguo on 2016/5/17.
 */
public abstract class FragmentBaseWithSharedHeaderView extends Fragment implements RenderFragmentBase {

    protected OnFragmentInteractionListener mListener;
    protected OnFragmentFinishedListener mOnFragmentFinishedListener;
    private int mRequestCode;
    protected ImageView mHeaderViewMainMenu, mHeaderViewLeftArrow, mHeaderViewVoiceInput, mHeaderViewAddNewTask, mHeaderViewSaveTask;
    protected TextView mHeaderViewTitle;
    protected SearchView mHeaderViewSearch;
    protected RenderService mRenderService;

    public FragmentBaseWithSharedHeaderView() {
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
        mHeaderViewSearch = (SearchView) view.findViewById(R.id.header_view_search);
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
            mRenderService.destoryService();
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
    public void onStart() {
        super.onStart();
        fetchData();
        Log.e("Render", "onStart");
        if (mListener != null) {
            mListener.onFragmentSelected(this);
        }
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
              + " must implement OnFragmentInteractionListener");
        }
        Log.e("Render", "onAttach");

    }

    public void navigateToFragmentForResultCode(OnFragmentFinishedListener context, int requestCode) {
        if (context instanceof OnFragmentFinishedListener) {
            mOnFragmentFinishedListener = context;
            mRequestCode = requestCode;
        }
    }

    public void finish() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        }
    }

    public void finishWithResultCode(int resultCode, Bundle bundle) {
        if (mOnFragmentFinishedListener != null) {
            mOnFragmentFinishedListener.onFinishedWithResult(mRequestCode, resultCode, bundle);
            finish();
        } else {
            Log.e("Render", "Fragment does not implement on mOnFragmentFinishedListener" +
              " or navigate to fragment with function navigateToFragmentForResultCode(" +
              "Fragment context, int requestCode)");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyServices();
    }

}
