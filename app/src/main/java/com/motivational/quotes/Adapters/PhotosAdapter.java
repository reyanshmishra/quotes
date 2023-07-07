package com.motivational.quotes.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motivational.quotes.Fragments.PhotosFragment;
import com.motivational.quotes.Models.Photo;
import com.motivational.quotes.Models.PixabayModel;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.TypefaceHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by reyansh on 12/10/17.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosHolder> {

    private List mPhotos;
    private PhotosFragment mPhotosFragment;

    public PhotosAdapter(List mPhotos, PhotosFragment mPhotosFragment) {
        this.mPhotos = mPhotos;
        this.mPhotosFragment = mPhotosFragment;
    }


    @Override
    public PhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item, parent, false);
        return new PhotosHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosHolder holder, int position) {

        if (mPhotos.get(position) instanceof Photo) {
            holder.mPhotographerName.setText("Unsplash");
            Photo photo = (Photo) mPhotos.get(position);
            if (photo.urls != null) {
                Picasso.with(mPhotosFragment.getContext()).load(photo.urls.thumb).placeholder(R.drawable.drawable_placeholder).into(holder.mPhoto);
                holder.artistName.setText(photo.user.username);
            }

        } else {
            holder.mPhotographerName.setText("Pixabay");
            PixabayModel.Hits photo = (PixabayModel.Hits) mPhotos.get(position);
            Picasso.with(mPhotosFragment.getContext()).load(photo.previewurl).placeholder(R.drawable.drawable_placeholder).into(holder.mPhoto);
        }
        if ((position >= getItemCount() - 1)) {
            mPhotosFragment.getPhotos();
        }
    }

    @Override
    public int getItemCount() {
        return mPhotos == null ? 0 : mPhotos.size();
    }

    public void updatePhotos(List photos) {
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


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPhoto.getLayoutParams();
            params.width = mWidth;
            params.height = mWidth + (mWidth / 2);
            mPhoto.setLayoutParams(params);

            mOverFlow = itemView.findViewById(R.id.overflow);
            mOverFlow.setVisibility(View.INVISIBLE);
            mOverFlow.setOnClickListener(v -> {
                PopupMenu menu = new PopupMenu(mPhotosFragment.getActivity(), v);
                menu.inflate(R.menu.menu_photo);
                menu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_edit:
                            break;
                        case R.id.menu_set_wallpaper:
                            break;
                    }
                    return false;
                });
                menu.show();

            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mPhotos.get(getAdapterPosition()) instanceof Photo) {
                Photo photo = (Photo) mPhotos.get(getAdapterPosition());
                mPhotosFragment.photoSelected(photo.id);
            } else {
                PixabayModel.Hits photo = (PixabayModel.Hits) mPhotos.get(getAdapterPosition());
                mPhotosFragment.photoSelected(photo.webformaturl);
            }
        }
    }
}
