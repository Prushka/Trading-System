package phase2.trade.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * The Geo config, used by view to set the possible Countries, Provinces and Cities.
 *
 * @author Dan Lyu
 */
// The resource /json/ca.json comes from https://simplemaps.com/data/ca-cities
public class GeoConfig {

    /**
     * The Map with Country -> Province -> City.
     */
    Map<String, Map<String, Set<String>>> map = new HashMap<>();

    /**
     * Constructs a new Geo config.
     */
    public GeoConfig(JSONObject json) {

        JSONArray jsonArray = json.getJSONArray("list");

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String country = jsonObject.getString("country");

                if (!map.containsKey(country)) {
                    map.put(country, new HashMap<>());
                }
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String country = jsonObject.getString("country");
                String admin = jsonObject.getString("admin");
                if (!map.get(country).containsKey(admin)) {
                    map.get(jsonObject.getString("country")).put(jsonObject.getString("admin"), new HashSet<>());
                }
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String country = jsonObject.getString("country");
                String admin = jsonObject.getString("admin");
                String city = jsonObject.getString("city");
                map.get(country).get(admin).add(city);
            }
        }

    }


    /**
     * Gets countries.
     *
     * @return the countries
     */
    public ObservableList<String> getCountries() {
        ObservableList<String> countries = FXCollections.observableArrayList(map.keySet());
        Collections.sort(countries);
        return countries;
    }

    /**
     * Gets provinces by country.
     *
     * @param country the country
     * @return the provinces by country
     */
    public ObservableList<String> getProvincesByCountry(String country) {
        ObservableList<String> provinces = FXCollections.observableArrayList();
        if (map.containsKey(country)) {
            provinces.addAll(map.get(country).keySet());
            Collections.sort(provinces);
        }
        return provinces;
    }

    /**
     * Gets cities by province country.
     *
     * @param country  the country
     * @param province the province
     * @return the cities by province country
     */
    public ObservableList<String> getCitiesByProvinceCountry(String country, String province) {
        ObservableList<String> cities = FXCollections.observableArrayList();
        if (map.containsKey(country) && map.get(country).containsKey(province)) {
            cities.addAll(map.get(country).get(province));
            Collections.sort(cities);
        }
        Collections.sort(cities);
        return cities;
    }


    /**
     * Gets map.
     *
     * @return the map
     */
    public Map<String, Map<String, Set<String>>> getMap() {
        return map;
    }

    /**
     * Sets map.
     *
     * @param map the map
     */
    public void setMap(Map<String, Map<String, Set<String>>> map) {
        this.map = map;
    }
}
