package com.intent.moviecatalogue.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.fragment.FavoriteParentFragment;
import com.intent.moviecatalogue.fragment.MoviesFragment;
import com.intent.moviecatalogue.fragment.TvShowFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intent.moviecatalogue.Public.KEY_SEARCH_MOVIES;
import static com.intent.moviecatalogue.Public.KEY_SEARCH_TVSHOW;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String kind;
    private MenuItem search;

    @BindView(R.id.bottom_nav_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("List Movies");
        }
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_movies);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.navigation_movies:
                if (search != null) {
                    search.setVisible(true);
                }
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("List Movies");
                }
                kind = getString(R.string.movies);
                fragment = new MoviesFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                return true;

            case R.id.navigation_tv_show:
                search.setVisible(true);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("List Tv Show");
                }
                kind = getString(R.string.tv_show);
                fragment = new TvShowFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                return true;

            case R.id.navigation_favorite:
                search.setVisible(false);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Your Favorite");
                }
                kind = getString(R.string.favorite);
                fragment = new FavoriteParentFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        search = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.close) {
            closeAction();
            return true;
        }

        if (id == R.id.action_search) {
            searchAction(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeAction() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave ??");
        builder.setPositiveButton("Yes. Exit Now!", (dialogInterface, i) -> finish());
        builder.setNegativeButton("Not Now", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void searchAction(MenuItem item) {
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Please Insert The Title");
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Bundle bundle = new Bundle();
                if (kind.equals(getString(R.string.movies))) {
                    bundle.putString(KEY_SEARCH_MOVIES, s);
                    MoviesFragment moviesFragment = new MoviesFragment();
                    moviesFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, moviesFragment, moviesFragment.getClass().getSimpleName()).commit();
                } else if (kind.equals(getString(R.string.tv_show))) {
                    bundle.putString(KEY_SEARCH_TVSHOW, s);
                    TvShowFragment tvShowFragment = new TvShowFragment();
                    tvShowFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tvShowFragment, tvShowFragment.getClass().getSimpleName()).commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


