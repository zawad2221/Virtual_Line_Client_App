package com.dhakanewsclub.virtualline.models.retrofit;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "placeId",
        "placeName",
        "placeAddress",
        "placeLongitude",
        "placeLatitude",
        "placeType",
        "placeLine",
        "owner"
})
public class PlaceInfo {
    @JsonProperty("placeId")
    private Integer placeId;
    @JsonProperty("placeName")
    private String placeName;
    @JsonProperty("placeAddress")
    private String placeAddress;
    @JsonProperty("placeLongitude")
    private String placeLongitude;
    @JsonProperty("placeLatitude")
    private String placeLatitude;
    @JsonProperty("placeType")
    private PlaceType placeType;
    @JsonProperty("placeLine")
    private PlaceLine placeLine;
    @JsonProperty("owner")
    private Integer owner;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("placeId")
    public Integer getPlaceId() {
        return placeId;
    }

    @JsonProperty("placeId")
    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    @JsonProperty("placeName")
    public String getPlaceName() {
        return placeName;
    }

    @JsonProperty("placeName")
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @JsonProperty("placeAddress")
    public String getPlaceAddress() {
        return placeAddress;
    }

    @JsonProperty("placeAddress")
    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    @JsonProperty("placeLongitude")
    public String getPlaceLongitude() {
        return placeLongitude;
    }

    @JsonProperty("placeLongitude")
    public void setPlaceLongitude(String placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    @JsonProperty("placeLatitude")
    public String getPlaceLatitude() {
        return placeLatitude;
    }

    @JsonProperty("placeLatitude")
    public void setPlaceLatitude(String placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    @JsonProperty("placeType")
    public PlaceType getPlaceType() {
        return placeType;
    }

    @JsonProperty("placeType")
    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    @JsonProperty("placeLine")
    public PlaceLine getPlaceLine() {
        return placeLine;
    }

    @JsonProperty("placeLine")
    public void setPlaceLine(PlaceLine placeLine) {
        this.placeLine = placeLine;
    }

    @JsonProperty("owner")
    public Integer getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
