package demoDBService;


import csv.Csv;
import java.util.ArrayList;


public class DeleteRequest extends AbstractRequest<Csv> {
  Csv target;
  Selector selector;

  private DeleteRequest(Csv target, Selector whereSelector) {
    super(target);
    this.target = target;
    selector = whereSelector;
  }

  @Override
  protected Csv execute() throws RequestException {
    Csv newCsv = null;
    ArrayList<String[]> list = new ArrayList<>();

    int i;

    for (i = 0; i < target.header().length; i++) {
      if (target.header()[i].equals(selector.fileName())) {
        break;
      }
    }

    for (String[] row : target.values()) {
      for (int j = 0; j < row.length; j++) {
        if (j == i) {
          if (!row[j].equals(selector.value())) {
            list.add(row);
          }
        }
      }
    }

    String[][] val = new String[list.size()][];
    for (int j = 0; j < list.size(); j++) {
      val[j] = list.get(j);
    }

    return new Csv(target.header(), val);
  }

  public static class Builder {
    Selector selector;
    Csv csv;

    public Builder where(Selector selector) {
      this.selector = selector;
      return this;
    }

    public Builder from(Csv csv) {
      this.csv = csv;
      return this;
    }

    public DeleteRequest build() {
      return new DeleteRequest(csv, selector);
    }
  }
}
