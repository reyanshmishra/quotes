package com.motivational.quotes.ImagePicker;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Neha on 2/15/2017.
 */

public class PicketAdapter extends RecyclerView.Adapter<PicketAdapter.ItemHolder> {

    private GalleryFragment mGalleryFragment;
    private ArrayList<String> mImages;

    public PicketAdapter(GalleryFragment galleryFragment, ArrayList<String> images) {
        mGalleryFragment = galleryFragment;
        mImages = images;
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_picker_rowcontent, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Picasso.with(Common.getContext()).
                load(new File(mImages.get(position))).
                placeholder(R.drawable.drawable_placeholder).
                fit().centerCrop().
                into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mImages != null ? mImages.size() : 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public ItemHolder(View itemView) {
            super(itemView);
            int mWidth = 0;

            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            mWidth = (metrics.widthPixels) / 3;

            mImageView = itemView.findViewById(R.id.image_view_main);
            mImageView.getLayoutParams().height = mWidth;
            mImageView.getLayoutParams().width = mWidth;

            itemView.setOnClickListener(v -> mGalleryFragment.onPictureSelected(mImages.get(getAdapterPosition())));
        }
    }
}
