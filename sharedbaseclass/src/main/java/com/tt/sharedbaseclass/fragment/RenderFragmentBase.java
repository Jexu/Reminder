package com.tt.sharedbaseclass.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import com.tt.sharedbaseclass.listener.OnFragmentRegisterListener;

/**
 * Created by zhengguo on 5/25/16.
 */
public abstract class RenderFragmentBase extends Fragment implements RenderBase {

    protected OnFragmentRegisterListener mFragmentRegister;
    protected RenderFragmentBase mRenderFragment;
    protected int mRequestCode;
    protected String mFragmentTag;

    public RenderFragmentBase() {
        mFragmentTag = System.currentTimeMillis()+"";
    }

    public OnFragmentRegisterListener getmFragmentRegister() {
        return mFragmentRegister;
    }

    @Override
    public void setContextAndReqCode(RenderFragmentBase context, int requestCode) {
        if (context instanceof RenderFragmentBase) {
            mRenderFragment = context;
            mRequestCode = requestCode;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Reander", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Reander", "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public String getFragmentTag() {
        return this.mFragmentTag;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("Reander", "onViewCreated");
        fetchData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Reander", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Render", "onStart");
        if (mFragmentRegister != null) {
            mFragmentRegister.onFragmentRegistered(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Render", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Render", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Render", "onStop");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentRegisterListener) {
            mFragmentRegister = (OnFragmentRegisterListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentRegisterListener");
        }
        Log.i("Render", "onAttach");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Render", "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentRegister = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Render", "onDestroy");
    }
}
