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
        "checkedId",
        "user"
})
public class CheckedUser {

    @JsonProperty("checkedId")
    private Integer checkedId;
    @JsonProperty("user")
    private UserInfo user;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("checkedId")
    public Integer getCheckedId() {
        return checkedId;
    }

    @JsonProperty("checkedId")
    public void setCheckedId(Integer checkedId) {
        this.checkedId = checkedId;
    }

    @JsonProperty("user")
    public UserInfo getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(UserInfo user) {
        this.user = user;
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