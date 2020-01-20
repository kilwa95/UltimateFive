package com.example.ultimatefive;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by Abdulhalim Khaled on 2019-12-25.
 */
public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mimageMatche;
    TextView nomOrganisat,lie,adres,dat,horair,typeTerr;
    IthemClickListner mIthemClickListner;




    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.nomOrganisat= itemView.findViewById(R.id.nomOrganisateur);
        this.lie= itemView.findViewById(R.id.lieu);
        this.adres = itemView.findViewById(R.id.adresse);
        this.dat = itemView.findViewById(R.id.date);
        this.horair = itemView.findViewById(R.id.horaire);
        this.typeTerr = itemView.findViewById(R.id.typeTerrain);
        this.mimageMatche = itemView.findViewById(R.id.imageMatche);


        itemView.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        this.mIthemClickListner.onIthemClickListner(v,getLayoutPosition());

    }

    public void setIthemClickList(IthemClickListner ic)
    {
        this.mIthemClickListner = ic;
    }
}
