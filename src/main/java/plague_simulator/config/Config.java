package plague_simulator.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import plague_simulator.global.MessageComposer;

// shouldn't import Global

@Slf4j
@Accessors(fluent = true, chain = false)
@Getter
public class Config implements Cloneable {
    static private final String SEED_KEY                     = "seed";
    static private final String AGENT_COUNT_KEY              = "agentCount";
    static private final String PATIENT_ZERO_COUNT_KEY       = "patientZeroCount";
    static private final String SOCIAL_AGENT_PROBABILITY_KEY = "socialAgentProbability";
    static private final String MEETING_PROBABILITY_KEY      = "meetingProbability";
    static private final String MEETING_LIMIT_KEY            = "meetingLimit";
    static private final String INFECTIVITY_KEY              = "infectivity";
    static private final String RECOVERY_PROBABILITY_KEY     = "recoveryProbability";
    static private final String IMMUNITY_PROBABILITY_KEY     = "immunityProbability";
    static private final String LETHALITY_KEY                = "lethality";
    static private final String SIMULATION_DURATION_KEY      = "simulationDuration";
    static private final String AVERAGE_DEGREE_KEY           = "averageDegree";
    static private final String REPORT_FILE_PATH_KEY         = "reportFilePath";
    static private final String REPORT_FILE_OVERWRITE_KEY    = "reportFileOverwrite";

    private String SEED                     = SEED_KEY;
    private String AGENT_COUNT              = AGENT_COUNT_KEY;
    private String PATIENT_ZERO_COUNT       = PATIENT_ZERO_COUNT_KEY;
    private String SOCIAL_AGENT_PROBABILITY = SOCIAL_AGENT_PROBABILITY_KEY;
    private String MEETING_PROBABILITY      = MEETING_PROBABILITY_KEY;
    private String MEETING_LIMIT            = MEETING_LIMIT_KEY;
    private String INFECTIVITY              = INFECTIVITY_KEY;
    private String RECOVERY_PROBABILITY     = RECOVERY_PROBABILITY_KEY;
    private String IMMUNITY_PROBABILITY     = IMMUNITY_PROBABILITY_KEY;
    private String LETHALITY                = LETHALITY_KEY;
    private String SIMULATION_DURATION      = SIMULATION_DURATION_KEY;
    private String AVERAGE_DEGREE           = AVERAGE_DEGREE_KEY;
    private String REPORT_FILE_PATH         = REPORT_FILE_PATH_KEY;
    private String REPORT_FILE_OVERWRITE    = REPORT_FILE_OVERWRITE_KEY;

    static public final Set<String> KEYS =
        Set.of(
            SEED_KEY,
            AGENT_COUNT_KEY,
            PATIENT_ZERO_COUNT_KEY,
            SOCIAL_AGENT_PROBABILITY_KEY,
            MEETING_PROBABILITY_KEY,
            MEETING_LIMIT_KEY,
            INFECTIVITY_KEY,
            RECOVERY_PROBABILITY_KEY,
            IMMUNITY_PROBABILITY_KEY,
            LETHALITY_KEY,
            SIMULATION_DURATION_KEY,
            AVERAGE_DEGREE_KEY,
            REPORT_FILE_PATH_KEY,
            REPORT_FILE_OVERWRITE_KEY
        );


    static public Config createEmptyConfig() {
        return new Config();
    }


    private Config() {
        compositeConfiguration = new CompositeConfiguration();
        compositeConfiguration.setThrowExceptionOnMissing(true);
    }


    @Getter
    private CompositeConfiguration compositeConfiguration;


    @Override
    public Config clone() {
        Config newConfig = this;
        try {
            newConfig = (Config)super.clone();
        } catch (CloneNotSupportedException e) { LOG.error("That shouldn't happen.", e); }

        newConfig.compositeConfiguration = (CompositeConfiguration)compositeConfiguration.clone();
        return newConfig;
    }


    public void initFieldNames(MessageComposer m) {
        try {
            for (Field field : Config.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) { continue; }
                if (field.getType() != String.class)         { continue; }

                String key = (String)field.get(this);
                if (KEYS.contains(key)) {
                    field.set(this, m.getProperty(key));
                }
            }
        } catch (IllegalAccessException e) { LOG.error("That shouldn't happen.", e); }
    }


    public void prependConfiguration(Configuration configuration) {
        compositeConfiguration = new CompositeConfiguration(List.of(configuration, compositeConfiguration));
        compositeConfiguration.setThrowExceptionOnMissing(true);
    }


    // region: CompositeConfiguration wrapper
    public void addConfiguration(Configuration configuration) {
        compositeConfiguration.addConfiguration(configuration);
    }

    public <T> T get(Class<? extends T> clazz, String key) {
        return compositeConfiguration.get(clazz, key);
    }

    public <T> T get(Class<T> clazz, String key, T defaultValue) {
        return compositeConfiguration.get(clazz, key, defaultValue);
    }
    // endregion

    // region: named key getters
    public String getSeed() {
        return get(String.class, SEED());
    }
    public int getAgentCount() {
        return get(Integer.class, AGENT_COUNT());
    }
    public int getPatientZeroCount() {
        return get(Integer.class, PATIENT_ZERO_COUNT());
    }
    public double getSocialAgentProbability() {
        return get(Double.class, SOCIAL_AGENT_PROBABILITY());
    }
    public double getMeetingProbability() {
        return get(Double.class, MEETING_PROBABILITY());
    }
    public int getMeetingLimit() {
        return get(Integer.class, MEETING_LIMIT());
    }
    public double getInfectivity() {
        return get(Double.class, INFECTIVITY());
    }
    public double getRecoveryProbability() {
        return get(Double.class, RECOVERY_PROBABILITY());
    }
    public double getImmunityProbability() {
        return get(Double.class, IMMUNITY_PROBABILITY());
    }
    public double getLethality() {
        return get(Double.class, LETHALITY());
    }
    public int getSimulationDuration() {
        return get(Integer.class, SIMULATION_DURATION());
    }
    public int getAverageDegree() {
        return get(Integer.class, AVERAGE_DEGREE());
    }
    public String getReportFilePath() {
        return get(String.class, REPORT_FILE_PATH());
    }
    public boolean getReportFileOverwrite() {
        return get(Boolean.class, REPORT_FILE_OVERWRITE());
    }
    // endregion
}
