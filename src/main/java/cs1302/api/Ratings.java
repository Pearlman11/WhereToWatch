
package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * used to store tatings for api response.
 */
public class Ratings {
    String source;

    @SerializedName("Value")
    String value;


}
