package namantam1.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtil {
  public static ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  public static JsonNode parseJson(String jsonString) {
    return mapper.readTree(jsonString);
  }

  @SneakyThrows
  public static String toJson(JsonNode jsonNode) {
    return mapper.writeValueAsString(jsonNode);
  }

  @SneakyThrows
  public static byte[] toJsonBytes(JsonNode jsonNode) {
    return mapper.writeValueAsBytes(jsonNode);
  }
}
