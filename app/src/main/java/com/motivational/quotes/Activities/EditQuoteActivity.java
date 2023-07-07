package com.motivational.quotes.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.motivational.quotes.AlarmService;
import com.motivational.quotes.Dialogs.EditTextDialog;
import com.motivational.quotes.Dialogs.LoaderDialog;
import com.motivational.quotes.Fragments.EditControllersFragment;
import com.motivational.quotes.Fragments.PhotosFragment;
import com.motivational.quotes.Fragments.QuotesFragment;
import com.motivational.quotes.ImagePicker.GalleryFragment;
import com.motivational.quotes.Models.Photo;
import com.motivational.quotes.Models.SinglePhoto;
import com.motivational.quotes.Networking.Retro;
import com.motivational.quotes.Networking.RetroInterface;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;
import com.motivational.quotes.Utils.Constants;
import com.motivational.quotes.Utils.Logger;
import com.motivational.quotes.Utils.OnDragTouchListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

public class EditQuoteActivity extends AppCompatActivity implements QuotesFragment.OnFragmentInteractionListener, EditControllersFragment.EditImageFragmentListener, EditTextDialog.OnTextEditingDone {

    private ImageView mQuoteImageView;
    private LinearLayout mFragmentsHolder;
    private ArrayList<Fragment> mFragments;
    private String mImageDownloadUrl;
    private RetroInterface mRetroInterface;
    private String photoId;
    private TextView mQuoteText;
    private Bitmap mOgPhotoBitmap;
    private LoaderDialog mLoaderDialog;
    private RelativeLayout mCanvas;
    private boolean mEditAuthor = false;
    private CompositeDisposable mCompositeDisposable;
    private VerticalSeekBar mBrightnessSeekbar;
    private VerticalSeekBar mBlurSeekbar;
    private DatabaseReference mDatabase;
    private DataSnapshot mData;

    private RelativeLayout mOverLay;
    boolean mEdited = false;

    private LinearLayout mViewHolder;
    Iterator<DataSnapshot> mDataSnapshotIterator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_quote);
        mCanvas = findViewById(R.id.canvasHolder);
        mCanvas.setDrawingCacheEnabled(true);
        mCanvas.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        ((Common) Common.getContext()).setPosted(false);
        startService(new Intent(this,AlarmService.class));

        mViewHolder = findViewById(R.id.view_holder);
        mViewHolder.setOnTouchListener(new OnDragTouchListener(mViewHolder));

        mLoaderDialog = new LoaderDialog(this);

        mQuoteImageView = findViewById(R.id.quoteImage);
       /* mQuoteImageView.setOnClickListener(v -> {
            if (filterApplied) {
                mQuoteImageView.setColorFilter(null);
                filterApplied = false;
            } else {

                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                mQuoteImageView.setColorFilter(filter);
                filterApplied = true;
            }
        });*/


        mQuoteText = findViewById(R.id.quoteText);

        mRetroInterface = Retro.getRetro().create(RetroInterface.class);
        mQuoteText.setOnClickListener(v -> {
            mEditAuthor = false;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            EditTextDialog editTextDialog = new EditTextDialog();
            Bundle bundle = new Bundle();
            bundle.putString("QUOTE_TEXT", mQuoteText.getText().toString());
            editTextDialog.setArguments(bundle);
            editTextDialog.show(fragmentTransaction, "EDIT_TEXT");
        });
        mOverLay = findViewById(R.id.over_lay);

        mBlurSeekbar = findViewById(R.id.slider_1);
        mBrightnessSeekbar = findViewById(R.id.slider_2);
        mBlurSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float p = progress / 10;
                if (p > 0 && mOgPhotoBitmap != null) {
                    mQuoteImageView.setImageBitmap(Common.blurImage(mOgPhotoBitmap, p, 8));
                } else {
                    mQuoteImageView.setImageBitmap(mOgPhotoBitmap);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mBrightnessSeekbar.setProgress(60);
        mOverLay.setAlpha(0.6f);
        mBrightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float p = (float) progress / 100f;
                mOverLay.setAlpha(p);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        findViewById(R.id.button_shuffle).setOnClickListener(v -> {
            mLoaderDialog.show();
            loadRandomPhoto();
        });

        mQuoteText.setOnClickListener(v -> editText(mQuoteText.getText().toString()));

        findViewById(R.id.button_save).setOnClickListener(v -> {
            if (!Common.isKitkat() && checkAndRequestPermissions()) {
                saveAndShare();
            } else {
                saveAndShare();
            }
        });
        findViewById(R.id.button_gallery).setOnClickListener(v -> addFragment(R.id.fragment_holder, new GalleryFragment()));
        findViewById(R.id.button_web).setOnClickListener(v -> addFragment(R.id.fragment_holder, new PhotosFragment()));
        findViewById(R.id.button_increase).setOnClickListener(v -> mQuoteText.setTextSize(Common.spFromPixel(mQuoteText.getTextSize()) + Common.spFromPixel(3)));

        findViewById(R.id.button_right).setOnClickListener(v -> nextPost());

        findViewById(R.id.button_left).setOnClickListener(v -> {

        });


        findViewById(R.id.button_decrease).setOnClickListener(v -> {
            mQuoteText.setTextSize(Common.spFromPixel(mQuoteText.getTextSize()) - Common.spFromPixel(3));
        });

        findViewById(R.id.button_expand).setOnClickListener(v -> {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewHolder.getLayoutParams();
            layoutParams.leftMargin = layoutParams.leftMargin - 10;
            layoutParams.rightMargin = layoutParams.rightMargin - 10;
            mViewHolder.setLayoutParams(layoutParams);
        });

        findViewById(R.id.button_collapse).setOnClickListener(v -> {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewHolder.getLayoutParams();
            layoutParams.leftMargin = layoutParams.leftMargin + 10;
            layoutParams.rightMargin = layoutParams.rightMargin + 10;
            mViewHolder.setLayoutParams(layoutParams);
        });

        mFragmentsHolder = findViewById(R.id.relativeFragmentsHolder);
        mFragments = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            photoId = getIntent().getExtras().getString("IMAGE_ID");
            loadPhoto(photoId);
        }

        mCompositeDisposable = new CompositeDisposable();
        mCanvas.post(() -> {
            mCanvas.getLayoutParams().height = mCanvas.getLayoutParams().width;
            RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) mCanvas.getLayoutParams();
            mParams.height = mCanvas.getWidth();
            mCanvas.setLayoutParams(mParams);
        });

        mLoaderDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(postListener);

    }


    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mDataSnapshotIterator = dataSnapshot.child("quotes").getChildren().iterator();
            if (!mEdited) {
                nextPost();
            } else {
                Toast.makeText(EditQuoteActivity.this, "No Quote.", Toast.LENGTH_SHORT).show();
                mLoaderDialog.dismiss();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mLoaderDialog.dismiss();
            Toast.makeText(EditQuoteActivity.this, "Cancelled. " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void nextPost() {
        if (mDataSnapshotIterator.hasNext()) {
            mData = mDataSnapshotIterator.next();
            HashMap<String, String> ss = (HashMap<String, String>) mData.getValue();
            mQuoteText.setText(ss.get("quote"));
            loadPhoto(ss.get("image"));
        } else {
            Toast.makeText(this, "No more posts please add more.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRandomPhoto() {
        mRetroInterface.randomPhoto().enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    loadPhoto(response.body().urls.regular);
                } else {
                    Toast.makeText(EditQuoteActivity.this, "Failed to load picture.", Toast.LENGTH_SHORT).show();
                    mLoaderDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                mLoaderDialog.dismiss();
                Toast.makeText(EditQuoteActivity.this, "Failed to load picture.", Toast.LENGTH_SHORT).show();
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

    private void loadPhoto(String photoId) {
        if (!mLoaderDialog.isShowing())
            mLoaderDialog.show();

        if (photoId.startsWith("https://")) {
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

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mOgPhotoBitmap = bitmap;
            mQuoteImageView.setImageBitmap(mOgPhotoBitmap);
            mLoaderDialog.dismiss();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Logger.exp("Logger ");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Logger.exp("Logger ");
        }
    };


    private void move(float x, float y) {
        int[] location = new int[2];
        mQuoteText.getLocationOnScreen(location);
        float aaa = x - (x - location[0]);
        Log.d("TTT", "  " + aaa + " - " + x + " - " + location[0]);

        if (aaa > 0 && x < mCanvas.getWidth() - mQuoteText.getWidth()) {
            mQuoteText.setX(x - mQuoteText.getWidth() / 2);
        }

        if (y > location[1]) {
            return;
        }


        if (location[1] + mQuoteText.getHeight() > mCanvas.getHeight()) {
            return;
        }
    }


    @Override
    public void onFragmentInteraction(String quote, String author) {
        mQuoteText.setText(quote);
        removeFragment();
    }

    @Override
    public void onPhotoSelected(String photoId) {
        loadPhoto(photoId);
        removeFragment();
    }

    @Override
    public void onGalleryPhotoSelected(String photoPath) {
        Picasso.with(Common.getContext())
                .load(new File(photoPath))
                .placeholder(R.drawable.drawable_placeholder)
                .into(mTarget);
        removeFragment();
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

    @Override
    public void onBackPressed() {
        mLoaderDialog.dismiss();
        if (mFragments.isEmpty()) {
            super.onBackPressed();
        } else {
            removeFragment();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (mFragments.isEmpty()) {
            getMenuInflater().inflate(R.menu.photo_edit_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_save:
                if (!Common.isKitkat() && checkAndRequestPermissions()) {
                    saveAndShare();
                } else {
                    saveAndShare();
                }
                break;
            case R.id.item_set_wallpaper:
                break;
            case R.id.item_share:

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void getCaption() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", "Follow @motivational_mantra\n" + getTagOrComment(Constants.MESSAGES) +
                "\n.\n.\n.\n" + getTagOrComment(Constants.TAGS) + " " + getTagOrComment(Constants.TAGS));
        clipboard.setPrimaryClip(clip);
    }


    public static String getTagOrComment(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {

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
                        getCaption();
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        Uri uri = Uri.parse("file://" + path);
                        sharingIntent.setType("image/*");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
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


    /**
     * Permission check if version is >= 6 (Marshmallow.)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (Constants.REQUEST_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveAndShare();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.grant_permission);
                builder.setMessage(R.string.grant_permission_message);
                builder.setNegativeButton(R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });


                builder.setPositiveButton(R.string.open_settings, (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);

                });
                builder.create().show();
            }
        }
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

    private String createBitmap() {
        mCanvas.buildDrawingCache(true);
        Bitmap bitmap = mCanvas.getDrawingCache(true);
        bitmap = getResizedBitmap(bitmap, 2000, 2000, false);
        mCanvas.destroyDrawingCache();
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

            if (mData != null) {
                mData.getRef().removeValue();
                mData = null;
                mEdited = true;
            }

            return file.getAbsolutePath();
        } catch (Exception e) {
            Logger.exp("" + e);
            return "";
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight, boolean isNecessaryToKeepOrig) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getWidth();

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;

    }

    @Override
    public void onAlignmentChanged(int alignment) {

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mQuoteText.getLayoutParams();
        params.addRule(alignment);
        mQuoteText.setLayoutParams(params); //causes layout update
        if (RelativeLayout.ALIGN_PARENT_RIGHT == alignment) {
            mQuoteText.setGravity(Gravity.RIGHT);
        } else if (RelativeLayout.CENTER_HORIZONTAL == alignment) {
            mQuoteText.setGravity(Gravity.CENTER);
        } else if (RelativeLayout.ALIGN_PARENT_LEFT == alignment) {
            mQuoteText.setGravity(Gravity.LEFT);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mOgPhotoBitmap.recycle();
//        mOgPhotoBitmap = null;
    }

    @Override
    public void onTextSizeChanged(boolean sizeIncreased) {
        float currentSize = Common.spFromPixel(mQuoteText.getTextSize());
        if (sizeIncreased) {
            currentSize = currentSize + 1f;
            if (currentSize < 56)
                mQuoteText.setTextSize(currentSize);
        } else {
            currentSize = currentSize - 1f;
            if (currentSize > 18)
                mQuoteText.setTextSize(currentSize);
        }
    }

    @Override
    public void onTextEdited(String text, int color) {
        if (mEditAuthor) {
//            mAuthorText.setText(text);
//            mAuthorText.setTextColor(color);
        } else {
            mQuoteText.setText(text);
            mQuoteText.setTextColor(color);
        }
    }

    public void changeBackground(int color) {
        if (color != 1) {

            mQuoteImageView.setImageBitmap(null);
            mQuoteImageView.setBackgroundColor(color);
        } else {
            mQuoteImageView.setImageBitmap(mOgPhotoBitmap);
        }
    }
}
