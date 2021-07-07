package com.example.iot;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    List<Kontak> kontaks;

    LayoutInflater inflater;
    public CallAdapter(Context context, List<Kontak> kontaks){
        this.inflater = LayoutInflater.from(context);
        this.kontaks = kontaks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.call_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNoHP.setText(kontaks.get(position).getTelp());
        holder.txtNamaRS.setText(kontaks.get(position).getName());
        holder.rsImage.setImageResource(R.drawable.rs);
        holder.callImage.setImageResource(R.drawable.call);
    }

    @Override
    public int getItemCount() {
        return kontaks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNoHP;
        TextView txtNamaRS;
        ImageView rsImage;
        ImageView callImage;

        public ViewHolder(View itemView){
            super(itemView);
            txtNoHP = itemView.findViewById(R.id.noHP);
            txtNamaRS = itemView.findViewById(R.id.namaRS);
            rsImage = itemView.findViewById(R.id.rs_icon);
            callImage = itemView.findViewById(R.id.call_icon);
        }

    }
}
