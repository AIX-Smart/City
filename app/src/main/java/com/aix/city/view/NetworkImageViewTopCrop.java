package com.aix.city.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Thomas on 28.01.2016.
 */
public class NetworkImageViewTopCrop extends NetworkImageView {

    public NetworkImageViewTopCrop(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public NetworkImageViewTopCrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public NetworkImageViewTopCrop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b)
    {
        Matrix matrix = getImageMatrix();
        float scaleFactor = getWidth()/(float)getDrawable().getIntrinsicWidth();
        matrix.setScale(scaleFactor, scaleFactor, 0, 0);
        setImageMatrix(matrix);
        return super.setFrame(l, t, r, b);
    }
}
