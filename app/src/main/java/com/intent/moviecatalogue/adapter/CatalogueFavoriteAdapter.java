package com.intent.moviecatalogue.adapter;

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
import com.intent.moviecatalogue.Public;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.activity.DetailActivity;
import com.intent.moviecatalogue.database.Favorite;
import com.intent.moviecatalogue.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogueFavoriteAdapter extends RecyclerView.Adapter<CatalogueFavoriteAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Movie> listMovie;
    private List<Favorite> listFavorite;

    public CatalogueFavoriteAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    private List<Favorite> getListFavorite() {
        return listFavorite;
    }

    public void setListFavorite(List<Favorite> listFavorite) {
        this.listFavorite = listFavorite;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_favorite, viewGroup, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder cardViewViewHolder, final int i) {
        if (getListFavorite() != null) {
            cardViewViewHolder.title.setText(getListFavorite().get(i).getTitle());
            cardViewViewHolder.release.setText(getListFavorite().get(i).getRelease());
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + getListFavorite().get(i).getPoster())
                    .into(cardViewViewHolder.Image);

            cardViewViewHolder.cardView.setOnClickListener(v -> {
                Intent moveIntent = new Intent(context, DetailActivity.class);
                moveIntent.putExtra(Public.KEY_CATALOGUE_FAVORITE, getListFavorite().get(i));
                context.startActivity(moveIntent);
            });
        } else if (getListMovie() != null) {
            if (getListMovie().get(i).getTitle() != null) {
                cardViewViewHolder.title.setText(getListMovie().get(i).getTitle());
                cardViewViewHolder.release.setText(getListMovie().get(i).getRelease());
            } else if (getListMovie().get(i).getName() != null) {
                cardViewViewHolder.title.setText(getListMovie().get(i).getName());
                cardViewViewHolder.release.setText(getListMovie().get(i).getFirstAirDate());

            }
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + getListMovie().get(i).getPoster())
                    .into(cardViewViewHolder.Image);

            cardViewViewHolder.cardView.setOnClickListener(v -> {
                Intent moveIntent = new Intent(context, DetailActivity.class);
                moveIntent.putExtra(Public.KEY_CATALOGUE, getListMovie().get(i));
                context.startActivity(moveIntent);
            });

        }
    }

    @Override
    public int getItemCount() {
        int size;
        if (getListFavorite() != null) {
            size = getListFavorite().size();
        } else if (getListMovie() != null) {
            size = getListMovie().size();
        } else {
            size = 0;
        }
        return size;
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_release)
        TextView release;
        @BindView(R.id.img_item_photo)
        ImageView Image;
        @BindView(R.id.cardview)
        CardView cardView;

        CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
