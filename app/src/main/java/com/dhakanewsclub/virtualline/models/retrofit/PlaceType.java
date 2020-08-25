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
        "placeTypeId",
        "placeTypeName"
})
public class PlaceType {

    @JsonProperty("placeTypeId")
    private Integer placeTypeId;
    @JsonProperty("placeTypeName")
    private String placeTypeName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public PlaceType(Integer placeTypeId, String placeTypeName) {
        this.placeTypeId = placeTypeId;
        this.placeTypeName = placeTypeName;
    }

    @JsonProperty("placeTypeId")
    public Integer getPlaceTypeId() {
        return placeTypeId;
    }

    @JsonProperty("placeTypeId")
    public void setPlaceTypeId(Integer placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    @JsonProperty("placeTypeName")
    public String getPlaceTypeName() {
        return placeTypeName;
    }

    @JsonProperty("placeTypeName")
    public void setPlaceTypeName(String placeTypeName) {
        this.placeTypeName = placeTypeName;
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
