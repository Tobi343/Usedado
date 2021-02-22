package com.company.usedado.Java.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.view.ViewCompat;

import com.company.usedado.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class dialog_offered_price extends AppCompatDialogFragment {

    private EditText editText;
    private RadioGroup linearLayout;
    private dialog_offered_price.dialog_offered_price_Listener listener;
    private Integer oriPrice;
    private Integer lastPrice;
    private List<String> methods;



    public dialog_offered_price(Integer oriPrice, Integer lastPrice, List<String> methods){
        this.oriPrice = oriPrice;
        this.lastPrice = lastPrice;
        this.methods = methods;
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
                if (!editText.getText().toString().isEmpty()) {
                    Integer newPrice = Integer.parseInt(editText.getText().toString());
                    if (newPrice <= oriPrice) {
                        if (newPrice >= lastPrice) {
                            View btn = linearLayout.findFocus();
                            int id = linearLayout.getCheckedRadioButtonId();
                            id-=3;
                            String method =methods.get(id%10);
                            listener.applyTexts(newPrice, method);
                            linearLayout.removeAllViews();
                        }
                        else
                            {
                                editText.setText(lastPrice.toString());
                                Toast.makeText(getContext(), "Last Price is " + lastPrice, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            editText.setText(oriPrice.toString());
                            Toast.makeText(getContext(), "Price is " + oriPrice, Toast.LENGTH_SHORT).show();
                        }
                    } else Toast.makeText(getContext(), "EMPTY", Toast.LENGTH_SHORT).show();
                }

        });
        editText = view.findViewById(R.id.dialog_offered_price_editText);
        linearLayout = view.findViewById(R.id.dialog_offered_price_llayout);
        Queue<Integer> ids = new LinkedList<Integer>();
        ids.add(R.id.layout1);
        ids.add(R.id.layout2);
        ids.add(R.id.layout3);
        ids.add(R.id.layout4);
        ids.add(R.id.layout5);
        ids.add(R.id.layout6);
        ids.add(R.id.layout7);
        for (String method : methods) {
            RadioButton button = new RadioButton(getContext());
            button.setText(method);

            button.setId(ids.poll());
            button.setSelected(true);
            linearLayout.addView(button);
        }
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
        void applyTexts(Integer newText,String method);
    }
}
