package com.company.usedado.Java.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.company.usedado.R;

public class dialog_profile extends AppCompatDialogFragment {

    private EditText editText;
    private String title;
    private dialog_profile_Listener listener;

    public dialog_profile(String title){
        this.title = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profile,null);

        builder.setView(view).setTitle(title).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = editText.getText().toString();
                listener.applyTexts(newText,title);
            }
        });

        editText = view.findViewById(R.id.dialog_profile_editText);

        editText.setHint(title.split(" ")[2].replace("!",""));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (dialog_profile_Listener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString()+" must implement dialog_profile_listener!");
        }
    }

    public interface dialog_profile_Listener{
        void applyTexts(String newText,String title);
    }
}
