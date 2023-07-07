package com.motivational.quotes.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motivational.quotes.Fragments.GoogleSearchPhotoFragment;
import com.motivational.quotes.Models.GoogleImages;
import com.motivational.quotes.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by reyansh on 2/3/18.
 */

public class GoogleSearchAdapter extends RecyclerView.Adapter<GoogleSearchAdapter.PhotosHolder> {

    private ArrayList<GoogleImages.Items> mPhotos;
    private GoogleSearchPhotoFragment mGoogleSearchPhotoFragment;

    public GoogleSearchAdapter(ArrayList<GoogleImages.Items> mPhotos, GoogleSearchPhotoFragment mGoogleSearchPhotoFragment) {
        this.mPhotos = mPhotos;
        this.mGoogleSearchPhotoFragment = mGoogleSearchPhotoFragment;
    }


    @Override
    public GoogleSearchAdapter.PhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item, parent, false);
        return new GoogleSearchAdapter.PhotosHolder(view);
    }

    @Override
    public void onBindViewHolder(GoogleSearchAdapter.PhotosHolder holder, int position) {
        try {
            Picasso.with(mGoogleSearchPhotoFragment.getContext()).load(mPhotos.get(position).mPageMap.mThumnail.get(0).mSrc)
                    .placeholder(R.drawable.drawable_placeholder)
                    .into(holder.mPhoto);
        } catch (Exception e) {

        }

        if ((position >= getItemCount() - 1)) {
            mGoogleSearchPhotoFragment.nextPage();
        }
    }

    @Override
    public int getItemCount() {
        return mPhotos == null ? 0 : mPhotos.size();
    }

    public void updatePhotos(ArrayList<GoogleImages.Items> photos) {
        this.mPhotos = photos;
        notifyDataSetChanged();
    }

    public class PhotosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPhotographerName;
        private TextView artistName;
        private ImageView mPhoto;
        private ImageView mOverFlow;

        public PhotosHolder(View itemView) {
            super(itemView);
            int mWidth;

            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            mWidth = (metrics.widthPixels) / 2;

            mPhotographerName = itemView.findViewById(R.id.gridViewTitleText);
            artistName = itemView.findViewById(R.id.gridViewSubText);
            mPhoto = itemView.findViewById(R.id.gridViewImage);


            mPhotographerName.setVisibility(View.GONE);
            artistName.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPhoto.getLayoutParams();
            params.width = mWidth;
            params.height = mWidth + (mWidth / 2);
            mPhoto.setLayoutParams(params);

            mOverFlow = itemView.findViewById(R.id.overflow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() > 0) {
                mGoogleSearchPhotoFragment.photoSelected(mPhotos.get(getAdapterPosition()).mPageMap.mCseImage.get(0).mSrc);
            }
        }
    }
}
