package com.vericakaranakova.advocacyapp;

import android.graphics.drawable.Drawable;
import android.view.View;
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

public class MainViewHolder extends RecyclerView.ViewHolder {
    TextView pos;
    TextView name;
    ImageView image;

    public MainViewHolder(View view) {
        super(view);

        pos = view.findViewById(R.id.position);
        name = view.findViewById(R.id.nameAndParty);
        image = view.findViewById(R.id.mainImage);
    }
}
