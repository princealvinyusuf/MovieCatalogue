package com.intent.favoritemovie.activity;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intent.favoritemovie.R;
import com.intent.favoritemovie.adapter.CatalogueFavoriteAdapter;
import com.intent.favoritemovie.database.Favorite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intent.favoritemovie.Public.LOADER_FAVORITE;
import static com.intent.favoritemovie.Public.URI_FAVORITE;
import static com.intent.favoritemovie.database.Favorite.cursorMapping;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_movies_favoraite)
    RecyclerView rvFavorite;
    private CatalogueFavoriteAdapter listFavoriteAdapter;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            if (i == LOADER_FAVORITE) {
                if (getApplicationContext() != null) {
                    return new CursorLoader(getApplicationContext(), URI_FAVORITE,
                            null,
                            "movie",
                            null,
                            null);
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            if (loader.getId() == LOADER_FAVORITE) {
                ArrayList<Favorite> favoriteArrayList = cursorMapping(cursor);
                if (favoriteArrayList.size() > 0) {
                    listFavoriteAdapter.setListFavorite(favoriteArrayList);
                } else {
                    listFavoriteAdapter.setListFavorite(new ArrayList<Favorite>());
                }
                rvFavorite.setAdapter(listFavoriteAdapter);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (loader.getId() == LOADER_FAVORITE) {
                listFavoriteAdapter.setListFavorite(null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        createLayout();

        LoaderManager.getInstance(this).initLoader(LOADER_FAVORITE, null, loaderCallbacks);
    }

    private void createLayout() {
        rvFavorite.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        listFavoriteAdapter = new CatalogueFavoriteAdapter(this.getApplicationContext());
        rvFavorite.setAdapter(listFavoriteAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LoaderManager.getInstance(this).restartLoader(LOADER_FAVORITE, null, loaderCallbacks);
    }
}
