package com.company.usedado.Java.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.company.usedado.R;

public class dialog_offered_price extends AppCompatDialogFragment {

    private EditText editText;
    private dialog_offered_price.dialog_offered_price_Listener listener;
    private Integer oriPrice;
    private Integer lastPrice;


    public dialog_offered_price(Integer oriPrice, Integer lastPrice){
        this.oriPrice = oriPrice;
        this.lastPrice = lastPrice;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_offered_price,null);
        builder.setView(view).setTitle("What price do you want to offer?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO add that dialog is not closed when entering invalid input
                Integer newPrice = Integer.parseInt(editText.getText().toString());
                if(newPrice <= oriPrice){
                    if(newPrice >= lastPrice){
                        listener.applyTexts(newPrice);
                    }
                    else {
                        editText.setText(lastPrice.toString());
                        Toast.makeText(getContext(), "Last Price is "+lastPrice, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    editText.setText(oriPrice.toString());
                    Toast.makeText(getContext(), "Price is "+oriPrice, Toast.LENGTH_SHORT).show();
                }
            }
        });
        editText = view.findViewById(R.id.dialog_offered_price_editText);
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
        void applyTexts(Integer newText);
    }
}
