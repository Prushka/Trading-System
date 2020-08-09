package phase2.trade.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConfig {

    private String database = "mysql";

    private String url = "jdbc:mysql://muddy.ca:3308/group";

    private String dialect = "auto";

    private String driver = "auto";

    private String username = "member";

    private String password = "aC4YD6G4J@Y";

    private int connection_pool_size = 10;

    private String hbm2ddl = "update";

    private boolean showSQL = true;

    private boolean autoReconnect = true;

    private final transient Map<String, List<String>> preconfiguredDialect = new HashMap<>();

    public DatabaseConfig() {
        configureDialects("mysql", "org.hibernate.dialect.MySQL5Dialect", "com.mysql.cj.jdbc.Driver");
    }

    private void configureDialects(String database, String dialect, String driver) { // the driver library has to be added first
        this.preconfiguredDialect.put(database, new ArrayList<String>() {{
            add(dialect);
            add(driver);
        }});
    }

    public String getConfiguredDialect() {
        if (!getDialect().equals("auto")) return getDialect();
        return preconfiguredDialect.get(database).get(0);
    }

    public String getConfiguredDriver() {
        if (!getDialect().equals("auto")) return getDialect();
        return preconfiguredDialect.get(database).get(1);
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnection_pool_size() {
        return connection_pool_size;
    }

    public void setConnection_pool_size(int connection_pool_size) {
        this.connection_pool_size = connection_pool_size;
    }

    public String getHbm2ddl() {
        return hbm2ddl;
    }

    public void setHbm2ddl(String hbm2ddl) {
        this.hbm2ddl = hbm2ddl;
    }

    public boolean isShowSQL() {
        return showSQL;
    }

    public void setShowSQL(boolean showSQL) {
        this.showSQL = showSQL;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    // use this only for debugging
    public String toString() {
        return String.format("Database: %s\nDialect: %s\nDriver: %s\nUrl: %s\nUserName: %s\nhbm2ddl: %s", database, getConfiguredDialect(), getConfiguredDriver(), getUrl(), getUsername(), getHbm2ddl());
    }
}
