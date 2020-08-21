package phase2.trade.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Database config, used to configure host, port, driver, dialect etc.<p>
 * Mysql and MariaDB are supported. PostgreSQL can be supported by changing some JPA annotations for schema.
 *
 * @author Dan Lyu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseConfig {

    private String databaseType = "mysql";

    private String host = "muddy.ca";

    private int port = 3308;

    private String database = "group";

    private String dialect = "auto";

    private String driver = "auto";

    private String url = "auto";

    private String username = "member";

    private String password = "aC4YD6G4J@Y";

    private int poolSize = 10;

    private String hbm2ddl = "update";

    private boolean showSQL = false;

    private boolean autoReconnect = true;

    private final transient Map<String, List<String>> preconfiguredDialect = new HashMap<>();


    private void configureDialects(String database, String dialect, String driver, String prefix) { // the driver library has to be added first
        this.preconfiguredDialect.put(database, new ArrayList<String>() {{
            add(dialect);
            add(driver);
            add(prefix);
        }});
    }

    /**
     * Gets configured url with jdbc header, host, port and database.
     *
     * @return the configured url
     */
    @JsonIgnore
    public String getConfiguredURL() {
        if (!getUrl().equals("auto")) return getUrl();
        return preconfiguredDialect.get(databaseType).get(2) + getHost() + ":" + getPort() + "/" + getDatabase();
    }

    /**
     * Gets configured dialect, used only if "auto" is the value of dialect in the configuration.
     *
     * @return the configured dialect
     */
    @JsonIgnore
    public String getConfiguredDialect() {
        if (!getDialect().equals("auto")) return getDialect();
        return preconfiguredDialect.get(databaseType).get(0);
    }

    /**
     * Gets configured driver.
     *
     * @return the configured driver
     */
    @JsonIgnore
    public String getConfiguredDriver() {
        if (!getDialect().equals("auto")) return getDialect();
        return preconfiguredDialect.get(databaseType).get(1);
    }

    /**
     * Is auto reconnect boolean.
     *
     * @return the boolean
     */
    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    /**
     * Sets auto reconnect.
     *
     * @param autoReconnect the auto reconnect
     */
    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    /**
     * Gets database type.
     *
     * @return the database type
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * Sets database type.
     *
     * @param databaseType the database type
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets pool size.
     *
     * @return the pool size
     */
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * Sets pool size.
     *
     * @param poolSize the pool size
     */
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * Gets hbm 2 ddl.
     *
     * @return the hbm 2 ddl
     */
    public String getHbm2ddl() {
        return hbm2ddl;
    }

    /**
     * Sets hbm 2 ddl.
     *
     * @param hbm2ddl the hbm 2 ddl
     */
    public void setHbm2ddl(String hbm2ddl) {
        this.hbm2ddl = hbm2ddl;
    }

    /**
     * Is show sql boolean.
     *
     * @return the boolean
     */
    public boolean isShowSQL() {
        return showSQL;
    }

    /**
     * Sets show sql.
     *
     * @param showSQL the show sql
     */
    public void setShowSQL(boolean showSQL) {
        this.showSQL = showSQL;
    }

    /**
     * Gets dialect.
     *
     * @return the dialect
     */
    public String getDialect() {
        return dialect;
    }

    /**
     * Sets dialect.
     *
     * @param dialect the dialect
     */
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    /**
     * Gets driver.
     *
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Sets driver.
     *
     * @param driver the driver
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets database.
     *
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Sets database.
     *
     * @param database the database
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Constructs a new Database config.
     */
    public DatabaseConfig() {
        configureDialects("mysql", "org.hibernate.dialect.MySQL5Dialect", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://");
        configureDialects("mariadb", "org.hibernate.dialect.MariaDB53Dialect", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://"); // mariadb is compatible with mysql
        configureDialects("postgresql", "org.hibernate.dialect.PostgreSQLDialect", "org.postgresql.Driver", "jdbc:postgresql://"); // schema has to be configured in entity table to support postgresql
        // mssql
    }
}
