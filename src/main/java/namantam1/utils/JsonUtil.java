package namantam1.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtil {
  public static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @SneakyThrows
  public static JsonNode parseJson(String jsonString) {
    return mapper.readTree(jsonString);
  }

  @SneakyThrows
  public static String toJsonString(JsonNode jsonNode) {
    return mapper.writeValueAsString(jsonNode);
  }

  @SneakyThrows
  public static byte[] toJsonBytes(JsonNode jsonNode) {
    return mapper.writeValueAsBytes(jsonNode);
  }
}