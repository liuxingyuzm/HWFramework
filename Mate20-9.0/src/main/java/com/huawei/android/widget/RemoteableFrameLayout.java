package com.huawei.android.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

@RemoteViews.RemoteView
public class RemoteableFrameLayout extends FrameLayout {
    public RemoteableFrameLayout(Context context) {
        this(context, null);
    }

    public RemoteableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @RemotableViewMethod
    public void callRemoteableMethod(Bundle bundle) {
        onCallRemoteable(bundle);
    }

    /* access modifiers changed from: protected */
    public void onCallRemoteable(Bundle bundle) {
    }
}
