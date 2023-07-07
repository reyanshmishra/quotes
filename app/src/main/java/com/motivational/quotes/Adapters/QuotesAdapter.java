package com.motivational.quotes.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motivational.quotes.Fragments.QuotesFragment;
import com.motivational.quotes.Models.Quote;
import com.motivational.quotes.R;

import java.util.ArrayList;

/**
 * Created by reyansh on 12/10/17.
 */

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuotesHolder> {

    private QuotesFragment mQuotesFragment;
    private ArrayList<Quote> mQuotes;

    public QuotesAdapter(QuotesFragment quotesFragment, ArrayList<Quote> quotes) {
        mQuotesFragment = quotesFragment;
        mQuotes = quotes;
    }

    @Override
    public QuotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_view_item, parent, false);
        return new QuotesHolder(view);
    }

    @Override
    public void onBindViewHolder(QuotesHolder holder, int position) {
        holder.mQuoteText.setText(mQuotes.get(position).quote);
        holder.mAuthorText.setText(mQuotes.get(position).author);
    }


    @Override
    public int getItemCount() {
        return mQuotes == null ? 0 : mQuotes.size();
    }

    public void updateData(ArrayList<Quote> quotes) {
        this.mQuotes = quotes;
        notifyDataSetChanged();
    }

    public class QuotesHolder extends RecyclerView.ViewHolder {
        private TextView mQuoteText;
        private TextView mAuthorText;

        public QuotesHolder(View itemView) {
            super(itemView);
            int mWidth = 0;

            mQuoteText = itemView.findViewById(R.id.quoteText);
            mAuthorText = itemView.findViewById(R.id.authorText);

            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            mWidth = (metrics.widthPixels) / 2;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mQuoteText.getLayoutParams();
            params.width = mWidth;
            params.height = mWidth + (mWidth / 3);
            mQuoteText.setLayoutParams(params);
            itemView
                    .setOnClickListener(v -> {
                        mQuotesFragment.onButtonPressed(mQuotes.get(getAdapterPosition()).quote, mQuotes.get(getAdapterPosition()).author);
                    });

        }
    }
}
