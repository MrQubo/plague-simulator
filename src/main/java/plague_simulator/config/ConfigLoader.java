package plague_simulator.config;

import org.apache.commons.configuration2.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import plague_simulator.config.Config;
import plague_simulator.config.ConfigurationReader;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Combines features of ConfigurationReader with converting from Configuration to Config.
public class ConfigLoader {
    @Setter
    private ConfigurationReader configurationReader = ConfigurationReader.createConfigurationReader();


    public Config loadPropertiesConfig(String fileName, Config config) {
        Configuration configuration = getConfigurationReader().loadPropertiesConfiguration(fileName);

        return apply(configuration, config);
    }

    public Config loadXMLConfig(String fileName, Config config) {
        Configuration configuration = getConfigurationReader().loadXMLConfiguration(fileName);

        return apply(configuration, config);
    }


    private Config apply(Configuration configuration, Config config) {
        if (configuration == null) { return null; }

        Config clone = config.clone();
        clone.prependConfiguration(configuration);
        return clone;
    }
}
