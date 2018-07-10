package plague_simulator.message;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import plague_simulator.message.MessageResolver;
import plague_simulator.message.MissingKeyException;

// Resolves messages from ResourceBundle object.
@RequiredArgsConstructor
public class ResourceBundleMessageResolver implements MessageResolver {
    private final @Getter ResourceBundle bundle;


    public ResourceBundleMessageResolver(String bundleName) {
        this(ResourceBundle.getBundle(bundleName));
    }

    public ResourceBundleMessageResolver(String bundleName, Locale locale) {
        this(ResourceBundle.getBundle(bundleName, locale));
    }


    public String getMessage(String key) throws MissingKeyException {
        try {
            return getBundle().getString(key);
        } catch (MissingResourceException e) {
            throw new MissingKeyException(e);
        }
    }
}
