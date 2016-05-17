package com.tt.sharedbaseclass.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.tt.sharedbaseclass.R;
import com.tt.sharedbaseclass.listener.OnFragmentInteractionListener;

/**
 * Created by zhengguo on 2016/5/17.
 */
public class FragmentBaseWithSharedHeaderView extends Fragment  {

    private OnFragmentInteractionListener mListener;
    protected ImageView mHeaderViewMainMenu, mHeaderViewLeftArrow, mHeaderViewVoiceInput, mHeaderViewAddNewTask;
    protected TextView mHeaderViewTitle;
    protected SearchView mHeaderViewSearch;

    public FragmentBaseWithSharedHeaderView() {
        // Required empty public constructor
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
