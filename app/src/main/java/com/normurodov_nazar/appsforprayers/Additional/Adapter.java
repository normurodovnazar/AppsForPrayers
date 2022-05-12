package com.normurodov_nazar.appsforprayers.Additional;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.normurodov_nazar.appsforprayers.Models.PrayerTime;
import com.normurodov_nazar.appsforprayers.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.TimesHolder> {
    final Context context;
    final ArrayList<PrayerTime> items;

    public Adapter(Context context, ArrayList<PrayerTime> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public TimesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_item,parent,false);
        return new TimesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimesHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TimesHolder extends RecyclerView.ViewHolder{
        TextView t;
        ImageView i;

        public TimesHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.data);
            i = itemView.findViewById(R.id.status);
        }

        public void setData(PrayerTime item){
            if (!item.isEnabled()) i.setImageResource(R.drawable.ic_off);
            t.setText(Hey.getItemName(item));
        }
    }
}
