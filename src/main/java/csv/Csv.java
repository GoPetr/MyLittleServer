package csv;

import java.util.Arrays;
import java.util.Objects;

public record Csv(String[] header, String[][] values) {

  public static class Builder {
    private String[] header;
    private String[][] values;

    public Builder header(String[] header) {
      this.header = header;
      return this;
    }

    public Builder values(String[][] values) {
      this.values = values;
      return this;
    }

    public Csv build() {
      return new Csv(header, values);
    }
  }

  public boolean withHeader() {
    return header != null && header.length > 0;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null || o.getClass() != this.getClass()) {
      return false;
    }

    Csv csv = (Csv) o;
    if (!Arrays.deepEquals(csv.header, this.header)) {
      return false;
    }

    return Arrays.deepEquals(csv.values, this.values);
  }

  public int hashCode() {
    return Objects.hashCode(this);
  }

  public String toString() {
    return "Csv{"
            + "header=" + Arrays.toString(header)
            + ", values=" + Arrays.toString(values)
            + '}';
  }
}