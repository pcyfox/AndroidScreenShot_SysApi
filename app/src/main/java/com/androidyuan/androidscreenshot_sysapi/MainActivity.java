package com.androidyuan.androidscreenshot_sysapi;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidyuan.lib.screenshot.ScreenShotActivity;
import com.androidyuan.lib.screenshot.Shotter;


public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 0x12f81ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
    }

    public void onClickShot(View view) {
        //1.直接启动
        startActivityForResult(new Intent(this, ScreenShotActivity.class),REQ_CODE);
    }

    public void onClickShotFromService(View view) {
        //2.通过service启动
        startService(new Intent(this, ScreenShotService.class));
        finish();
    }

    public void onClickHavePermission(View view){
        requestScreenShotPermission();
    }

    public void requestScreenShotPermission() {
        if (Build.VERSION.SDK_INT >= 21) {
            startActivityForResult(createScreenCaptureIntent(), REQ_CODE);
        }
    }

    private Intent createScreenCaptureIntent() {
        //here used media_projection instead of Context.MEDIA_PROJECTION_SERVICE to  make it successfully build on low api.
        return ((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && data != null) {
                    Shotter shotter = new Shotter(ScreenShotActivity.this, resultCode, data);
                    shotter.startScreenShot(new Shotter.OnShotListener() {
                        @Override
                        public void onFinish() {

                        }
                    });
                } else if (resultCode == RESULT_CANCELED) {

//                    toast("shot cancel , please give permission.");
                } else {

                }
            }
        }
    }

}
