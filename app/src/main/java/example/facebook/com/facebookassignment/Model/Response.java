package example.facebook.com.facebookassignment.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mausamkumari on 3/29/17.
 */

public class Response implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.category);
        parcel.writeString(this.picture);
    }

    protected Response(Parcel in) {
        this.name = in.readString();
        this.category = in.readString();
        this.picture = in.readString();
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        public Response createFromParcel(Parcel source) {
            return new Response(source);
        }

        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

}
