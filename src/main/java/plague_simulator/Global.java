package plague_simulator;

import java.util.Locale;
import java.util.Random;

import lombok.Getter;

import plague_simulator.Config;
import plague_simulator.Localized;

public enum Global {
  // region singleton
  INSTANCE;

  static public Global getInstance() {
    return INSTANCE;
  }
  // endregion


  private @Getter Config config = null;
  private @Getter Localized localized = null;

  static public Config getConfigInstance() {
    return getInstance().getConfig();
  }

  static public Localized getLocalizedInstance() {
    return getInstance().getLocalized();
  }

  void setConfig(Config newConfig) {
    if (this.config != null) { throw new IllegalStateException("Global config should be set only once during program execution"); }
    this.config = newConfig;
  }

  void setLocalized(Localized newLocalized) {
    this.localized = newLocalized;
  }

  void setLocale(Locale locale) {
    setLocalized(new Localized(locale));
  }


  private @Getter(lazy = true) final long seed     = calculateSeed();
  // Using just one Random instance is enough for almost all use cases.
  private @Getter(lazy = true) final Random random = createRandom();

  private long calculateSeed() {
    try {
      return Long.parseLong(getConfig().getSeed());
    } catch (NumberFormatException e) { }

    long h = 0L;
    for (char c : getConfig().getSeed().toCharArray()) {
      h = 31L * h + c;
    }
    return h;
  }

  private Random createRandom() {
    return new Random(getSeed());
  }
}
