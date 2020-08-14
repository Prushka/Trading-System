package phase2.trade.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.database.DatabaseResourceBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseConfig {

    private static final Logger logger = LogManager.getLogger(DatabaseConfig.class);

    private String databaseType = "mysql";

    private String host = "muddy.ca";

    private int port = 3308;

    private String database = "group";

    private String dialect = "auto";

    private String driver = "auto";

    private String url = "auto";

    private String username = "member";

    private String password = "aC4YD6G4J@Y";

    private int connection_pool_size = 10;

    private String hbm2ddl = "update";

    private boolean showSQL = true;

    private boolean autoReconnect = true;

    private final transient Map<String, List<String>> preconfiguredDialect = new HashMap<>();


    private void configureDialects(String database, String dialect, String driver, String prefix) { // the driver library has to be added first
        this.preconfiguredDialect.put(database, new ArrayList<String>() {{
            add(dialect);
            add(driver);
            add(prefix);
        }});
    }

    @JsonIgnore
    public String getConfiguredURL() {
        if (!getUrl().equals("auto")) return getUrl();
        return preconfiguredDialect.get(databaseType).get(2) + getHost() + ":" + getPort() + "/" + getDatabase();
    }

    @JsonIgnore
    public String getConfiguredDialect() {
        if (!getDialect().equals("auto")) return getDialect();
        return preconfiguredDialect.get(databaseType).get(0);
    }

    @JsonIgnore
    public String getConfiguredDriver() {
        if (!getDialect().equals("auto")) return getDialect();
        return preconfiguredDialect.get(databaseType).get(1);
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public DatabaseConfig() {
        configureDialects("mysql", "org.hibernate.dialect.MySQL5Dialect", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://");
        configureDialects("mariadb", "org.hibernate.dialect.MariaDB53Dialect", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://"); // mariadb is compatible with mysql
        configureDialects("postgresql", "org.hibernate.dialect.PostgreSQLDialect", "org.postgresql.Driver", "jdbc:postgresql://"); // schema has to be configured in entity table to support postgresql
        // mssql
    }
}
