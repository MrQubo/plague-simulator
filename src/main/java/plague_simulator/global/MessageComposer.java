package plague_simulator.global;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import plague_simulator.message.MessageInterpolator;
import plague_simulator.message.MessageResolver;
import plague_simulator.message.Variable;

@NoArgsConstructor
@AllArgsConstructor
public class MessageComposer {
    @Getter
    @Setter
    private MessageResolver messageResolver;

    @Getter
    @Setter
    private MessageInterpolator messageInterpolator;


    private String getMessage(String key) {
        return getMessage(key, List.of());
    }

    private String getMessage(String key, Collection<? extends Variable> variables) {
        return getMessageFromTemplate(getTemplate(key), variables);
    }

    private String getMessageFromTemplate(String template, Collection<? extends Variable> variables) {
        variables = appendComposer(variables);
        return getMessageInterpolator().interpolate(template, variables);
    }

    private String getTemplate(String key) {
        return getMessageResolver().getMessage(key);
    }


    private Collection<? extends Variable> appendComposer(Collection<? extends Variable> variables) {
        return Stream.of(
                variables,
                Variable.newVariables("composer", this, MessageComposer.class)
            ).flatMap(Collection::stream)
            .collect(Collectors.toList());
    }


    // region: App messages
    public String getProperty(String key) {
        return getMessage(String.format("config.%s.property", key));
    }

    public String getPropertyMissing(String property) {
        return getMessage("config.violation.missing", Variable.newStringVariables("property", property));
    }

    public String getPropertyEmpty(String property) {
        return getMessage("config.violation.empty", Variable.newStringVariables("property", property));
    }

    public String getPatientZeroCountGTAgentCount() {
        return getMessage("config.violation.patientZeroCountGTAgentCount");
    }

    public String getAverageDegreeGEAgentCount() {
        return getMessage("config.violation.averageDegreeGEAgentCount");
    }

    public String getPropertyOutOfRange(String property, int value, int min, int max) {
        return getMessage("config.violation.outOfRange", Variable.combineVariables(
            Variable.newStringVariables("property", property),
            Variable.newIntegerVariables("value", value, "min", min, "max", max)
        ));
    }

    public String getPropertyOutOfRange(String property, double value, double min, double max) {
        return getMessage("config.violation.outOfRange", Variable.combineVariables(
            Variable.newStringVariables("property", property),
            Variable.newDoubleVariables("value", value, "min", min, "max", max)
        ));
    }

    public String getPropertyNegative(String property, int value) {
        return getMessage("config.violation.negative", Variable.combineVariables(
            Variable.newStringVariables("property", property),
            Variable.newIntegerVariables("value", value)
        ));
    }


    public String getAgentType(String agentType) {
        return getMessage(String.format("simulation.agent.%s", agentType));
    }


    public String getReportConfigHeader() {
        return getMessage("report.config.header");
    }

    public String getReportAgentsHeader() {
        return getMessage("report.agents.header");
    }

    public String getReportGraphHeader() {
        return getMessage("report.graph.header");
    }

    public String getReportPhaseSummariesHeader() {
        return getMessage("report.phaseSummaries.header");
    }


    public String getMissingConfigFile(String fileName) {
        return getMessage("exception.config.missingFile", Variable.newStringVariables("fileName", fileName));
    }

    public String getCannotAccessReportFile(String fileName) {
        return getMessage("exception.report.cannotAccessReportFile", Variable.newStringVariables("fileName", fileName));
    }
    // endregion
}
