package com.example.weathereapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityNameAdapter extends RecyclerView.Adapter<CityNameAdapter.CityNameHolder> {

    List<String> nameList;
public  CityNameAdapter(List<String> names)
{
    nameList=names;
}
    @NonNull
    @Override
    public CityNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.city_name,parent,false);
        CityNameHolder cityNameHolder=new CityNameHolder(view);
        return  cityNameHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CityNameHolder holder, int position) {
    if(nameList.size()>0) {
     holder.txtCity.setText(nameList.get(position));
    }

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }
    public  class  CityNameHolder extends RecyclerView.ViewHolder{
        TextView txtCity;


        public CityNameHolder(@NonNull View itemView) {
            super(itemView);
           txtCity = itemView.findViewById(R.id.txtcity);
        }
    }




}
