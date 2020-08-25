package com.dhakanewsclub.virtualline.models;

public class PlaceInformation {
    private String placeName,placeLocation;
    private PlaceType placeType;

    public PlaceInformation(String placeName, PlaceType placeType, String placeLocation) {
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeLocation = placeLocation;
    }
    public PlaceInformation(){

    }

    public String getPlaceName() {
        return placeName;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public String getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    public void setPlaceLocation(String placeLocation) {
        this.placeLocation = placeLocation;
    }
}
