package ro.tuscale.udacity.popmovies.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.functions.Consumer;
import ro.tuscale.udacity.popmovies.databinding.MovieCommentItemBinding;
import ro.tuscale.udacity.popmovies.models.Movie;
import ro.tuscale.udacity.popmovies.models.Review;
import ro.tuscale.udacity.popmovies.models.ReviewsPage;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>
    implements Consumer<ReviewsPage> {
    private Context mContext;
    private List<Review> mReviews;

    public ReviewsAdapter(@NonNull Context ctx, @NonNull Movie movie) {
        this.mContext = ctx;

        // Request the videos
        // TODO: get other pages as well if required
        movie.getReviewsPage(1).subscribe(this);
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieCommentItemBinding vhBinding = MovieCommentItemBinding.inflate(
                LayoutInflater.from(mContext),
                parent, false);

        return new ReviewsAdapter.ViewHolder(mContext, vhBinding);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        holder.bind(mReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return (mReviews == null ? 0 : mReviews.size());
    }

    @Override
    public void accept(ReviewsPage reviewsPage) throws Exception {
        this.mReviews = reviewsPage.getReviews();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private MovieCommentItemBinding mBindedView;

        ViewHolder(Context ctx, MovieCommentItemBinding v) {
            super(v.movieCommentsContainer);

            this.mBindedView = v;
        }

        void bind(Review review) {
            mBindedView.setReview(review);
        }
    }
}
