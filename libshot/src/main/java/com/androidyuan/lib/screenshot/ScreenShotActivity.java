package com.androidyuan.lib.screenshot;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by wei on 16-9-18.
 * <p>
 *    there is  totally transparent,only has a record permission dialog.
 *    if you want to screenshot other applications,might you need to use this activity to take screenshot.
 */
public class ScreenShotActivity extends Activity {

    public static final int REQUEST_MEDIA_PROJECTION = 0x12f81ac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        setTheme(android.R.style.Theme_Dialog);//this line cause a problem, activity background wasn't transparent but black.
        super.onCreate(savedInstanceState);

        //here is a transparent activity,and previous activity will not be called Activity#onPause().
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0f);

        requestScreenShotPermission();
    }


    public void requestScreenShotPermission() {
        if (Build.VERSION.SDK_INT >= 21) {
            startActivityForResult(createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    private Intent createScreenCaptureIntent() {
        //here used media_projection instead of Context.MEDIA_PROJECTION_SERVICE to  make it successfully build on low api.
        return ((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent();
    }

    private void toast(String str) {
        Toast.makeText(ScreenShotActivity.this, str, Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION: {
                if (resultCode == RESULT_OK && data != null) {
                    Shotter shotter = new Shotter(ScreenShotActivity.this, resultCode, data);
                    shotter.startScreenShot(new Shotter.OnShotListener() {
                        @Override
                        public void onFinish() {
                            setResult(RESULT_OK);
                            finish(); // don't forget finish activity
                        }
                    });
                } else if (resultCode == RESULT_CANCELED) {
                    setResult(RESULT_CANCELED);
                    finish();
//                    toast("shot cancel , please give permission.");
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
//                    toast("unknow exceptions!");
                }
            }
        }
    }


}