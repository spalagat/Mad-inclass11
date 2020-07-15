package com.example.gmapsdemo;

public class PathCoordinates {


    Double latitude,longitude;
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PathCoordinates(){

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Places{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
