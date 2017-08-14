package com.mani.property.home;

import com.mani.property.common.StatusOf;

/**
 * Created by Mani on 04-08-2017.
 */

public class PropertyModel {
private String street;
    private String city;
    private String state;

    public String getMap_amount() {
        return map_amount;
    }

    public void setMap_amount(String map_amount) {
        this.map_amount = map_amount;
    }

    private String map_amount;
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    //"zipcode:":"91342"
    private String zipcode;
    public String getZpid() {
        return zpid;
    }

    public void setZpid(String zpid) {
        this.zpid = zpid;
    }

    private String zpid;
    private String latitude;
    private String longitude;
    private String amount;
    private String squareaft;
    private String bedrooms;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    private boolean favorite;
    public String getPosting_type() {
        return posting_type;
    }

    public void setPosting_type(String posting_type) {
        this.posting_type = posting_type;
    }

    private String posting_type;

    public StatusOf getImages() {
        return images;
    }

    public void setImages(StatusOf images) {
        this.images = images;
    }

    StatusOf images;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSquareaft() {
        return squareaft;
    }

    public void setSquareaft(String squareaft) {
        this.squareaft = squareaft;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    private String bathrooms;
}
