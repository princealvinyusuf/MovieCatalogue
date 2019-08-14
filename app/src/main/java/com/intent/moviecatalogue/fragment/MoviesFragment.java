package com.intent.moviecatalogue.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intent.moviecatalogue.Public;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.adapter.CatalogueMovieAdapter;
import com.intent.moviecatalogue.api.GetClient;
import com.intent.moviecatalogue.interfaces.ApiServiceInterface;
import com.intent.moviecatalogue.model.FeedBackMovies;
import com.intent.moviecatalogue.model.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.intent.moviecatalogue.Public.KEY_MOVIES;
import static com.intent.moviecatalogue.Public.KEY_SEARCH_MOVIES;
import static com.intent.moviecatalogue.Public.apiKey;

public class MoviesFragment extends Fragment {

    @BindView(R.id.rv)
    RecyclerView rvMovie;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    CatalogueMovieAdapter listMovieAdapter;

    public MoviesFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_film, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        ButterKnife.bind(this, view);

        layoutCreated();

        if (saveInstanceState != null) {
            Public.type = saveInstanceState.getParcelableArrayList(KEY_MOVIES);
            showAdapter(Public.type);
        } else {
            ApiServiceInterface apiClient = GetClient.getClient().create(ApiServiceInterface.class);
            Call<FeedBackMovies> call;
            if (getArguments() != null) {
                Public.title = this.getArguments().getString(KEY_SEARCH_MOVIES);
                call = apiClient.getSearch("movie", apiKey, Public.title);
            } else {
                call = apiClient.getType("movie", apiKey);
            }
            callApi(call);
        }
    }

    private void showAdapter(ArrayList<Movie> movies) {
        listMovieAdapter = new CatalogueMovieAdapter(this.getContext());
        listMovieAdapter.setListCatalogue(movies);
        rvMovie.setAdapter(listMovieAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void layoutCreated() {
        progressBar.setVisibility(View.VISIBLE);
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    private void callApi(@NotNull Call<FeedBackMovies> call) {
        call.enqueue(new Callback<FeedBackMovies>() {
            @Override
            public void onResponse(@NotNull Call<FeedBackMovies> call, @NotNull Response<FeedBackMovies> response) {
                if (response.body() != null) {
                    Public.type = response.body().getCatalogues();
                    showAdapter(Public.type);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedBackMovies> call, @NotNull Throwable t) {

            }
        });
    }


    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_MOVIES, Public.type);
    }
}
