package me.loper.storage.sql.connection.factory;

import com.zaxxer.hikari.HikariConfig;
import me.loper.storage.sql.SqlStorageCredentials;

import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

public class PostgreConnectionFactory extends HikariConnectionFactory {
    public PostgreConnectionFactory(SqlStorageCredentials configuration) {
        super(configuration);
    }

    @Override
    public String getImplementationName() {
        return "PostgreSQL";
    }

    @Override
    protected void appendProperties(HikariConfig config, Map<String, String> properties) {
        // remove the default config properties which don't exist for PostgreSQL
        properties.remove("useUnicode");
        properties.remove("characterEncoding");

        super.appendProperties(config, properties);
    }

    @Override
    protected void appendConfigurationInfo(HikariConfig config) {
        String address = this.configuration.getAddress();
        String[] addressSplit = address.split(":");
        address = addressSplit[0];
        String port = addressSplit.length > 1 ? addressSplit[1] : "5432";

        String database = this.configuration.getDatabase();
        String username = this.configuration.getUsername();
        String password = this.configuration.getPassword();

        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("serverName", address);
        config.addDataSourceProperty("portNumber", port);
        config.addDataSourceProperty("databaseName", database);
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
    }

    @Override
    protected Logger getLogger() {
        return null;
    }

    @Override
    public Function<String, String> getStatementProcessor() {
        return s -> s.replace("'", "\"");
    }
}
