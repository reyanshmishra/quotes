package com.motivational.quotes.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by reyansh on 12/12/17.
 */

public class CustomImageView extends View {

    private Paint mBitmapPaint;
    private Matrix mMatrix;
    private Bitmap mBitmap;

    public CustomImageView(Context context) {
        super(context);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidate();
    }

    public void setBrightness(int brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });
        mBitmapPaint.setColorFilter(new ColorMatrixColorFilter(cm));
        invalidate();
    }
}
