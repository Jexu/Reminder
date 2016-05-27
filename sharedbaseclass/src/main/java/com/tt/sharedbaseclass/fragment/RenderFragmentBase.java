package com.tt.sharedbaseclass.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

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
    public String getFragmentTag() {
        return this.mFragmentTag;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
        Log.e("Render", "onStart");
        if (mFragmentRegister != null) {
            mFragmentRegister.onFragmentRegistered(this);
        }
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
        Log.e("Render", "onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentRegister = null;
    }

}
