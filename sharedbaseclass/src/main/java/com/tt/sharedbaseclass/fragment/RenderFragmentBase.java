package com.tt.sharedbaseclass.fragment;

import android.os.Bundle;

/**
 * Created by zhengguo on 5/25/16.
 */
public interface RenderFragmentBase {
  void initServices();
  void fetchData();
  void navigateToFragmentForResultCode(RenderFragmentBase context, int requestCode);
  void finish();
  void finishWithResultCode(int resultCode, Bundle bundle);
  void onFinishedWithResult(int requestCode, int resultCode, Bundle bundle);
  boolean onBackPressed();
  void destroyServices();
}
