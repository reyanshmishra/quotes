package com.motivational.quotes.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.motivational.quotes.BuildConfig;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.ImageFilePath;

public class VideoEditActivity extends AppCompatActivity {

    private static final String TAG = VideoEditActivity.class.getSimpleName();
    private String imageUri = "android.resource://" + BuildConfig.APPLICATION_ID + "/wolf_logo.png";
    private String mVideoUrl;
    private final String[] command = {"-i",
            mVideoUrl,
            "-i", imageUri,
            "-filter_complex",
            "overlay=x=(main_w-overlay_w-12):y=(main_h-overlay_h)-8",
            "/storage/emulated/0/tes1.mp4"
    };

    private Button mPickVideo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);
        mPickVideo = findViewById(R.id.pick_video);
        mPickVideo.setOnClickListener(v -> {
            Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
            mediaChooser.setType("video/*");
            startActivityForResult(mediaChooser, 56);
        });

        initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 56) {
            mVideoUrl = ImageFilePath.getPath(this, data.getData());
            Log.d("AAAAA ", "" + mVideoUrl);
        }
    }

    private void initUI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.processing);
    }



    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.device_not_supported))
                .setMessage(getString(R.string.device_not_supported_message))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .create()
                .show();

    }

}
