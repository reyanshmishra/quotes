package com.motivational.quotes.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.motivational.quotes.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * Created by reyansh on 12/10/17.
 */

public class Common extends Application {
    boolean posted = false;

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    /**
     * Enable or disable debugging and TAG
     */



    //Device orientation constants.
    public static final int ORIENTATION_PORTRAIT = 0;
    public static final int ORIENTATION_LANDSCAPE = 1;
    //Device screen size/orientation identifiers.

    public static final String REGULAR = "regular";
    public static final String SMALL_TABLET = "small_tablet";
    public static final String LARGE_TABLET = "large_tablet";
    public static final String XLARGE_TABLET = "xlarge_tablet";
    public static final int REGULAR_SCREEN_PORTRAIT = 0;
    public static final int REGULAR_SCREEN_LANDSCAPE = 1;
    public static final int SMALL_TABLET_PORTRAIT = 2;
    public static final int SMALL_TABLET_LANDSCAPE = 3;
    public static final int LARGE_TABLET_PORTRAIT = 4;
    public static final int LARGE_TABLET_LANDSCAPE = 5;
    public static final int XLARGE_TABLET_PORTRAIT = 6;
    public static final int XLARGE_TABLET_LANDSCAPE = 7;
    /**
     * Constant and service instance
     */
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // Initialize Realm (just once per application)

// Get a Realm instance for this thread

//        initPicasso();
    }

    private void initPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        //builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }

    /**
     * Returns the orientation of the device.
     */
    public static int getOrientation() {

        if (mContext.getResources().getDisplayMetrics().widthPixels >
                mContext.getResources().getDisplayMetrics().heightPixels) {
            return ORIENTATION_LANDSCAPE;
        } else {
            return ORIENTATION_PORTRAIT;
        }
    }

    /**
     * Returns the no of column which will be applied to the grids on different devices
     */
    public static int getNumberOfColms() {

        if (getDeviceScreenConfiguration() == Common.REGULAR_SCREEN_PORTRAIT) {
            return 2;
        } else if (getDeviceScreenConfiguration() == Common.REGULAR_SCREEN_LANDSCAPE) {
            return 4;
        }
        return 2;
    }


    /**
     * Returns the current screen configuration of the device.
     */
    public static int getDeviceScreenConfiguration() {
        String screenSize = mContext.getResources().getString(R.string.screen_size);
        boolean landscape = false;
        if (getOrientation() == ORIENTATION_LANDSCAPE) {
            landscape = true;
        }

        if (screenSize.equals(REGULAR) && !landscape)
            return REGULAR_SCREEN_PORTRAIT;
        else if (screenSize.equals(REGULAR) && landscape)
            return REGULAR_SCREEN_LANDSCAPE;
        else if (screenSize.equals(SMALL_TABLET) && !landscape)
            return SMALL_TABLET_PORTRAIT;
        else if (screenSize.equals(SMALL_TABLET) && landscape)
            return SMALL_TABLET_LANDSCAPE;
        else if (screenSize.equals(LARGE_TABLET) && !landscape)
            return LARGE_TABLET_PORTRAIT;
        else if (screenSize.equals(LARGE_TABLET) && landscape)
            return LARGE_TABLET_LANDSCAPE;
        else if (screenSize.equals(XLARGE_TABLET) && !landscape)
            return XLARGE_TABLET_PORTRAIT;
        else if (screenSize.equals(XLARGE_TABLET) && landscape)
            return XLARGE_TABLET_LANDSCAPE;
        else
            return REGULAR_SCREEN_PORTRAIT;
    }


    public static boolean isMarshmello() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    public static float spFromPixel(float pixel) {
        float sp = pixel / Common.getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp;
    }


    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px) {
        Resources resources = Common.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static boolean isKitkat() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    }


    /*
     * Returns the status bar height for the current layout configuration.
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float convertPixelToSp(int px) {
        float sp = px / Common.getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static Bitmap blurImage(Bitmap bitmap, float radius, int sampleSize) {
//        int inSampleSize = 8;
        android.support.v8.renderscript.RenderScript rs = android.support.v8.renderscript.RenderScript.create(Common.getContext());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        Bitmap blurTemplate = BitmapFactory.decodeStream(bis, null, options);
        final android.support.v8.renderscript.Allocation input = android.support.v8.renderscript.Allocation.createFromBitmap(rs, blurTemplate);
        final android.support.v8.renderscript.Allocation output = android.support.v8.renderscript.Allocation.createTyped(rs, input.getType());
        final android.support.v8.renderscript.ScriptIntrinsicBlur script = android.support.v8.renderscript.ScriptIntrinsicBlur.create(rs, android.support.v8.renderscript.Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurTemplate);
        return blurTemplate;
    }

    public static void applyFontForToolbarTitle(Activity context) {
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    break;
                }
            }
        }
    }
}
