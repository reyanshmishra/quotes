package com.motivational.quotes.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motivational.quotes.Adapters.QuotesAdapter;
import com.motivational.quotes.Models.Quote;
import com.motivational.quotes.Networking.Retro;
import com.motivational.quotes.Networking.RetroInterface;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuotesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View mView;
    private RecyclerView mRecyclerView;
    private ArrayList<Quote> mQuotes;
    private RetroInterface mRetro;
    private QuotesAdapter mQuotesAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_quotes, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView.setLayoutManager(new GridLayoutManager(Common.getContext(), 2));
        mQuotes = new ArrayList<>();
        mQuotesAdapter = new QuotesAdapter(this, mQuotes);
        mRecyclerView.setAdapter(mQuotesAdapter);
        getQuotes();
        return mView;
    }


    private void getQuotes() {
        mSwipeRefreshLayout.setRefreshing(true);
        mRetro = Retro.getRetro().create(RetroInterface.class);
        mRetro.getQuotes().enqueue(new Callback<ArrayList<Quote>>() {
            @Override
            public void onResponse(Call<ArrayList<Quote>> call, Response<ArrayList<Quote>> response) {
                if (response.isSuccessful()) {
                    mQuotes.addAll(response.body());
                    mQuotesAdapter.updateData(mQuotes);
                } else {

                }
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArrayList<Quote>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    public void onButtonPressed(String quote, String author) {
        if (mListener != null) {
            mListener.onFragmentInteraction(quote, author);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String quote, String author);

        void onPhotoSelected(String photoId);

        void onGalleryPhotoSelected(String photoPath);
    }
}
