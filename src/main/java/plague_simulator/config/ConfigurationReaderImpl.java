package plague_simulator.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLPropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileLocationStrategy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import plague_simulator.config.ConfigurationReader;

final class ConfigurationReaderImpl implements ConfigurationReader {
    @Getter(AccessLevel.PRIVATE)
    static private final Parameters parameters = new Parameters();


    @Getter
    @Setter
    private FileLocationStrategy locationStrategy = DEFAULT_LOCATION_STRATEGY;


    @Override
    // Locate jproperties file and instantiate Configuration object from it.
    public Configuration loadPropertiesConfiguration(String fileName) {
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
            .configure(
                    parameters.properties()
                    // .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                    .setEncoding("UTF-8")
                    .setLocationStrategy(getLocationStrategy())
                    .setFileName(fileName)
            );


        return getConfiguration(builder);
    }

    @Override
    // Locate xml file and instantiate Configuration object from it.
    public Configuration loadXMLConfiguration(String fileName) {
        FileBasedConfigurationBuilder<XMLPropertiesConfiguration> builder =
            new FileBasedConfigurationBuilder<>(XMLPropertiesConfiguration.class)
            .configure(
                    parameters.xml()
                    // .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                    .setEncoding("UTF-8")
                    .setLocationStrategy(getLocationStrategy())
                    .setFileName(fileName)
            );


        return getConfiguration(builder);
    }


    private Configuration getConfiguration(FileBasedConfigurationBuilder<? extends Configuration> builder) {
        try {
            return builder.getConfiguration();
        } catch(ConfigurationException e) {
            return null;
        }
    }
}
