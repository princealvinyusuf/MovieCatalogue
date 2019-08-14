package com.intent.favoritemovie.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.intent.favoritemovie.R;
import com.intent.favoritemovie.database.Favorite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intent.favoritemovie.Public.COLUMN_ID;
import static com.intent.favoritemovie.Public.COLUMN_KIND;
import static com.intent.favoritemovie.Public.COLUMN_OVERVIEW;
import static com.intent.favoritemovie.Public.COLUMN_POSTER;
import static com.intent.favoritemovie.Public.COLUMN_RELEASE;
import static com.intent.favoritemovie.Public.COLUMN_TITLE;
import static com.intent.favoritemovie.Public.KEY_CATALOGUE_FAVORITE;
import static com.intent.favoritemovie.Public.URI_FAVORITE;
import static com.intent.favoritemovie.database.Favorite.cursorMapping;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tvRelease)
    TextView tvRelease;
    @BindView(R.id.tvOverview)
    TextView tvOverview;
    @BindView(R.id.Image)
    ImageView Image;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.btnFavorite)
    ImageView btnFavorite;
    Favorite favorite;
    ArrayList<Favorite> Favorites;

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if (getIntent().getParcelableExtra(KEY_CATALOGUE_FAVORITE) != null) {
            notNull();
        }
    }

    private void notNull() {
        favorite = getIntent().getParcelableExtra(KEY_CATALOGUE_FAVORITE);
        getSupportActionBar().setTitle(favorite.getTitle());

        Cursor cursor = getContentResolver().query(URI_FAVORITE, null, favorite.getCatalogue(), null, null);
        Favorites = cursorMapping(cursor);
        onClickFavoriteFavorite();

        tvRelease.setText(favorite.getRelease());
        tvOverview.setText(favorite.getOverview());
        progressBar.setVisibility(View.GONE);

        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500/" + favorite.getPoster())
                .into(Image);
    }

    private void insertFavorite(Long id, String catalogueName, String title, String release, String overview, String poster) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_KIND, catalogueName);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_RELEASE, release);
        cv.put(COLUMN_OVERVIEW, overview);
        cv.put(COLUMN_POSTER, poster);
        getContentResolver().insert(URI_FAVORITE, cv);
        Toast.makeText(DetailActivity.this, "You Marked " + getText(R.string.favorite), Toast.LENGTH_SHORT).show();
    }

    private void onClickFavoriteFavorite() {
        boolean fabCondition = false;
        for (Favorite catalogueFavorite : Favorites) {
            if (catalogueFavorite.getId() == favorite.getId()) {
                fabCondition = true;
                btnFavorite.setImageResource(R.drawable.ic_favorite_black);
                btnFavorite.setOnClickListener(v -> {
                    getContentResolver().delete(Uri.parse(URI_FAVORITE + "/" + favorite.getId()), null, null);
                    Toast.makeText(DetailActivity.this, "You Not Marked " + getText(R.string.favorite), Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                });
            }
        }
        if (!fabCondition) {
            btnFavorite.setOnClickListener(v -> {
                insertFavorite(favorite.getId(), favorite.getCatalogue(), favorite.getTitle(), favorite.getRelease(), favorite.getOverview(), favorite.getPoster());
                finish();
                startActivity(getIntent());
            });
        }
    }


}
