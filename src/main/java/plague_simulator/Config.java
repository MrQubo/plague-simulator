package plague_simulator;

import java.lang.reflect.Field;
import java.nio.file.Path;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import plague_simulator.validator.Max;
import plague_simulator.validator.Min;
import plague_simulator.validator.NotBlank;
import plague_simulator.validator.ValidateConfig;

// Should be instantiated only by jackson-databind module.
@Data
@NoArgsConstructor(access = AccessLevel.NONE)
@AllArgsConstructor(access = AccessLevel.NONE)
@ValidateConfig
public class Config {
  public interface NotNullChecks { }


  @JsonProperty("seed")
  @NotNull(groups = NotNullChecks.class)
  @NotBlank
  private String seed = null;

  @JsonProperty("liczbaAgentów")
  @NotNull(groups = NotNullChecks.class)
  @Min(1)
  @Max(1000000)
  private Integer agentCount = null;

  @JsonProperty("liczbaZarażonych")
  @NotNull(groups = NotNullChecks.class)
  @Min(0)
  private Integer patientZeroCount = null;

  @JsonProperty("prawdTowarzyski")
  @NotNull(groups = NotNullChecks.class)
  @Min(0.0)
  @Max(1.0)
  private Double socialAgentProbability = null;

  @JsonProperty("prawdSpotkania")
  @NotNull(groups = NotNullChecks.class)
  @Min(0.0)
  @Max(value = 1.0, inclusive = false)
  private Double meetingProbability = null;

  @JsonProperty("maksSpotkania")
  @NotNull(groups = NotNullChecks.class)
  private Integer meetingLimit = null;

  @JsonProperty("prawdZarażenia")
  @NotNull(groups = NotNullChecks.class)
  @Min(0.0)
  @Max(1.0)
  private Double infectivity = null;

  @JsonProperty("prawdWyzdrowienia")
  @NotNull(groups = NotNullChecks.class)
  @Min(0.0)
  @Max(1.0)
  private Double recoveryProbability = null;

  @JsonProperty("prawdOdporność")
  @NotNull(groups = NotNullChecks.class)
  @Min(0.0)
  @Max(1.0)
  private Double immunityProbability = null;

  @JsonProperty("śmiertelność")
  @NotNull(groups = NotNullChecks.class)
  @Min(0.0)
  @Max(1.0)
  private Double lethality = null;

  @JsonProperty("liczbaDni")
  @NotNull(groups = NotNullChecks.class)
  @Min(1)
  @Max(100000)
  private Integer simulationDuration = null;

  @JsonProperty("śrZnajomych")
  @NotNull(groups = NotNullChecks.class)
  @Min(0)
  private Integer averageDegree = null;

  @JsonProperty("plikZRaportem")
  @NotNull(groups = NotNullChecks.class)
  private Path reportFilePath = null;

  @JsonProperty("nadpiszPlikZRaportem")
  @NotNull(groups = NotNullChecks.class)
  private Boolean reportFileOverwrite = null;


  static public String getPropertyName(String fieldName) throws NoSuchFieldException {
    return getPropertyName(Config.class.getDeclaredField(fieldName));
  }
  static private String getPropertyName(final Field field) {
    final var annotation = field.getAnnotation(JsonProperty.class);
    if (annotation == null) { return null; }

    return annotation.value();
  }

  static public Double getMin(String fieldName) throws NoSuchFieldException {
    return getMin(Config.class.getDeclaredField(fieldName));
  }
  static private Double getMin(final Field field) {
    final var annotation = field.getAnnotation(Min.class);
    if (annotation == null) { return null; }

    return annotation.value();
  }

  static public Double getMax(String fieldName) throws NoSuchFieldException {
    return getMax(Config.class.getDeclaredField(fieldName));
  }
  static private Double getMax(final Field field) {
    final var annotation = field.getAnnotation(Max.class);
    if (annotation == null) { return null; }

    return annotation.value();
  }
}
