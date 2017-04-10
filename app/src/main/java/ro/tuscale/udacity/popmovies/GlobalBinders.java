package ro.tuscale.udacity.popmovies;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

public class GlobalBinders {
    @BindingAdapter("bind:mvvmToBind")
    public static void bindImageViewToMovieDetailsViewModel(ImageView imgView, MovieDetailsViewModel model) {
        model.loadPosterImage().into(imgView);
    }
}
