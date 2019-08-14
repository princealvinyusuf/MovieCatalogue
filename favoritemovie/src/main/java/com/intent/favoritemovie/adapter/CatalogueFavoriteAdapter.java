package com.intent.favoritemovie.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.intent.favoritemovie.Public;
import com.intent.favoritemovie.activity.DetailActivity;
import com.intent.favoritemovie.R;
import com.intent.favoritemovie.database.Favorite;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CatalogueFavoriteAdapter extends RecyclerView.Adapter<CatalogueFavoriteAdapter.CardViewViewHolder> {
    private Context context;
    private List<Favorite> listFavorite;

    private List<Favorite> getListFavorite() {
        return listFavorite;
    }

    public void setListFavorite(List<Favorite> listFavorite) {
        this.listFavorite = listFavorite;
    }

    public CatalogueFavoriteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_favorite, viewGroup, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder cardViewViewHolder, final int i) {
        cardViewViewHolder.tvTitle.setText(getListFavorite().get(cardViewViewHolder.getAdapterPosition()).getTitle());
        cardViewViewHolder.tvRelease.setText(getListFavorite().get(i).getRelease());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/" + getListFavorite().get(i).getPoster())
                .into(cardViewViewHolder.imgPoster);

        cardViewViewHolder.cardView.setOnClickListener(v -> {
            Intent moveIntent = new Intent(context, DetailActivity.class);
            moveIntent.putExtra(Public.KEY_CATALOGUE_FAVORITE, getListFavorite().get(i));
            moveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(moveIntent);
        });
    }

    @Override
    public int getItemCount() {
        int size;
        if (getListFavorite() != null) {
            size = getListFavorite().size();
        } else {
            size = 0;
        }
        return size;
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_release)
        TextView tvRelease;
        @BindView(R.id.img_item_photo)
        ImageView imgPoster;
        @BindView(R.id.cardview)
        CardView cardView;

        CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
