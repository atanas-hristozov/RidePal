package com.example.ridepal.models.dtos;

public class LocationDto {
    private String country;
    private String administrativeDistrict;
    private String locality;
    private String address;

    public LocationDto() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdministrativeDistrict() {
        return administrativeDistrict;
    }

    public void setAdministrativeDistrict(String administrativeDistrict) {
        this.administrativeDistrict = administrativeDistrict;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
