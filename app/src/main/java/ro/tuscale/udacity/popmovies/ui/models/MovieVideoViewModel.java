package ro.tuscale.udacity.popmovies.ui.models;

import ro.tuscale.udacity.popmovies.models.Video;

public class MovieVideoViewModel {
    private Video mVideo;

    public MovieVideoViewModel(Video video) {
        this.mVideo = video;
    }

    public String getTitle() {
        return mVideo.getName();
    }
    public String getSource() {
        return mVideo.getSite();
    }
}
