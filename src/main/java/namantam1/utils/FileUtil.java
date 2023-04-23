package namantam1.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.util.List;

import static namantam1.utils.JsonUtil.mapper;

public class FileUtil {
  @SneakyThrows
  public static <T> List<T> readJsonAsList(String path, Class<T> type) {
    File fp = new File(path);
    return mapper.readerForListOf(type).readValue(fp);
  }

  @SneakyThrows
  public static <T> T readJson(String path, Class<T> type) {
    File fp = new File(path);
    return mapper.readValue(fp, type);
  }
}