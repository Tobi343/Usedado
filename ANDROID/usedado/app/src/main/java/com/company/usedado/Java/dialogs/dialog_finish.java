package com.company.usedado.Java.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;

import java.util.List;

public class dialog_finish extends AppCompatDialogFragment {

    private Offer_Offer_Item item;
    private dialog_finish.dialog_finish_Listener listener;
    private EditText address;
    private EditText fullName;
    private TextView heading;
    private TextView method;


    public dialog_finish(Offer_Offer_Item item){
     this.item = item;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_last_details,null);

        builder.setView(view).setTitle("").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO add that dialog is not closed when entering invalid input
                if (!address.getText().toString().isEmpty()) {
                    listener.applyTexts(fullName.getText().toString(),address.getText().toString(),item);
                } else Toast.makeText(getContext(), "EMPTY", Toast.LENGTH_SHORT).show();
            }

        });
        method = view.findViewById(R.id.dialog_last_details_method);
        address = view.findViewById(R.id.dialog_last_details_address);
        fullName = view.findViewById(R.id.dialog_last_details_fullName);
        heading = view.findViewById(R.id.dialog_last_details_headline);
        heading.setText(item.getTitle());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (dialog_finish.dialog_finish_Listener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString()+" must implement dialog_profile_listener!");
        }
    }

    public interface dialog_finish_Listener{
        void applyTexts(String name, String address, Offer_Offer_Item item);
    }
}