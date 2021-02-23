package com.company.usedado.Java.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.R;


import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class CategorieView extends AppCompatActivity {

    private AutoCompleteTextView searchBar;
    private RecyclerView recyclerView;
    private TextView headline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie_view);
        recyclerView = findViewById(R.id.category_recycler);
        searchBar = findViewById(R.id.category_searchbar);
        headline = findViewById(R.id.category_headline);
    }
}