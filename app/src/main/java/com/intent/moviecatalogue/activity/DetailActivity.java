package com.intent.moviecatalogue.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.database.Favorite;
import com.intent.moviecatalogue.model.Movie;
import com.intent.moviecatalogue.widget.FavoriteCollectionWidget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intent.moviecatalogue.Public.CATALOGUE_MOVIE;
import static com.intent.moviecatalogue.Public.COLUMN_ID;
import static com.intent.moviecatalogue.Public.COLUMN_KIND;
import static com.intent.moviecatalogue.Public.COLUMN_OVERVIEW;
import static com.intent.moviecatalogue.Public.COLUMN_POSTER;
import static com.intent.moviecatalogue.Public.COLUMN_RELEASE;
import static com.intent.moviecatalogue.Public.COLUMN_TITLE;
import static com.intent.moviecatalogue.Public.GET_WIDGET_UPDATE;
import static com.intent.moviecatalogue.Public.KEY_CATALOGUE;
import static com.intent.moviecatalogue.Public.KEY_CATALOGUE_FAVORITE;
import static com.intent.moviecatalogue.Public.KEY_CATALOGUE_LIST;
import static com.intent.moviecatalogue.Public.URI_FAVORITE;
import static com.intent.moviecatalogue.database.Favorite.cursorMapping;

public class DetailActivity extends AppCompatActivity {

    String title = "";
    String release = "";
    String kind = "";

    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.release)
    TextView tvRelease;

    @BindView(R.id.description)
    TextView tvDescription;

    @BindView(R.id.img_item_photo)
    ImageView Image;

    @BindView(R.id.btn_favorite)
    ImageView btnFavorite;

    Movie movie;
    Favorite favorite;
    ArrayList<Favorite> Favorites;
    private ProgressDialog loading;

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CATALOGUE_MOVIE, kind);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewLoad();
        decision();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void notNull() {
        movie = getIntent().getParcelableExtra(KEY_CATALOGUE);

        if (movie.getTitle() != null) {
            title = movie.getTitle();
            release = convertDate(movie.getRelease());
            kind = "movie";
        } else if (movie.getName() != null) {
            title = movie.getName();
            release = convertDate(movie.getFirstAirDate());
            kind = "tv";
        }

        if (getSupportActionBar() != null) {
            if (movie.getTitle() != null) {
                getSupportActionBar().setTitle(title);
            }
            if (movie.getName() != null) {
                getSupportActionBar().setTitle(title);
            }
        }
        Cursor cursor = getContentResolver().query(URI_FAVORITE, null, kind, null, null);
        Favorites = cursorMapping(cursor);
        FavoriteDetail();

        tvTitle.setText(title);
        tvRelease.setText(release);
        tvDescription.setText(movie.getOverview());
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500/" + movie.getPoster())
                .into(Image);
    }

    private void decision() {
        if (getIntent().getParcelableExtra(KEY_CATALOGUE) != null) {
            notNull();

        } else if ((getIntent().getParcelableExtra(KEY_CATALOGUE_FAVORITE) != null) || (getIntent().getParcelableArrayListExtra(KEY_CATALOGUE_LIST) != null)) {
            if (getIntent().getParcelableExtra(KEY_CATALOGUE_FAVORITE) != null) {
                favorite = getIntent().getParcelableExtra(KEY_CATALOGUE_FAVORITE);
            } else {
                ArrayList<Favorite> listFav = getIntent().getParcelableArrayListExtra(KEY_CATALOGUE_LIST);
                favorite = listFav.get(0);
            }
            Cursor cursor = getContentResolver().query(URI_FAVORITE, null, favorite.getKind(), null, null);
            Favorites = cursorMapping(cursor);
            onClickFavorite();

            getSupportActionBar().setTitle(favorite.getTitle());
            tvTitle.setText(favorite.getTitle());
            tvRelease.setText(favorite.getRelease());
            tvDescription.setText(favorite.getOverview());
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500/" + favorite.getPoster())
                    .into(Image);
        }
    }

    private void viewLoad() {
        loading = ProgressDialog.show(this, null, "Wait...", true, false);
        loading.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.dismiss();
            }
        }, 2000);
    }

    public String convertDate(String date) {

        SimpleDateFormat simpleDateA = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat simpleDateB = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        Date dates = null;
        try {
            dates = simpleDateA.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String simpleDate = simpleDateB.format(dates);
        simpleDate = simpleDate.replace("-", " ");
        return ((simpleDate));
    }

    private void widgetUpdate() {
        Intent updateWidgetIntent = new Intent(this, FavoriteCollectionWidget.class);
        updateWidgetIntent.setAction(GET_WIDGET_UPDATE);
        sendBroadcast(updateWidgetIntent);
    }

    private void FavoriteDetail() {
        boolean btnFavoriteSit = false;
        for (Favorite catalogueFavorite : Favorites) {
            if (catalogueFavorite.getId() == movie.getId()) {
                btnFavoriteSit = true;
                btnFavorite.setImageResource(R.drawable.ic_favorite_black);
                btnFavorite.setOnClickListener(v -> {
                    getContentResolver().delete(Uri.parse(URI_FAVORITE + "/" + movie.getId()), null, null);
                    Toast.makeText(DetailActivity.this, "You Not Marked " + getText(R.string.favorite), Toast.LENGTH_SHORT).show();
                    widgetUpdate();
                    finish();
                    startActivity(getIntent());
                });
            }
        }
        if (!btnFavoriteSit) {
            btnFavorite.setOnClickListener(v -> {
                procFavorite(movie.getId(), kind, title, release, movie.getOverview(), movie.getPoster());
                widgetUpdate();
                finish();
                startActivity(getIntent());
            });
        }

    }

    private void procFavorite(Long id, String catalogueName, String title, String release, String overview, String poster) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_KIND, catalogueName);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_RELEASE, release);
        contentValues.put(COLUMN_OVERVIEW, overview);
        contentValues.put(COLUMN_POSTER, poster);
        getContentResolver().insert(URI_FAVORITE, contentValues);
        Toast.makeText(DetailActivity.this, "You Marked " + getText(R.string.favorite), Toast.LENGTH_SHORT).show();
    }

    private void onClickFavorite() {
        boolean btnFavoriteSit = false;
        for (Favorite catalogueFavorite : Favorites) {
            if (catalogueFavorite.getId() == favorite.getId()) {
                btnFavoriteSit = true;
                btnFavorite.setImageResource(R.drawable.ic_favorite_black);
                btnFavorite.setOnClickListener(v -> {
                    getContentResolver().delete(Uri.parse(URI_FAVORITE + "/" + favorite.getId()), null, null);
                    Toast.makeText(DetailActivity.this, "You Not Marked " + getText(R.string.favorite), Toast.LENGTH_SHORT).show();
                    widgetUpdate();
                    finish();
                    startActivity(getIntent());
                });
            }
        }
        if (!btnFavoriteSit) {
            btnFavorite.setOnClickListener(v -> {
                procFavorite(favorite.getId(), favorite.getKind(), favorite.getTitle(), favorite.getRelease(), favorite.getOverview(), favorite.getPoster());
                widgetUpdate();
                finish();
                startActivity(getIntent());
            });
        }
    }
}
