package demoDBService;



import csv.Csv;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InsertRequest extends AbstractRequest<Csv> {

  private final String[] line;

  private InsertRequest(Csv target, String[] line) {
    super(target);

    Objects.requireNonNull(line);
    this.line = line;
  }

  @Override
  protected Csv execute() throws RequestException {
    List<String[]> valuesAsList = new ArrayList<>(Arrays.asList(this.target.values()));
    valuesAsList.add(this.line);

    String[][] values = new String[valuesAsList.size()][];

    for (int i = 0; i < values.length; i++) {
      values[i] = valuesAsList.get(i);
    }

    return new Csv.Builder().header(this.target.header()).values(values).build();
  }

  public static class Builder {
    String[] line;
    Csv csv;

    public Builder insert(String[] line) {
      this.line = line;
      return this;
    }

    public Builder to(Csv csv) {
      this.csv = csv;
      return this;
    }

    public InsertRequest build() {
      return new InsertRequest(this.csv, this.line);
    }
  }
}
