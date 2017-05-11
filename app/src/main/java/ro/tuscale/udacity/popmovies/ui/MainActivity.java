package ro.tuscale.udacity.popmovies.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ro.tuscale.udacity.popmovies.ui.adapters.MoviesAdapter;
import ro.tuscale.udacity.popmovies.R;
import ro.tuscale.udacity.popmovies.backend.SortingType;
import ro.tuscale.udacity.popmovies.models.Movie;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieSortHandler, MoviesAdapter.MovieClickHandler {
    private final SortingType STARTUP_SORTBY_CRITERION = SortingType.MOST_POPULAR;

    private AlertDialog mSortOptionsDlg;
    private ProgressDialog mLoadingDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the movies list posters. Start with dependencies first
        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        final MoviesAdapter moviesAdapter = new MoviesAdapter(this.getApplicationContext(), STARTUP_SORTBY_CRITERION, this);

        moviesAdapter.setMovieClickHandler(this);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setAdapter(moviesAdapter);

        // Initialize the sorting dialog
        AlertDialog.Builder sortOptionsDialogBuilder = new AlertDialog.Builder(this);
        sortOptionsDialogBuilder.setTitle(R.string.sort_dialog_title);
        sortOptionsDialogBuilder.setSingleChoiceItems(R.array.sort_options, STARTUP_SORTBY_CRITERION.ordinal(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int sortOptionId) {
                SortingType availableOrderbyCriterias[] = SortingType.values();

                if (sortOptionId > availableOrderbyCriterias.length) {
                    return;
                }

                moviesAdapter.resetAndLoadByOrder(availableOrderbyCriterias[sortOptionId]);
                dialog.dismiss();
            }
        });
        mSortOptionsDlg = sortOptionsDialogBuilder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_sort) {
            mSortOptionsDlg.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewSortStarted() {
        if (mLoadingDlg == null) {
            mLoadingDlg = new ProgressDialog(this);
            mLoadingDlg.setIndeterminate(true);
            mLoadingDlg.setCancelable(false);
            mLoadingDlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        finish();
                    }
                    return true;
                }
            });
            mLoadingDlg.setMessage("Fetching movies ...");
        }
        mLoadingDlg.show();
    }

    @Override
    public void onNewSortApplied() {
        if (mLoadingDlg != null) {
            mLoadingDlg.dismiss();
            mLoadingDlg = null;
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);

        movieDetailsIntent.putExtra(MovieDetailsActivity.MOVIE_EXTRA_INTENT_KEY, movie);
        startActivity(movieDetailsIntent);
    }
}
