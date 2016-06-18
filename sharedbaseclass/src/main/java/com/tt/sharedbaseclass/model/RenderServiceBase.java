package com.tt.sharedbaseclass.model;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengguo on 6/17/16.
 */
public class RenderServiceBase {
  protected Map<String, Object> mHandlers;
  protected RenderDbHelper mRenderDbHelper;
  protected Context mContext;

  public RenderServiceBase(Context context) {
    mHandlers = new HashMap<>();
    mRenderDbHelper = new RenderDbHelper(context);
    mContext = context;
  }

  public void destroyService() {
    removeAllHandlers();
    mHandlers = null;
  }

  public void addHandler(String action, Object handler) {
    if (mHandlers != null && !mHandlers.containsKey(action)) {
      mHandlers.put(action, handler);
    }
  }

  public void removeHandler(RenderDbCallback handler) {
    if (mHandlers != null) {
      mHandlers.remove(handler);
    }
  }

  public void removeAllHandlers() {
    if (mHandlers != null && mHandlers.size() > 0) {
      mHandlers.clear();
    }
  }

}
