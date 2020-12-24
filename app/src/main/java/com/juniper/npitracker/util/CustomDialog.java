package com.juniper.npitracker.util;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.juniper.npitracker.R;

import java.util.List;

/**
 * Created by koteswara on 22/11/16.
 */

public class CustomDialog extends Dialog {
    Animation animation;
    private ImageView iv;
    TextView tv;
        Context context;

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }
    public CustomDialog(Context context, String message) {
        super(context);
        this.context=context;
       /* WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_VERTICAL;
        getWindow().setAttributes(wlmp);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(Color.TRANSPARENT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        int width=400;
        int height=400;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        layout.setLayoutParams(params);
        iv = new ImageView(context);
        tv=new TextView(context);
        iv.setBackgroundResource(R.drawable.log);

        tv.setText("Loading...");

        layout.addView(iv, params);
        layout.addView(tv,params);
        addContentView(layout, params);*/
        setContentView(R.layout.customprogressdialog);
        iv=(ImageView) findViewById(R.id.cutomalert_image);
        tv=(TextView)findViewById(R.id.customalert_text);
        tv.setText(message);
       // animation = AnimationUtils.loadAnimation(context,R.anim.scale);
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel) dismiss();
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);
        iv.setAnimation(anim);
        iv.startAnimation(anim);
    }


    @Override
    public void cancel() {
        super.cancel();
        dismiss();
    }
}
