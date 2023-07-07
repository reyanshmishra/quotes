package com.motivational.quotes.Activities;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.motivational.quotes.Dialogs.LoaderDialog;
import com.motivational.quotes.Models.SinglePhoto;
import com.motivational.quotes.Networking.Retro;
import com.motivational.quotes.Networking.RetroInterface;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPhotoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private String photoId;
    private RetroInterface mRetroInterface;
    private LoaderDialog loaderDialog;
    private Bitmap mBitmap;
    private Toolbar mToolbar;
    private RelativeLayout mParentLayout;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        mImageView = findViewById(R.id.imageView);
        photoId = getIntent().getExtras().getString("IMAGE_ID");
        mRetroInterface = Retro.getRetro().create(RetroInterface.class);
        loaderDialog = new LoaderDialog(this);
        mParentLayout = findViewById(R.id.parentLayout);

        mAppBarLayout = findViewById(R.id.id_toolbar_container);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        params.topMargin = Common.getStatusBarHeight(this);
        mAppBarLayout.setLayoutParams(params);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        loadPhoto(photoId);
    }


    private void loadPhoto(String photoId) {
        loaderDialog.show();
        mRetroInterface.getPhotoById(Retro.Urls.baseUrl + Retro.Urls.photoById + photoId, Retro.Urls.clientId).enqueue(new Callback<SinglePhoto>() {
            @Override
            public void onResponse(Call<SinglePhoto> call, Response<SinglePhoto> response) {
                if (response.isSuccessful()) {
                    Picasso.with(Common.getContext())
                            .load(response.body().urls.regular)
                            .placeholder(R.drawable.drawable_placeholder)
                            .into(mTarget);
                } else {
                    loaderDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SinglePhoto> call, Throwable t) {
                loaderDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        loaderDialog.dismiss();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.with(this).cancelRequest(mTarget);
        mBitmap = null;
    }

    Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mImageView.setImageBitmap(bitmap);
            mBitmap = bitmap;
            loaderDialog.dismiss();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            loaderDialog.dismiss();

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_set_wallpaper:
                if (mBitmap != null) {
                    try {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        wallpaperManager.setBitmap(mBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.item_edit:
                Intent intent = new Intent(ViewPhotoActivity.this, EditQuoteActivity.class);
                intent.putExtra("IMAGE_ID", photoId);
                startActivity(intent);
                break;
        }
        return true;
    }
}
