package cs1302.api;

import com.google.gson.annotations.SerializedName;


/**
 * Class used for Omdb api response.
 */

public class OmdResponse {
    @SerializedName("Title")
    String title;


    @SerializedName("Year")
    String year;
    @SerializedName("Rating")
    String rating;
    @SerializedName("ReleaseDate")
    String releaseDate;

    @SerializedName("Genre")
    String genre;

    @SerializedName("director")
    String director;

    @SerializedName("Actors")
    String actors;

    @SerializedName("Plot")
    String plot;

    @SerializedName("Awards")
    String awards;

    @SerializedName("Poster")
    String poster;

    @SerializedName("Ratings")
    Ratings[] ratings;

    double imdbRating;

}
