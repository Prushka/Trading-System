package phase2.trade.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import phase2.trade.Main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

// The resource /json/ca.json comes from https://simplemaps.com/data/ca-cities
public class GeoConfig {

    Map<String, Map<String, Set<String>>> map = new HashMap<>();

    public GeoConfig() {


        InputStream is = Main.class.getResourceAsStream("/json/ca.json");
        String jsonTxt = null;
        try {
            jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);

            JSONObject json = new JSONObject(jsonTxt);
            JSONArray jsonArray = json.getJSONArray("list");

            if (jsonArray != null) {

                // someone rewrite these 3 loops
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ObservableList<String> getCountries() {
        ObservableList<String> countries = FXCollections.observableArrayList(map.keySet());
        Collections.sort(countries);
        return countries;
    }

    public ObservableList<String> getProvincesByCountry(String country) {
        ObservableList<String> provinces = FXCollections.observableArrayList();
        if (map.containsKey(country)) {
            provinces.addAll(map.get(country).keySet());
            Collections.sort(provinces);
        }
        return provinces;
    }

    public ObservableList<String> getCitiesByProvinceCountry(String country, String province) {
        ObservableList<String> cities = FXCollections.observableArrayList();
        if (map.containsKey(country) && map.get(country).containsKey(province)) {
            cities.addAll(map.get(country).get(province));
            Collections.sort(cities);
        }
        Collections.sort(cities);
        return cities;
    }


    public Map<String, Map<String, Set<String>>> getMap() {
        return map;
    }

    public void setMap(Map<String, Map<String, Set<String>>> map) {
        this.map = map;
    }
}
