package com.motivational.quotes.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.motivational.quotes.R;

/**
 * Created by reyansh on 12/11/17.
 */

public class EditControllersFragment extends Fragment {


    private SeekBar mBrightnessSeekbar;
    private SeekBar mContrastSeekbar;
    private SeekBar mSaturationSeekbar;

    private Button mTextSizeDecreaseButton;
    private Button mTextSizeIncreaseButton;

    private Button mAlignLeftButton;
    private Button mAlignRightButton;
    private Button mAlignMiddleButton;


    private View mView;
    private EditImageFragmentListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_controller, container, false);


        mBrightnessSeekbar = mView.findViewById(R.id.brightnessSeekbar);
        mContrastSeekbar = mView.findViewById(R.id.contrastSeekbar);
        mSaturationSeekbar = mView.findViewById(R.id.saturationSeekbar);

        mAlignLeftButton = mView.findViewById(R.id.alignLeftButton);
        mAlignMiddleButton = mView.findViewById(R.id.alignMiddleButton);
        mAlignRightButton = mView.findViewById(R.id.alignRightButton);

        mAlignLeftButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onAlignmentChanged(RelativeLayout.ALIGN_PARENT_LEFT);
            }
        });

        mAlignMiddleButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onAlignmentChanged(RelativeLayout.CENTER_HORIZONTAL);
            }
        });

        mAlignRightButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onAlignmentChanged(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
        });
        mTextSizeDecreaseButton = mView.findViewById(R.id.decreaseTextSizeButton);
        mTextSizeIncreaseButton = mView.findViewById(R.id.increaseTextSizeButton);

        mTextSizeDecreaseButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTextSizeChanged(false);
            }
        });
        mTextSizeIncreaseButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTextSizeChanged(true);
            }
        });

        mBrightnessSeekbar.setProgress(100);
        mContrastSeekbar.setProgress(50);

        mBrightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null) {
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mContrastSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = progress + 50;
                if (mListener != null) {
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mSaturationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mListener != null) {
                    if (progress % 2 == 0) {
                    } else {
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();

            }
        });

        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuotesFragment.OnFragmentInteractionListener) {
            mListener = (EditImageFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface EditImageFragmentListener {

        void onAlignmentChanged(int alignment);

        void onTextSizeChanged(boolean sizeIncreased);
    }
}
