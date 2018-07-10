package plague_simulator.message;

import plague_simulator.message.MissingKeyException;

public interface MessageResolver {
    // Resolves message from its key.
    String getMessage(String key) throws MissingKeyException;
}
