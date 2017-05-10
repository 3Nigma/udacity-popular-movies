package ro.tuscale.udacity.popmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import ro.tuscale.udacity.popmovies.backend.RestManager;
import ro.tuscale.udacity.popmovies.backend.SortingType;
import ro.tuscale.udacity.popmovies.models.Movie;
import ro.tuscale.udacity.popmovies.models.MoviesPage;
import timber.log.Timber;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> implements ItemClickHandler {
    private Context mContext;
    private SortingType mSortType;
    private int mLastLoadedPage;
    private List<Movie> mMovies;
    private boolean isRequestingMoviePage = false;

    private MovieClickHandler mItemClickHandler;
    private MovieSortHandler mSortChangedListener;

    public MoviesAdapter(@NonNull Context ctx, @NonNull SortingType sortOrder, MovieSortHandler sortChangedListener) {
        this.mContext = ctx;
        this.mSortChangedListener = sortChangedListener;

        resetAndLoadByOrder(sortOrder);
    }
    public MoviesAdapter(@NonNull Context ctx, @NonNull SortingType sortOrder) {
        this(ctx, sortOrder, null);
    }

    public MoviesAdapter setMovieClickHandler(MovieClickHandler handler) {
        this.mItemClickHandler = handler;

        return this;
    }

    public void resetAndLoadByOrder(SortingType desiredSortOrder) {
        if (mSortType != desiredSortOrder) {
            mSortType = desiredSortOrder;

            if (mSortType == SortingType.FAVORITED) {
                mMovies = Movie.getAllFavorites();

                notifyDataSetChanged();
            } else {
                if (mSortChangedListener != null) mSortChangedListener.onNewSortStarted();
                mLastLoadedPage = 0;
                if (mMovies != null) notifyItemRangeRemoved(0, mMovies.size());
                mMovies = new ArrayList<>();

                loadNextMoviePage();
            }
        }
    }

    private void loadNextMoviePage() {
        RestManager moviesRest = new RestManager();

        isRequestingMoviePage = true;
        moviesRest.getSortedMoviePage(mLastLoadedPage + 1, mSortType)
                .subscribe(new Consumer<MoviesPage>() {
                    @Override
                    public void accept(MoviesPage moviesPage) throws Exception {
                        List<Movie> moviesOnPage = moviesPage.getMovies();
                        int prevMoviesListSize = mMovies.size();

                        // Go over the received movies. We might have havorites among them in which case we
                        // just load the movie from local cache and use them directly, bypassing videos & comments
                        // api calls thus saving bandwidth
                        for (Movie movie : moviesOnPage) {
                            Movie favMovie = Movie.getFavoriteById(movie.getId());

                            if (favMovie != null) {
                                // Fav movie. Use it instead
                                mMovies.add(favMovie);
                            } else {
                                // Normal movie. Add it as it is
                                mMovies.add(movie);
                            }
                        }
                        notifyItemRangeInserted(prevMoviesListSize, moviesOnPage.size());
                        mLastLoadedPage++;
                        isRequestingMoviePage = false;
                        if (mSortChangedListener != null) mSortChangedListener.onNewSortApplied();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.w("There was an issue while retrieving the movie page: ", throwable.getMessage());
                    }
                });
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_main_poster, parent, false);

        return new ViewHolder(mContext, v).setItemClickHandler(this);
    }

    @Override
    public void onBindViewHolder(ViewHolder posterHolder, int position) {
        posterHolder.bind(mMovies.get(position));

        if (mSortType != SortingType.FAVORITED && position >= (8 * mMovies.size())/10 && isRequestingMoviePage == false) {
            // We passed the 80% mark. Request more movies
            loadNextMoviePage();
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public void onItemClicked(int itemId) {
        if (mItemClickHandler != null) mItemClickHandler.onMovieClicked(mMovies.get(itemId));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ImageView mMoviePoster;
        private ItemClickHandler mItemClickedListener;

        ViewHolder(Context ctx, ImageView v) {
            super(v);

            this.mContext = ctx;
            this.mMoviePoster = v;
            this.mMoviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickedListener != null) mItemClickedListener.onItemClicked(getAdapterPosition());
                }
            });
        }

        ViewHolder setItemClickHandler(ItemClickHandler movieClickedHandler) {
            this.mItemClickedListener = movieClickedHandler;

            return this;
        }

        void bind(Movie movie) {
            Picasso.with(mContext)
                    .load(movie.getPosterPath())
                    .placeholder(R.drawable.ic_movie_24dp)
                    .error(R.drawable.ic_error_outline_24dp)
                    .into(mMoviePoster);
        }
    }

    public interface MovieSortHandler {
        void onNewSortStarted();
        void onNewSortApplied();
    }

    public interface MovieClickHandler {
        void onMovieClicked(Movie movie);
    }
}
