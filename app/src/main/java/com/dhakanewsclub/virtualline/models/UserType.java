package com.dhakanewsclub.virtualline.models;
 class UserType {
    private String typeId,typeName;

    public UserType(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public UserType() {

    }

    public String getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

}
