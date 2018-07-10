package plague_simulator.message;

import java.util.Collection;

import plague_simulator.message.Variable;

public interface MessageInterpolator {
    // Interpolates message template with variables.
    String interpolate(String template, Collection<? extends Variable> variables);
}
