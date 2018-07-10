package plague_simulator.config;

import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.io.AbsoluteNameLocationStrategy;
import org.apache.commons.configuration2.io.BasePathLocationStrategy;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.FileSystemLocationStrategy;

import plague_simulator.config.ConfigurationReaderImpl;

public interface ConfigurationReader {
    FileLocationStrategy getLocationStrategy();

    void setLocationStrategy(FileLocationStrategy locationStrategy);

    // Locate jproperties file and instantiate Configuration object from it.
    Configuration loadPropertiesConfiguration(String fileName);

    // Locate xml file and instantiate Configuration object from it.
    Configuration loadXMLConfiguration(String fileName);


    static final FileLocationStrategy DEFAULT_LOCATION_STRATEGY = new CombinedLocationStrategy(
            List.of(
              new ClasspathLocationStrategy(),
              new FileSystemLocationStrategy(),
              new BasePathLocationStrategy(),
              new AbsoluteNameLocationStrategy()
            )
        );

    static final FileLocationStrategy CLASSPATH_LOCATION_STRATEGY = new ClasspathLocationStrategy();


    static ConfigurationReader createConfigurationReader() {
        return new ConfigurationReaderImpl();
    }
}
