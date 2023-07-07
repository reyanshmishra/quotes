package com.motivational.quotes.ImagePicker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.motivational.quotes.Fragments.QuotesFragment;
import com.motivational.quotes.Models.GalleryPhoto;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;

import java.util.ArrayList;
import java.util.HashMap;

import static com.motivational.quotes.Utils.Constants.READ_EXTERNAL_STORAGE_PERMISSION;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    private ArrayList<String> mImageArrayList;
    private ArrayList<GalleryPhoto> mAlbumsList;
    private ArrayList<String> mImagesList;

    private TextView mAlbumName;
    private LinearLayout mAlbumNameHolder;
    private PopupWindow mPopupWindow;

    private ImageButton mBackButton;
    private RecyclerView mRecyclerView;

    private PicketAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mBucketNameAndSize;


    private FrameLayout mParentFrameLayout;

    private String mPicturePath;
    private DropDownAdapter mDropDownAdapter;
    private String mSource = "";

    private View mView;
    private View mPopupView;

    private QuotesFragment.OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_gallary, container, false);

        mImageArrayList = new ArrayList<>();
        mBucketNameAndSize = new ArrayList<>();
        mImagesList = new ArrayList<>();
        mAlbumsList = new ArrayList<>();

        mAdapter = new PicketAdapter(this, mImageArrayList);

        mParentFrameLayout = mView.findViewById(R.id.parent_frame_layout);
        mParentFrameLayout.getForeground().setAlpha(0);

        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(Common.getContext(), 3));
        mRecyclerView.setAdapter(mAdapter);


        mBackButton = mView.findViewById(R.id.cancelButton);
        mBackButton.setOnClickListener(this);

        mAlbumNameHolder = mView.findViewById(R.id.albumLayout);


        mAlbumName = mView.findViewById(R.id.albumNameText);
        mAlbumName.setText("Camera");


        mAlbumNameHolder.setOnClickListener(this);

        initPopUpWindow();

        int readAccountsPermission = ActivityCompat.checkSelfPermission(Common.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (Common.isMarshmello()) {
            if (readAccountsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);
            } else {
                addImage("All");
                fetchBucketList();
            }
        } else {
            addImage("All");
            fetchBucketList();
        }


        return mView;
    }

    private void initPopUpWindow() {
        mBucketNameAndSize = new ArrayList<>();
        mPopupView = View.inflate(getActivity(), R.layout.drop_down_layout, null);

        mPopupWindow = new PopupWindow(getActivity());
        mPopupWindow.setContentView(mPopupView);

        RecyclerView recyclerView = mPopupView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Common.getContext()));

        mDropDownAdapter = new DropDownAdapter(this, mBucketNameAndSize);

        recyclerView.setAdapter(mDropDownAdapter);

        mPopupWindow.setFocusable(true);

        mAlbumNameHolder.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAlbumNameHolder.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPopupWindow.setWidth(mAlbumNameHolder.getWidth());
            }
        });


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;

        mPopupWindow.setHeight(height - 425);

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        mPopupWindow.setContentView(mPopupView);

        mPopupWindow.setOnDismissListener(() -> mParentFrameLayout.getForeground().setAlpha(0));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addImage("All");
                fetchBucketList();
            } else {
                Toast.makeText(Common.getContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void fetchBucketList() {

        String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        String[] projection = new String[]{
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA
        };

        Cursor cur = Common.getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                BUCKET_GROUP_BY,
                null,
                BUCKET_ORDER_BY);

        if (cur.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", cur.getString(1));
                mImagesList = getImagesFromBucket(cur.getString(1));
                map.put("size", String.valueOf(mImagesList.size()));

                if (mImagesList.size() > 0)
                    map.put("image", String.valueOf(mImagesList.get(0)));

                if (!mBucketNameAndSize.contains(map)) {
                    mBucketNameAndSize.add(map);
                }

            } while (cur.moveToNext());
        }

        mDropDownAdapter.update(mBucketNameAndSize);
        mDropDownAdapter.notifyDataSetChanged();
    }

    private void addImage(String bucketName) {
        mImageArrayList.clear();
        mAdapter.notifyDataSetChanged();
        mImageArrayList.addAll(getImagesFromBucket(bucketName));
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<String> getImagesFromBucket(String bucketName) {
        ArrayList<String> images = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA
        };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        String condition = null;
        if (!bucketName.equalsIgnoreCase("All")) {
            condition = MediaStore.Images.Media.DATA + " like '%/" + bucketName.replaceAll("'", "''") + "/%'";
        }

        Cursor cur = Common.getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, condition,
                null,
                orderBy + " DESC");
        if (cur.moveToFirst()) {
            do {
                images.add(cur.getString(2));
            } while (cur.moveToNext());
        }
        cur.close();
        return images;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.albumLayout:
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(v, 0, 0);
                    mParentFrameLayout.getForeground().setAlpha(110);
                }
                break;
        }
    }


    public void onPictureSelected(String imagePath) {
        if (mListener != null) {
            mListener.onGalleryPhotoSelected(imagePath);
        }
    }


    public void onAlbumSelected(String name) {
        mAlbumName.setText(name);
        addImage(name);
        mPopupWindow.dismiss();
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
}