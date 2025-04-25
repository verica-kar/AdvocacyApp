package com.vericakaranakova.advocacyapp;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private final ArrayList<Officials> lst;
    private final MainActivity mainActivity;

    public MainAdapter(ArrayList<Officials> govList, MainActivity ma) {
        lst = govList;
        mainActivity = ma;
    }


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gov_officials_recycler, parent, false);

        itemView.setOnClickListener(mainActivity);

        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Officials official = lst.get(position);
        holder.pos.setText(official.getPosition());
        holder.name.setText(official.getName() + " (" + official.getParty() + ")");

        if (!official.getPhotoURL().equals("missing")) {
            Picasso.get().load(official.getPhotoURL()).error(R.drawable.brokenimage).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.missing);
        }
    }

    @Override
    public int getItemCount() {
        if(lst == null) {
            return 0;
        } else {
            return lst.size();
        }
    }

    private int getIcon(String icon) {
        icon = icon.replace("-", "_"); // Replace all dashes with underscores
        int iconID =
                mainActivity.getResources().getIdentifier(icon, "drawable", mainActivity.getPackageName());
        if (iconID == 0) {
            return iconID;
        }
        return iconID;
    }
}
