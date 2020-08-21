package phase2.trade.address;

import javax.persistence.*;

/**
 * The Address of a user or a Trade.
 *
 * @author Dan Lyu
 * @author Grace Leung
 * @see AddressBook
 */
@Entity
@Embeddable
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String territory;
    private String city;
    private String postalCode;
    private String firstAddressLine;
    private String secondAddressLine;

    /**
     * Constructs a new Address.
     *
     * @param country           the country
     * @param territory         the territory
     * @param city              the city
     * @param firstAddressLine  the first address line
     * @param secondAddressLine the second address line
     * @param postalCode        the postal code
     */
    public Address(String country, String territory, String city, String firstAddressLine, String secondAddressLine, String postalCode) {
        this.country = country;
        this.territory = territory;
        this.city = city;
        this.firstAddressLine = firstAddressLine;
        this.secondAddressLine = secondAddressLine;
        this.postalCode = postalCode;
    }

    /**
     * Constructs a new Address.
     */
    public Address() {

    }

    /**
     * Gets the id of this Address.
     *
     * @return the uid
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of this Address.
     *
     * @param uid the uid
     */
    public void setId(Long uid) {
        this.id = uid;
    }

    /**
     * Gets the country of this Address.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of this Address.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the city of this Address.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of this Address.
     *
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets first address line.
     *
     * @return the first address line
     */
    public String getFirstAddressLine() {
        return firstAddressLine;
    }

    /**
     * Sets first address line.
     *
     * @param street the street
     */
    public void setFirstAddressLine(String street) {
        this.firstAddressLine = street;
    }

    /**
     * Gets second address line.
     *
     * @return the second address line
     */
    public String getSecondAddressLine() {
        return secondAddressLine;
    }

    /**
     * Sets second address line.
     *
     * @param streetNumber the street number
     */
    public void setSecondAddressLine(String streetNumber) {
        this.secondAddressLine = streetNumber;
    }

    /**
     * Gets territory of this Address.
     *
     * @return the territory
     */
    public String getTerritory() {
        return territory;
    }

    /**
     * Sets territory of this Address.
     *
     * @param territory the territory
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets postal code.
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final Address another = (Address) obj;
        return notNullEquals(another.country, country) &&
                notNullEquals(another.territory, territory) &&
                notNullEquals(another.city, city) &&
                notNullEquals(another.firstAddressLine, firstAddressLine) &&
                notNullEquals(another.secondAddressLine, secondAddressLine) &&
                notNullEquals(another.postalCode, postalCode);
    }

    private boolean notNullEquals(String s1, String s2) {
        if (s1 != null && s2 != null) {
            return s1.equals(s2);
        }
        return true;
    }
}
