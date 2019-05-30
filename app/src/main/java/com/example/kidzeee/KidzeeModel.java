package com.example.kidzeee;

public class KidzeeModel {

    private String ImageUri;
    private String OrgName;

    public KidzeeModel(String imageUri, String orgName) {
        ImageUri = imageUri;
        OrgName = orgName;
    }
    public String getImageUri() {
        return ImageUri;
    }

    public String getOrgName() {
        return OrgName;
    }
}
