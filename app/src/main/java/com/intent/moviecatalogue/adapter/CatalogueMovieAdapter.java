package com.intent.moviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class CatalogueMovieAdapter extends RecyclerView.Adapter<CatalogueMovieAdapter.CardViewViewHolder> {
    private Context context;
    private ArrayList<Movie> listMovie;
    private List<Favorite> listFavorite;

    public CatalogueMovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    private List<Favorite> getListFavorite() {
        return listFavorite;
    }

    public void setListCatalogue(ArrayList<Movie> listCatalogue) {
        this.listMovie = listCatalogue;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_film, viewGroup, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, final int i) {
        if (getListFavorite() != null) {
            holder.title.setText(getListFavorite().get(i).getTitle());
            holder.release.setText(getListFavorite().get(i).getRelease());
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + getListFavorite().get(i).getPoster())
                    .into(holder.Image);

            holder.cardView.setOnClickListener(v -> {
                Intent moveIntent = new Intent(context, DetailActivity.class);
                moveIntent.putExtra(Public.KEY_CATALOGUE_FAVORITE, getListFavorite().get(i));
                context.startActivity(moveIntent);
            });
        } else if (getListMovie() != null) {
            if (getListMovie().get(i).getTitle() != null) {
                holder.title.setText(getListMovie().get(i).getTitle());
                holder.release.setText(getListMovie().get(i).getRelease());
            } else if (getListMovie().get(i).getName() != null) {
                holder.title.setText(getListMovie().get(i).getName());
                holder.release.setText(getListMovie().get(i).getFirstAirDate());

            }
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500/" + getListMovie().get(i).getPoster())
                    .into(holder.Image);

            holder.cardView.setOnClickListener(v -> {
                Intent moveIntent = new Intent(context, DetailActivity.class);
                moveIntent.putExtra(Public.KEY_CATALOGUE, getListMovie().get(i));
                context.startActivity(moveIntent);
            });

            holder.btnDetail.setOnClickListener(v -> {
                Intent moveIntent = new Intent(context, DetailActivity.class);
                moveIntent.putExtra(Public.KEY_CATALOGUE, getListMovie().get(i));
                context.startActivity(moveIntent);
            });

            holder.btnShare.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, getListMovie().get(i).getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, getListMovie().get(i).getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, getListMovie().get(i).getTitle() + "\n\n" + getListMovie().get(i).getOverview());
                context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share)));
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
        @BindView(R.id.btn_share)
        Button btnShare;
        @BindView(R.id.btn_detail)
        Button btnDetail;

        CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
