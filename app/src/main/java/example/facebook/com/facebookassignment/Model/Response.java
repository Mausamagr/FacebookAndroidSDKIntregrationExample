package example.facebook.com.facebookassignment.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mausamkumari on 3/29/17.
 */

public class Response {

    @SerializedName("name")
    public String name;

    @SerializedName("category")
    public String category;

    @SerializedName("url")
    public String picture;

    private static List<Response> responseList;

    public Response(String name, String category, String picture) {
        this.name = name;
        this.category = category;
        this.picture = picture;
    }

    public static void setData(List<Response> response) {
        responseList = response;
    }

    public static Response getResponse(int id) {
        for (Response item : responseList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public int getId() {
        return name.hashCode() + picture.hashCode();
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return picture;
    }

}
