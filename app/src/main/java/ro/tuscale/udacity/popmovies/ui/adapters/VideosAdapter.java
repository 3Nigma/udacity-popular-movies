package ro.tuscale.udacity.popmovies.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.functions.Consumer;
import ro.tuscale.udacity.popmovies.ItemClickHandler;
import ro.tuscale.udacity.popmovies.databinding.MovieTrailerItemBinding;
import ro.tuscale.udacity.popmovies.models.Movie;
import ro.tuscale.udacity.popmovies.models.Video;
import ro.tuscale.udacity.popmovies.models.VideosList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder>
        implements Consumer<VideosList>, ItemClickHandler {

    private Context mContext;
    private List<Video> mVideos;
    private VideoClickHandler mVideoClickedHandler;

    public VideosAdapter(@NonNull Context ctx, @NonNull Movie movie) {
        this.mContext = ctx;

        // Request the videos
        movie.getVideosList().subscribe(this);
    }

    public VideosAdapter setVideoClickedHandler(VideoClickHandler vcHandler) {
        this.mVideoClickedHandler = vcHandler;

        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieTrailerItemBinding vhBinding = MovieTrailerItemBinding.inflate(
                LayoutInflater.from(mContext),
                parent, false);

        return new ViewHolder(mContext, vhBinding).setItemClickHandler(this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mVideos.get(position));
    }

    @Override
    public int getItemCount() {
        return (mVideos == null ? 0 : mVideos.size());
    }

    @Override
    public void accept(VideosList videosList) throws Exception {
        this.mVideos = videosList.getVideos();
        notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int itemId) {
        if (mVideoClickedHandler != null) mVideoClickedHandler.onVideoClicked(mVideos.get(itemId));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private MovieTrailerItemBinding mBindedView;
        private ItemClickHandler mItemClickedListener;

        ViewHolder(Context ctx, MovieTrailerItemBinding v) {
            super(v.movieTrailerContainer);

            this.mContext = ctx;
            this.mBindedView = v;
            v.movieTrailerContainer.setOnClickListener(new View.OnClickListener() {
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

        void bind(Video video) {
            mBindedView.setVideo(video);
        }
    }

    public interface VideoClickHandler {
        void onVideoClicked(Video video);
    }
}
