package plague_simulator.config;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import plague_simulator.config.Config;
import plague_simulator.config.ConfigValidator;
import plague_simulator.global.Global;
import plague_simulator.global.MessageComposer;

final class ConfigValidatorImpl implements ConfigValidator {
    private final static int AGENT_COUNT_MIN = 1;
    private final static int AGENT_COUNT_MAX = 1000000;

    private final static int SIMULATION_DURATION_MIN = 1;
    private final static int SIMULATION_DURATION_MAX = 1000000;


    @Override
    public List<String> validateConfig(Config c) {
        List<String> e = new ArrayList<>();
        MessageComposer m = Global.getMessageComposerInstance();

        // seed
        try {
            if      (c.getSeed() == null)       { e.add(m.getPropertyMissing(c.SEED())); }
            else if (c.getSeed().length() <= 0) { e.add(m.getPropertyEmpty(c.SEED())); }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.SEED()));
        }

        // agentCount
        try {
            if (AGENT_COUNT_MIN > c.getAgentCount() || c.getAgentCount() > AGENT_COUNT_MAX) {
                e.add(m.getPropertyOutOfRange(c.AGENT_COUNT(), c.getAgentCount(), AGENT_COUNT_MIN, AGENT_COUNT_MAX));
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.AGENT_COUNT()));
        }

        // patientZeroCount
        try {
            if (c.getPatientZeroCount() < 0) { e.add(m.getPropertyNegative(c.PATIENT_ZERO_COUNT(), c.getPatientZeroCount())); }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.PATIENT_ZERO_COUNT()));
        }

        // socialAgentProbability
        try {
            if (0.0 > c.getSocialAgentProbability() || c.getSocialAgentProbability() > 1.0) {
                e.add(m.getPropertyOutOfRange(c.SOCIAL_AGENT_PROBABILITY(), c.getSocialAgentProbability(), 0.0, 1.0));
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.SOCIAL_AGENT_PROBABILITY()));
        }

        // meetingProbability
        try {
            if (0.0 > c.getMeetingProbability() || c.getMeetingProbability() >= 1.0) {
                e.add(m.getPropertyOutOfRange(c.MEETING_PROBABILITY(), c.getMeetingProbability(), 0.0, 1.0));
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.MEETING_PROBABILITY()));
        }

        // meetingLimit
        try {
            c.getMeetingLimit();
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.MEETING_LIMIT()));
        }

        // infectivity
        try {
            if (0.0 > c.getInfectivity() || c.getInfectivity() > 1.0) {
                e.add(m.getPropertyOutOfRange(c.INFECTIVITY(), c.getInfectivity(), 0.0, 1.0));
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.INFECTIVITY()));
        }

        // recoveryProbability
        try {
            if (0.0 > c.getRecoveryProbability() || c.getRecoveryProbability() > 1.0) {
                e.add(m.getPropertyOutOfRange(c.RECOVERY_PROBABILITY(), c.getRecoveryProbability(), 0.0, 1.0));
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.RECOVERY_PROBABILITY()));
        }

        // immunityProbability
        try {
            if (0.0 > c.getImmunityProbability() || c.getImmunityProbability() > 1.0) {
                e.add(m.getPropertyOutOfRange(c.IMMUNITY_PROBABILITY(), c.getImmunityProbability(), 0.0, 1.0));
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.IMMUNITY_PROBABILITY()));
        }

        // lethality
        try {
            if (0.0 > c.getLethality() || c.getLethality() > 1.0) {
                e.add(m.getPropertyOutOfRange(c.LETHALITY(), c.getLethality(), 0.0, 1.0));
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.LETHALITY()));
        }

        // simulationDuration
        try {
            if (SIMULATION_DURATION_MIN > c.getSimulationDuration() || c.getSimulationDuration() > SIMULATION_DURATION_MAX) {
                e.add(m.getPropertyOutOfRange(
                    c.SIMULATION_DURATION(), c.getSimulationDuration(), SIMULATION_DURATION_MIN, SIMULATION_DURATION_MAX)
                );
            }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.SIMULATION_DURATION()));
        }

        // averageDegree
        try {
            if (c.getAverageDegree() < 0) { e.add(m.getPropertyNegative(c.AVERAGE_DEGREE(), c.getAverageDegree())); }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.AVERAGE_DEGREE()));
        }

        // reportFilePath
        try {
            if (c.getReportFilePath() == null) { e.add(m.getPropertyMissing(c.REPORT_FILE_PATH())); }
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.REPORT_FILE_PATH()));
        }

        // reportFileOverwrite
        try {
            c.getReportFileOverwrite();
        } catch (NoSuchElementException err) {
            e.add(m.getPropertyMissing(c.REPORT_FILE_OVERWRITE()));
        }


        // special checks
        try {
            if (c.getPatientZeroCount() > c.getAgentCount()) { e.add(m.getPatientZeroCountGTAgentCount()); }
        } catch (NoSuchElementException err) {
            // Already handled.
        }

        try {
            if (c.getAverageDegree() >= c.getAgentCount()) { e.add(m.getAverageDegreeGEAgentCount()); }
        } catch (NoSuchElementException err) {
            // Already handled.
        }


        return e;
    } // validate
} // ConfigValidatorImpl
