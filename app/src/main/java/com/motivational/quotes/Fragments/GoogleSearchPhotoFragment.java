package com.motivational.quotes.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.motivational.quotes.Adapters.GoogleSearchAdapter;
import com.motivational.quotes.Models.GoogleImages;
import com.motivational.quotes.Networking.Retro;
import com.motivational.quotes.Networking.RetroInterface;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.TypefaceHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by reyansh on 12/21/17.
 */

public class GoogleSearchPhotoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RetroInterface mRetro;
    private GoogleSearchAdapter mPhotosAdapter;
    private RecyclerView mRecyclerView;
    private View mView;
    private Context mContext;
    private ArrayList<GoogleImages.Items> mPhotos;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mGridLayoutManager;
    private QuotesFragment.OnFragmentInteractionListener mListener;
    private EditText mEditText;
    private Handler mHandler;
    private String newText = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photos_layout, container, false);
        mContext = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
        mHandler = new Handler();
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.CYAN, Color.BLUE, Color.YELLOW);
        mGridLayoutManager = new GridLayoutManager(mContext, 2);
        mPhotos = new ArrayList<>();

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mPhotosAdapter = new GoogleSearchAdapter(mPhotos, this);
        mRecyclerView.setAdapter(mPhotosAdapter);
        mEditText = mView.findViewById(R.id.edit_text);
        mEditText.addTextChangedListener(mTextWatcher);
        getPhotos(1, "");


        return mView;
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newText = s.toString().trim();
            if (newText == null || newText.equals("")) return;
            mSwipeRefreshLayout.setRefreshing(true);
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, 600);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void getPhotos(int startIndex, String query) {
        mSwipeRefreshLayout.setRefreshing(true);
        mRetro = Retro.getRetro().create(RetroInterface.class);
        mRetro.searchGoogle(query, startIndex).enqueue(new Callback<GoogleImages>() {
            @Override
            public void onResponse(Call<GoogleImages> call, Response<GoogleImages> response) {
                if (response.isSuccessful() && response.body().mItems != null) {
                    mPhotos.addAll(response.body().mItems);
                    mPhotosAdapter.updatePhotos(mPhotos);
                } else {

                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<GoogleImages> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
    }


    public void photoSelected(String photoId) {
        if (mListener != null) {
            mListener.onPhotoSelected(photoId);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuotesFragment.OnFragmentInteractionListener) {
            mListener = (QuotesFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onRefresh() {
        // getPhotos();
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPhotos.clear();
            getPhotos(1, newText);
        }
    };
    int startIndex = 1;

    public void nextPage() {
        startIndex = startIndex + 10;
        getPhotos(startIndex, newText);
    }


}
