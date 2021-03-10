package com.company.usedado.Java.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.view.ViewCompat;

import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class dialog_offered_price extends AppCompatDialogFragment {

    private EditText editText;
    private Spinner spinner;
    private EditText additionalComment;
    private TextView oriPriceView;
    private TextView title;
    private EditText address;
    private dialog_offered_price.dialog_offered_price_Listener listener;
    private Integer oriPrice;
    private Integer lastPrice;
    private String heading;
    private List<String> methods;



    public dialog_offered_price(Integer oriPrice, Integer lastPrice, List<String> methods,String title){
        this.oriPrice = oriPrice;
        this.lastPrice = lastPrice;
        this.methods = methods;
        heading = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_offered_price,null);

        builder.setView(view).setTitle("").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO add that dialog is not closed when entering invalid input
                if (!editText.getText().toString().isEmpty()) {
                    Integer newPrice = Integer.parseInt(editText.getText().toString());
                    listener.applyTexts(newPrice, spinner.getSelectedItem().toString(),address.getText().toString(),additionalComment.getText().toString(),lastPrice);
                } else Toast.makeText(getContext(), "EMPTY", Toast.LENGTH_SHORT).show();
            }

        });
        editText = view.findViewById(R.id.dialog_offered_price_editText);
        oriPriceView = view.findViewById(R.id.dialog_offered_price_oriPrice);
        additionalComment = view.findViewById(R.id.dialog_offered_price_comment);
        address = view.findViewById(R.id.dialog_offered_price_address);
        title = view.findViewById(R.id.dialog_offered_price_headline);
        spinner = view.findViewById(R.id.dialog_offered_price_llayout);

        oriPriceView.setText("Original Price: "+oriPrice);
        title.setText(heading);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, methods);
        spinner.setAdapter(dataAdapter);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (dialog_offered_price.dialog_offered_price_Listener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString()+" must implement dialog_profile_listener!");
        }
    }

    public interface dialog_offered_price_Listener{
        void applyTexts(Integer newText, String method, String address, String comment, Integer lastPrice);
    }
}
