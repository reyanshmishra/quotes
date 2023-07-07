package com.motivational.quotes.ImagePicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Neha on 2/15/2017.
 */

public class DropDownAdapter extends RecyclerView.Adapter<DropDownAdapter.ItemHolder> {
    private GalleryFragment mGalleryFragment;
    private ArrayList<HashMap<String, String>> mBucketNameAndSize;

    public DropDownAdapter(GalleryFragment galleryFragment, ArrayList<HashMap<String, String>> bucketNameAndSize) {
        mGalleryFragment = galleryFragment;
        mBucketNameAndSize = bucketNameAndSize;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_spinner_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Picasso.with(Common.getContext()).load(new File(mBucketNameAndSize.get(position).get("image"))).fit().centerCrop().into(holder.mImageView);

        holder.mBucketNameText.setText(mBucketNameAndSize.get(position).get("name"));
        holder.mImageCountText.setText(mBucketNameAndSize.get(position).get("size"));
    }

    @Override
    public int getItemCount() {
        return mBucketNameAndSize != null ? mBucketNameAndSize.size() : 0;
    }

    public void update(ArrayList<HashMap<String, String>> bucketNameAndSize) {
        mBucketNameAndSize = bucketNameAndSize;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mBucketNameText;
        private TextView mImageCountText;
        private ImageView mImageView;

        public ItemHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.albumPhotoImage);
            mBucketNameText = itemView.findViewById(R.id.albumNameText);
            mImageCountText = itemView.findViewById(R.id.photoCountText);

            itemView.setOnClickListener(v -> mGalleryFragment.onAlbumSelected(mBucketNameAndSize.get(getAdapterPosition()).get("name")));
        }
    }
}
