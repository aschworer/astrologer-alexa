package aschworer.astrologer.alexa.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Data {

    @SerializedName("chart")
    private List<CharacteristicInSign> data = new ArrayList<>();

    public Data() {
    }

    public Data(List<CharacteristicInSign> data) {
        this.data = data;
    }

    public List<CharacteristicInSign> getData() {
        return data;
    }

    public void setData(List<CharacteristicInSign> data) {
        this.data = data;
    }
}
