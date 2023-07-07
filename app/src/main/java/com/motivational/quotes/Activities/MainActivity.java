package com.motivational.quotes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.motivational.quotes.Fragments.QuotesFragment;
import com.motivational.quotes.R;


public class MainActivity extends AppCompatActivity implements QuotesFragment.OnFragmentInteractionListener {


    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onFragmentInteraction(String quote, String author) {

    }

    @Override
    public void onPhotoSelected(String photoId) {
        Intent intent = new Intent(this, EditQuoteActivity.class);
        intent.putExtra("IMAGE_ID", photoId);
        startActivity(intent);
    }

    @Override
    public void onGalleryPhotoSelected(String photoPath) {

    }

}
