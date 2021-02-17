package com.company.usedado.Java.items;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import com.company.usedado.R;


public class CatagorieItem {
    private int view;
    private String text;
    private int color;

    public CatagorieItem(int image, String cat, int color){
        view = image;
        text = cat;
        this.color = color;
    }

    public int getImage(){return view;};
    public String  getTextCat(){return text;};
    public int getColorLayoutt(){return color;};

}
