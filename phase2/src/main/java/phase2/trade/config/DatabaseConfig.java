package phase2.trade.config;

public class DatabaseConfig {

    private String database = "mysql";

    private String url = "jdbc:mysql://muddy.ca:3308/group";

    private String username = "member";

    private String password = "aC4YD6G4J@Y";

    private int connection_pool_size = 10;

    private String hbm2ddl = "update";

    private boolean showSQL = true;

    private boolean autoReconnect = true;

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
}
