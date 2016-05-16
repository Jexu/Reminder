package com.tt.reminder.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.tt.reminder.R;
import com.tt.sharedbaseclass.listener.OnFragmentInteractionListener;

public class EditTaskFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private static EditTaskFragment mEditTaskFragment;
    private ImageView mHeaderViewMainMenu, mHeaderViewLeftArrow, mHeaderViewVoiceInput, mHeaderViewAddNewTask;
    private TextView mHeaderViewTitle;
    private SearchView mHeaderViewSearch;

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance() {
        if (mEditTaskFragment == null) {
            mEditTaskFragment = new EditTaskFragment();
        }
        return mEditTaskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        mHeaderViewMainMenu = (ImageView) contentView.findViewById(R.id.header_view_main_menu);
        mHeaderViewLeftArrow = (ImageView) contentView.findViewById(R.id.header_view_left_arrow);
        mHeaderViewTitle = (TextView) contentView.findViewById(R.id.header_view_title);
        mHeaderViewVoiceInput = (ImageView) contentView.findViewById(R.id.header_view_voice_input);
        mHeaderViewSearch = (SearchView) contentView.findViewById(R.id.header_view_search);
        mHeaderViewAddNewTask = (ImageView) contentView.findViewById(R.id.header_view_add_new_task);
        mHeaderViewMainMenu.setVisibility(View.GONE);
        mHeaderViewLeftArrow.setVisibility(View.VISIBLE);
        mHeaderViewTitle.setText(R.string.header_view_title_new_task);
        mHeaderViewVoiceInput.setVisibility(View.GONE);
        mHeaderViewSearch.setVisibility(View.GONE);
        mHeaderViewAddNewTask.setVisibility(View.GONE);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeaderViewLeftArrow.setOnClickListener(this);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_view_left_arrow:
                onBack();
                break;
        }
    }

    private void onBack() {
        //getFragmentManager().popBackStack();
        getActivity().onBackPressed();
    }

}
