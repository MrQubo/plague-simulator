package plague_simulator.global;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

import plague_simulator.config.Config;
import plague_simulator.global.MessageComposer;

public enum Global {
    // region singleton
    INSTANCE;

    static public Global getInstance() {
        return INSTANCE;
    }
    // endregion


    @Getter
    @Setter
    private Config config;

    @Getter
    @Setter
    private MessageComposer messageComposer;


    static public Config getConfigInstance() {
        return getInstance().getConfig();
    }

    static public void setConfigInstance(Config newConfig) {
        getInstance().setConfig(newConfig);
    }

    static public MessageComposer getMessageComposerInstance() {
        return getInstance().getMessageComposer();
    }

    static public void setMessageComposerInstance(MessageComposer newMessageComposer) {
        getInstance().setMessageComposer(newMessageComposer);
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
