package com.example.weathereapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CityesTitleActivity extends AppCompatActivity {
 RecyclerView recyclerView;
 EditText edtCity;
 Button btnOk;
 String city;
 SqlHelper helper;
 List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityes_title);
        recyclerView=findViewById(R.id.cityRecycler);
        edtCity=findViewById(R.id.edtCity);
        cities=new ArrayList<>();
        helper=new SqlHelper(CityesTitleActivity.this,"City",null,1);
        cities=helper.SELECT_CITY();
        final CityNameAdapter adapter=new CityNameAdapter(cities);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CityesTitleActivity.this,RecyclerView.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String city = cities.get(position);
                        SetResult(city);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        btnOk=findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                city=edtCity.getText().toString();
                SetResult(city);
            }
        });
    }
    private void SetResult(String cityName){
        Intent intentResult=new Intent();
        intentResult.putExtra("CityName",cityName);
        helper.INSERT_CITY(cityName) ;
        setResult(RESULT_OK,intentResult);

        finish();
    }
}
