package namantam1.utils;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import static namantam1.utils.JsonUtil.mapper;

public class JsonSerde<T> implements Serde<T> {
  private final Class<T> type;

  public JsonSerde(Class<T> type) {
    this.type = type;
  }

  @SneakyThrows
  public static <T> byte[] toJsonBytes(T data) {
    return mapper.writeValueAsBytes(data);
  }

  @SneakyThrows
  public static <T> T parseJson(byte[] data, Class<T> type) {
    return mapper.readValue(data, type);
  }

  @Override
  public Serializer<T> serializer() {
    return (topic, data) -> toJsonBytes(data);
  }

  @Override
  public Deserializer<T> deserializer() {
    return (topic, data) -> parseJson(data, type);
  }
}
