package phase2.trade.address;

import javax.persistence.*;

@Entity
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String firstName;
    private String lastName;

    private String country;
    private String territory;
    private String city;
    private String postalCode;
    private String firstAddressLine;
    private String secondAddressLine;

    public Address(String country, String territory, String city, String firstAddressLine, String secondAddressLine, String postalCode) {
        this.country = country;
        this.territory = territory;
        this.city = city;
        this.firstAddressLine = firstAddressLine;
        this.secondAddressLine = secondAddressLine;
    }

    public Address(String country, String city, String firstAddressLine, String secondAddressLine) {
        this(country, null, city, firstAddressLine, secondAddressLine, null);
    }

    public Address(String country, String city) {
        this(country, city, null, null);
    }

    public Address() {

    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstAddressLine() {
        return firstAddressLine;
    }

    public void setFirstAddressLine(String street) {
        this.firstAddressLine = street;
    }

    public String getSecondAddressLine() {
        return secondAddressLine;
    }

    public void setSecondAddressLine(String streetNumber) {
        this.secondAddressLine = streetNumber;
    }

    public String getTerritory() {
        return territory;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }
}
