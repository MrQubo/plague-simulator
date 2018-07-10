package plague_simulator.config;

import java.util.List;

import plague_simulator.config.Config;
import plague_simulator.config.ConfigValidatorImpl;

public interface ConfigValidator {
    // Validate Config object and return found errors messages.
    List<String> validateConfig(Config config);


    static ConfigValidator createConfigValidator() {
        return new ConfigValidatorImpl();
    }
}
