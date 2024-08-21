
package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Class for GSON watchMode api.
 */

public class WatchModeResponse {
    public String title;

    @SerializedName("plot_overview")
    public String plotoverview;

    @SerializedName("similar_titles")
    public String [] similartitles;
    public Sources[] sources;





}
