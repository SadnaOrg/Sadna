package BusinessLayer.System;

public class SupplyInfo {
    private String name;
    private String city;
    private String country;
    private String address;
    private String zip;

    public SupplyInfo(String name, String city, String country, String address, String zip) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.address = address;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }
}
