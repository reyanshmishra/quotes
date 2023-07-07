package com.motivational.quotes.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;

/**
 * Created by reyansh on 12/18/17.
 */

public class EditTextDialog extends DialogFragment {

    private EditText mQuoteText;
    private OnTextEditingDone mListener;
    private int mColor = Color.parseColor("#ffffff");
    private InputMethodManager mImm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String quote = getArguments().getString("QUOTE_TEXT");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
        mQuoteText = view.findViewById(R.id.edit_text);
        mQuoteText.setText(quote);
        mQuoteText.setSelection(quote.length());


        builder.setTitle(R.string.edit_quote);
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (mListener != null) {
                mListener.onTextEdited(mQuoteText.getText().toString(), mColor);
            }

        });

        mQuoteText.requestFocus();

        return builder.create();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    public interface OnTextEditingDone {
        void onTextEdited(String text, int color);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTextEditingDone) {
            mListener = (OnTextEditingDone) context;
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
