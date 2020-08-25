package com.dhakanewsclub.virtualline.models;

class PlaceType {
    private String typeId;
    private String typeName;

    public String getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public PlaceType(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
}
