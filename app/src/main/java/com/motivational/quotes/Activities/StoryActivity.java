package com.motivational.quotes.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.motivational.quotes.Dialogs.EditTextDialog;
import com.motivational.quotes.Dialogs.LoaderDialog;
import com.motivational.quotes.Fragments.PhotosFragment;
import com.motivational.quotes.Fragments.QuotesFragment;
import com.motivational.quotes.ImagePicker.GalleryFragment;
import com.motivational.quotes.Models.MyQuote;
import com.motivational.quotes.Models.Photo;
import com.motivational.quotes.Models.SinglePhoto;
import com.motivational.quotes.Networking.Retro;
import com.motivational.quotes.Networking.RetroInterface;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;
import com.motivational.quotes.Utils.Constants;
import com.motivational.quotes.Utils.Logger;
import com.motivational.quotes.Utils.MyImageView;
import com.motivational.quotes.Utils.OnDragTouchListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryActivity extends AppCompatActivity implements EditTextDialog.OnTextEditingDone, QuotesFragment.OnFragmentInteractionListener {

    private MyImageView mAuthorImage;
    private RelativeLayout mOverlayLayout;
    private TextView mQuoteText;
    private RelativeLayout mMainLayout;
    private LoaderDialog mLoaderDialog;
    private CompositeDisposable mCompositeDisposable;
    private int colors[];
    private ArrayList<Fragment> mFragments;
    private LinearLayout mTWELogo;
    private long time;
    private VerticalSeekBar mVerticalSeekBar;
    private VerticalSeekBar mBlurSeekBar;
    private SeekBar mTextSizeSeekBar;
    private RetroInterface mRetroInterface;

    private DatabaseReference mDatabaseReference;
    private MyQuote myQuote;

    private TextView mBrandName;
    private LinearLayout mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        mFragments = new ArrayList<>();
        mLoaderDialog = new LoaderDialog(this);
        mCompositeDisposable = new CompositeDisposable();
        colors = Common.getContext().getResources().getIntArray(R.array.colors_);
        mAuthorImage = findViewById(R.id.image);
        mOverlayLayout = findViewById(R.id.over_lay);
        mQuoteText = findViewById(R.id.quote);
        mMainLayout = findViewById(R.id.main_layout);
        mBrandName = findViewById(R.id.brand_name);
        mHolder = findViewById(R.id.view_holder);
        findViewById(R.id.gallery).setOnClickListener(v -> addFragment(R.id.fragment_holder, new GalleryFragment()));

        mTWELogo = findViewById(R.id.quotesLogoText);
        mTWELogo.setOnTouchListener(new OnDragTouchListener(mTWELogo));
        mHolder.setOnTouchListener(new OnDragTouchListener(mHolder));


        mRetroInterface = Retro.getRetro().create(RetroInterface.class);

        mVerticalSeekBar = findViewById(R.id.slider_1);
        mBlurSeekBar = findViewById(R.id.slider_2);
        mTextSizeSeekBar = findViewById(R.id.font_size_seekbar);

        findViewById(R.id.save).setOnClickListener(v -> {
            if (!Common.isKitkat() && checkAndRequestPermissions()) {
                saveAndShare();
            } else {
                saveAndShare();
            }
        });

        mTextSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mQuoteText.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float p = (float) progress / 100f;
                mOverlayLayout.setAlpha(p);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBlurSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float p = progress / 10;
                if (p > 0 && mBitmap != null) {
                    Bitmap bitmap = Common.blurImage(mBitmap, p, 0);
                    Logger.log("1 BITMAP DETAILS WIDTH:-" + bitmap.getWidth() + " HEIGHT:-" + bitmap.getHeight());
                    Logger.log("2 BITMAP DETAILS WIDTH:-" + mBitmap.getWidth() + " HEIGHT:-" + mBitmap.getHeight());

                    mAuthorImage.setDrawable(bitmap);
                } else {
                    mAuthorImage.setDrawable(mBitmap);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mQuoteText.setOnClickListener(v -> editText(mQuoteText.getText().toString()));

        findViewById(R.id.save).setOnClickListener(v -> {
            if (!Common.isKitkat() && checkAndRequestPermissions()) {
                saveAndShare();
            } else {
                saveAndShare();
            }
        });

        findViewById(R.id.image_search).setOnClickListener(v -> addFragment(R.id.fragment_holder, new PhotosFragment()));
        findViewById(R.id.change_overlay).setOnClickListener(v -> setOverlayGradient());
        setOverlayGradient();
        loadRandomPhoto();
    }


    private void loadRandomPhoto() {
        mLoaderDialog.show();
        mRetroInterface.randomPhoto().enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    Picasso.with(Common.getContext()).load(response.body().urls.regular).into(mTarget);
                } else {
                    Toast.makeText(StoryActivity.this, "Failed to load picture.", Toast.LENGTH_SHORT).show();
                    mLoaderDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                mLoaderDialog.dismiss();
                Toast.makeText(StoryActivity.this, "Failed to load picture.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editText(String text) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        EditTextDialog editTextDialog = new EditTextDialog();
        Bundle bundle = new Bundle();
        bundle.putString("QUOTE_TEXT", text);
        editTextDialog.setArguments(bundle);
        editTextDialog.show(fragmentTransaction, "EDIT_TEXT");
    }


    private void saveAndShare() {
        mLoaderDialog.show();
        mCompositeDisposable.add(Observable.fromCallable(() -> createBitmap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String path) {
                        mLoaderDialog.dismiss();
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        Uri uri = Uri.parse("file://" + path);
                        sharingIntent.setType("image/*");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.exp("" + e.getMessage());
                        mLoaderDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        mLoaderDialog.dismiss();
                    }
                })
        );
    }

    private String createBitmap() {
        mMainLayout.buildDrawingCache(true);
        Bitmap bitmap = mMainLayout.getDrawingCache(true);
        return saveBitmap(bitmap);
    }


    private String saveBitmap(Bitmap bitmap) {

        try {
            String path = Environment.getExternalStorageDirectory() + File.separator + "MyQuote";
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdir();
            }

            OutputStream fOut = null;
            File file = new File(path, "/MyQuote" + System.currentTimeMillis() + ".png");
            if (!file.exists()) {
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            mMainLayout.destroyDrawingCache();
            if (mDatabaseReference != null) {
                mDatabaseReference.removeValue();
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            Logger.exp("AAAAAA" + e);
            return "";
        }
    }


    private void loadPhoto(String photoId) {
        mLoaderDialog.show();

        if (photoId.contains("pixabay.com")) {
            Picasso.with(Common.getContext()).load(photoId).into(mTarget);
        } else {
            mRetroInterface.getPhotoById(Retro.Urls.baseUrl + Retro.Urls.photoById + photoId, Retro.Urls.clientId).enqueue(new Callback<SinglePhoto>() {
                @Override
                public void onResponse(Call<SinglePhoto> call, Response<SinglePhoto> response) {
                    if (response.isSuccessful()) {
                        Picasso.with(Common.getContext()).load(response.body().urls.regular).into(mTarget);
                    } else {
                        mLoaderDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SinglePhoto> call, Throwable t) {
                    mLoaderDialog.dismiss();
                }
            });

        }
    }

    boolean blackAndWhite = true;

    private void setOverlayGradient() {
        GradientDrawable gd;

        if (blackAndWhite) {
            gd = new GradientDrawable();
            gd.setColor(Color.BLACK);
            blackAndWhite = false;
        } else {
            gd = new GradientDrawable(randomEnum(GradientDrawable.Orientation.class), new int[]{getRandom(colors), getRandom(colors)});
            blackAndWhite = true;
        }

        gd.setCornerRadius(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mOverlayLayout.setBackground(gd);
        }
    }


    private void access() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("quotes");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myQuote = dataSnapshot.getChildren().iterator().next().getValue(MyQuote.class);
                if (myQuote != null) {
                    Picasso.with(getApplicationContext()).load(myQuote.author_image).placeholder(R.color.black).into(mTarget);
                    mQuoteText.setText(myQuote.quote);
                    mDatabaseReference = dataSnapshot.getChildren().iterator().next().getRef();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static int getRandom(int[] array) {

        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private Bitmap mBitmap;
    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mAuthorImage.setDrawable(bitmap);
            mBitmap = bitmap;
            mLoaderDialog.dismiss();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Logger.exp("Logger ");
            mLoaderDialog.show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            mLoaderDialog.show();
        }
    };

    @Override
    public void onBackPressed() {
        mLoaderDialog.dismiss();
        if (mFragments.isEmpty()) {
            super.onBackPressed();
        } else {
            removeFragment();
        }
    }


    private void removeFragment() {
        if (!mFragments.isEmpty()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(mFragments.get(mFragments.size() - 1));
            fragmentTransaction.commit();
            mFragments.remove(mFragments.size() - 1);
        }
    }

    private void addFragment(int containerId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId, fragment);
        fragmentTransaction.commit();
        mFragments.add(fragment);
    }

    private boolean checkAndRequestPermissions() {
        int modifyAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (modifyAudioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onTextEdited(String text, int color) {
        mQuoteText.setText(text);
        mQuoteText.setTextColor(color);
    }

    @Override
    public void onFragmentInteraction(String quote, String author) {

    }

    @Override
    public void onPhotoSelected(String id) {
        loadPhoto(id);
        removeFragment();
    }

    @Override
    public void onGalleryPhotoSelected(String photoPath) {
        Picasso.with(Common.getContext()).
                load(new File(photoPath)).into(mTarget);
        removeFragment();
    }

    public void changeBackground(int color) {
        if (color != 1) {
            mAuthorImage.setDrawable(null);
            mAuthorImage.setColor(color);
        } else {
            mAuthorImage.setDrawable(mBitmap);
        }
    }
}
