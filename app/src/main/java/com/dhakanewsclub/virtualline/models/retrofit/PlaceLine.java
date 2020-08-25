package com.dhakanewsclub.virtualline.models.retrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lineId",
        "lineStatus",
        "checkedUser"
})
public class PlaceLine {

    @JsonProperty("lineId")
    private Integer lineId;
    @JsonProperty("lineStatus")
    private String lineStatus;
    @JsonProperty("checkedUser")
    private List<CheckedUser> checkedUser = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    @JsonProperty("lineId")
    public Integer getLineId() {
        return lineId;
    }

    @JsonProperty("lineId")
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    @JsonProperty("lineStatus")
    public String getLineStatus() {
        return lineStatus;
    }

    @JsonProperty("lineStatus")
    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }

    @JsonProperty("checkedUser")
    public List<CheckedUser> getCheckedUser() {
        return checkedUser;
    }

    @JsonProperty("checkedUser")
    public void setCheckedUser(List<CheckedUser> checkedUser) {
        this.checkedUser = checkedUser;
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
