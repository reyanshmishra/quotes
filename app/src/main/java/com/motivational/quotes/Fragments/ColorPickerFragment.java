package com.motivational.quotes.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motivational.quotes.R;

/**
 * Created by reyansh on 12/18/17.
 */

public class ColorPickerFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_color_picker, container, false);
        return mView;
    }
}
