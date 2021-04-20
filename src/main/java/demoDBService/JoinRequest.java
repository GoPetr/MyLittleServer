package demoDBService;



import csv.Csv;
import java.util.*;

public class JoinRequest extends AbstractRequest<Csv> {
  private Csv on;
  private String by;

  private JoinRequest(Csv from, Csv on, String by) {
    super(from);
    Objects.requireNonNull(on);
    Objects.requireNonNull(by);
    this.on = on;
    this.by = by;
  }

  @Override
  protected Csv execute() throws RequestException {
    if (!this.on.withHeader() || !this.target.withHeader()
            || this.target.values().length != this.on.values().length) {
      throw new RequestException();
    }

    int indexOfOnCsvHeader = -1;
    String[] headerOfOnCsv = this.on.header();
    String[][] valuesOfTarget = this.target.values();
    String[][] valuesOfOnCsv = this.on.values();

    List<String> header = new ArrayList<>(Arrays.asList(this.target.header()));

    for (int i = 0; i < headerOfOnCsv.length; i++) {
      if (headerOfOnCsv[i].equals(this.by)) {
        indexOfOnCsvHeader = i;
        continue;
      }

      header.add(headerOfOnCsv[i]);
    }

    List<String> listForEveryString = new ArrayList<>();
    String[][] values = new String[this.target.values().length][header.size()];

    for (int i = 0; i < values.length; i++) {
      Collections.addAll(listForEveryString, valuesOfTarget[i]);

      for (int j = 0; j < valuesOfOnCsv[i].length; j++) {
        if (j == indexOfOnCsvHeader) {
          continue;
        }

        listForEveryString.add(valuesOfOnCsv[i][j]);
      }

      values[i] = listForEveryString.toArray(new String[0]);
    }

    return new Csv.Builder().header(header.toArray(new String[0])).values(values).build();
  }

  public static class Builder {
    private Csv from;
    private Csv on;
    private String by;

    public Builder from(Csv from) {
      this.from = from;
      return this;
    }

    public Builder on(Csv on) {
      this.on = on;
      return this;
    }

    public Builder by(String by) {
      this.by = by;
      return this;
    }

    public JoinRequest build() {
      return new JoinRequest(this.from, this.on, this.by);
    }
  }
}
