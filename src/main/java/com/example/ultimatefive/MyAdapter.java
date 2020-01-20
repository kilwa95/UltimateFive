package com.example.ultimatefive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by Abdulhalim Khaled on 2019-12-25.
 */
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {


    Context c;
    ArrayList<Model> models;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // recuperation la row

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null,true);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        holder.nomOrganisat.setText(models.get(position).getNomOrganisateur());
        holder.adres.setText(models.get(position).getAdresse());
        holder.lie.setText(models.get(position).getLieu());
        holder.horair.setText(models.get(position).getHoraire());
        holder.dat.setText(models.get(position).getDate());
        holder.typeTerr.setText(models.get(position).getTypeTerraine());
        holder.mimageMatche.setImageResource(models.get(position).getImage());


        holder.setIthemClickList(new IthemClickListner() {
            @Override
            public void onIthemClickListner(View view, int position) {

                String glieu = models.get(position).getLieu();
                String gadresse = models.get(position).getAdresse();
                String gdate= models.get(position).getDate();


                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.mimageMatche.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytes = stream.toByteArray();







                // transmettre information de Activite vers autre activity
                Intent intent = new Intent(c,FicheActivity.class);
                intent.putExtra("ilieu" , glieu);
                intent.putExtra("iadresse",gadresse);
                intent.putExtra("idate",gdate);
                intent.putExtra("iImage" , bytes);
                c.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
