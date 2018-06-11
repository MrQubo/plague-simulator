package plague_simulator;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Localized {
  private final @Getter Locale locale;
  private final @Getter(lazy = true) ResourceBundle bundle = ResourceBundle.getBundle("LocalizedBundle", getLocale());
  private final @Getter(lazy = true) MessageFormat formatter = new MessageFormat("", getLocale());

  public String getMessage(String key) {
    return getMessage(key, new Object[0]);
  }
  public String getMessage(String key, Object... arguments) {
    return getMessageArray(key, arguments);
  }
  public String getMessageArray(String key, Object[] arguments) {
    String message;
    try {
      message = getBundle().getString(key);
    } catch (MissingResourceException e) {
      message = key;
    }


    if (arguments.length == 0) {
      return message;
    }

    getFormatter().applyPattern(message);
    return getFormatter().format(arguments);
  }
}
