package com.android.wikimedia;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Method;

import static com.android.volley.VolleyLog.TAG;

public class FacadeApplication extends Application {

  private static volatile Context context;

  private static FacadeApplication instance;
  private RequestQueue mRequestQueue;

  public static Context getAppContext() {
    return FacadeApplication.context;
  }

  public static FacadeApplication getInstance() {

    return instance;
  }

  public void onCreate() {
    super.onCreate();
    FacadeApplication.context = getApplicationContext();


    instance = this;
    if(Build.VERSION.SDK_INT>=24) {
      try {
        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
        m.invoke(null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public RequestQueue getRequestQueue() {

    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    return mRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req, String tag) {
    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(req);
  }

  public <T> void addToRequestQueue(Request<T> req) {
    req.setTag(TAG);
    getRequestQueue().add(req);
  }

  public void cancelPendingRequests(Object tag) {
    if (mRequestQueue != null) {
      mRequestQueue.cancelAll(tag);
    }
  }

}
