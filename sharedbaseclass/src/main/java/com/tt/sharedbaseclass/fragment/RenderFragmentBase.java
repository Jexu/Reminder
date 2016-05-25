package com.tt.sharedbaseclass.fragment;

/**
 * Created by zhengguo on 5/25/16.
 */
public interface RenderFragmentBase {
  void initServices();
  void fetchData();
  boolean onBackPressed();
  void destroyServices();
}
