package com.intent.moviecatalogue.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intent.moviecatalogue.Public;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.adapter.CatalogueFavoriteAdapter;
import com.intent.moviecatalogue.database.Favorite;
import com.intent.moviecatalogue.provider.FavoriteProvider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intent.moviecatalogue.Public.FAVORITE_LOAD_KEY;
import static com.intent.moviecatalogue.database.Favorite.cursorMapping;


public class FavoriteMovieFragment extends Fragment {

    @BindView(R.id.rv_movies)
    RecyclerView rvMovie;
    CatalogueFavoriteAdapter listFavoriteAdapter;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            if (i == FAVORITE_LOAD_KEY) {
                if (getContext() != null) {
                    return new CursorLoader(getContext(), Public.URI_FAVORITE,
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
            if (loader.getId() == FAVORITE_LOAD_KEY) {
                ArrayList<Favorite> mFavoriteArrayList = cursorMapping(cursor);
                if (mFavoriteArrayList.size() > 0) {
                    listFavoriteAdapter.setListFavorite(mFavoriteArrayList);
                } else {
                    listFavoriteAdapter.setListFavorite(new ArrayList<Favorite>());
                }
                rvMovie.setAdapter(listFavoriteAdapter);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (loader.getId() == FAVORITE_LOAD_KEY) {
                listFavoriteAdapter.setListFavorite(null);
            }
        }
    };


    public FavoriteMovieFragment() {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        rvMovie.setHasFixedSize(true);

        createdLayout();

    }

    private void createdLayout() {
        rvMovie.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listFavoriteAdapter = new CatalogueFavoriteAdapter(this.getContext());
        LoaderManager.getInstance(this).initLoader(FAVORITE_LOAD_KEY, null, loaderCallbacks);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(FAVORITE_LOAD_KEY, null, loaderCallbacks);
    }
}
