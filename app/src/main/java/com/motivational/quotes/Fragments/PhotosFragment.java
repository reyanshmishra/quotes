package com.motivational.quotes.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.motivational.quotes.Activities.ViewPhotoActivity;
import com.motivational.quotes.Adapters.PhotosAdapter;
import com.motivational.quotes.Models.Photo;
import com.motivational.quotes.Models.PixabayModel;
import com.motivational.quotes.Networking.Retro;
import com.motivational.quotes.Networking.RetroInterface;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Logger;
import com.motivational.quotes.Utils.TypefaceHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by reyansh on 12/10/17.
 */

public class PhotosFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RetroInterface mRetro;
    private PhotosAdapter mPhotosAdapter;
    private RecyclerView mRecyclerView;
    private View mView;
    private Context mContext;
    private List mPhotos;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mGridLayoutManager;
    private int mPageNumber = 0;
    private QuotesFragment.OnFragmentInteractionListener mListener;
    private SearchView mSearchView;
    private Handler mHandler;
    private EditText mEditText;

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

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mPhotosAdapter = new PhotosAdapter(mPhotos, this);
        mRecyclerView.setAdapter(mPhotosAdapter);
        mPhotos = new ArrayList();
        mEditText = mView.findViewById(R.id.edit_text);
        mEditText.addTextChangedListener(mTextWatcher);
        mRetro = Retro.getRetro().create(RetroInterface.class);
        mSwipeRefreshLayout.setEnabled(false);

        getPhotos();
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

    public void getPhotos() {
        mPageNumber++;
        mSwipeRefreshLayout.setRefreshing(true);
        mRetro.getPhotos(30, mPageNumber).enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                if (response.isSuccessful()) {
                    mPhotos.addAll(response.body());
                } else {
                    try {
                        Toast.makeText(mContext, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getPixaBayPics();
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                Logger.exp("Filed");
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPixaBayPics() {
        int page = 0;
        if (mPageNumber == 0) {
            page = 1;
        } else {
            page = mPageNumber;
        }
        mRetro.searchPixabay(newText, page, 30).enqueue(new Callback<PixabayModel>() {
            @Override
            public void onResponse(Call<PixabayModel> call, Response<PixabayModel> response) {
                if (response.isSuccessful()) {
                    mPhotos.addAll(response.body().hits);
                    mPhotosAdapter.updatePhotos(mPhotos);
                } else {
                    try {
                        Toast.makeText(mContext, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PixabayModel> call, Throwable t) {
                Logger.exp("Filed");
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.search_fragment_menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_library));

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    String newText = "";

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.equals("")) return false;
        this.newText = newText;
        mSwipeRefreshLayout.setRefreshing(true);
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 600);
        return false;
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Call<Photo> arrayListCall = mRetro.searchPhotos(Retro.Urls.clientId, newText, mPageNumber, 30);
            mPageNumber = 0;
            mPhotos.clear();
            arrayListCall.enqueue(new Callback<Photo>() {
                @Override
                public void onResponse(Call<Photo> call, Response<Photo> response) {
                    if (response.isSuccessful()) {
                        mPhotos.addAll(response.body().results);
                    } else {
                        try {
                            Toast.makeText(mContext, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    getPixaBayPics();
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        }
    };

    public void viewPhoto(String id) {

        Intent intent = new Intent(getActivity(), ViewPhotoActivity.class);
        intent.putExtra("IMAGE_ID", id);
        startActivity(intent);
    }
}
