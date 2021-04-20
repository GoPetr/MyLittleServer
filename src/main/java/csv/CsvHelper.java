package csv;

import demoBufferedReader.StdBufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class CsvHelper {

  public static Csv parseFile(Reader readerFile) {
    return parseFile(readerFile, false, ',');
  }

  public static Csv parseFile(Reader reader, boolean withHeader, char delimiter) {
    ArrayList<String[]> list = new ArrayList<>();
    String[] header = null;
    try (StdBufferedReader stdBufferedReader = new StdBufferedReader(reader)) {

      while (stdBufferedReader.hasNext()) {
        String str = new String(stdBufferedReader.readLine());

        if (str.trim().isEmpty()) {
          continue;
        }

        if (withHeader) {
          header = str.split(Character.toString(delimiter));
          withHeader = false;
          continue;
        }
        list.add(str.split(Character.toString(delimiter)));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    String[][] values = new String[list.size()][];
    for (int i = 0; i < list.size(); i++) {
      values[i] = list.get(i);
    }

    return new Csv(header, values);
  }

  public static void writeCsv(Writer writer, Csv csv, char delimiter) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (writer) {
      if (csv.withHeader()) {
        String[] arrHeadr = csv.header();

        for (int i = 0; i < arrHeadr.length; i++) {
          stringBuilder.append(arrHeadr[i]);
          if (i != arrHeadr.length - 1) {
            stringBuilder.append(delimiter);
          }
        }
        stringBuilder.append('\n');
      }

      for (String[] pointer : csv.values()) {
        for (int i = 0; i < pointer.length; i++) {
          stringBuilder.append(pointer[i]);
          if (i != pointer.length - 1) {
            stringBuilder.append(delimiter);
          }
        }
        stringBuilder.append('\n');
      }
      writer.write(stringBuilder.toString());
    }
  }
}
