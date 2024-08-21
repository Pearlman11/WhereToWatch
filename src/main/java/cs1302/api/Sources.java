
package cs1302.api;

import com.google.gson.annotations.SerializedName;
/**
 * Used to store sources in response.
 */

public class Sources {


    @SerializedName("source_id")
    public Integer sourceid;

    public String name;

    @SerializedName("web_url")
    public String weburl;

    public Double price;
    public String type;



    /**
     * metod used to get the price of the show or movie.
     * @return the price.
     */

    public Double getPrice() {
        return this.price;
    } // getPrice
}
