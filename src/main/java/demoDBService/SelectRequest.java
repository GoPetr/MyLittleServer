package demoDBService;


import csv.Csv;

import java.util.ArrayList;
import java.util.Objects;


public class SelectRequest extends AbstractRequest<String[][]> {
  Csv target;
  Selector selector;
  String[] columns;

  private SelectRequest(Csv target, Selector selector, String[] columns) {
    super(target);
    this.target = target;
    this.selector = selector;
    this.columns = columns;
  }

  @Override
  public String[][] execute() throws RequestException {
    ArrayList<String[]> list = new ArrayList<>();
    int[] selectColumn = new int[columns.length];
    int whereSelect = -1;

    for (int i = 0; i < target.header().length; i++) {
      if (selector != null && target.header()[i].equals(selector.fileName())) {
        whereSelect = i;
      }

      for (int j = 0; j < columns.length; j++) {
        if (target.header()[i].equals(columns[j])) {
          selectColumn[j] = i;
        }
      }
    }

    for (String[] row : target.values()) {
      if (whereSelect != -1) {
        for (int j = 0; j < row.length; j++) {
          if (j == whereSelect) {
            if (row[j].equals(selector.value())) {
              //взять из row только индексы из selectColumn
              String[] tmp = new String[selectColumn.length];
              for (int i = 0; i < selectColumn.length; i++) {
                tmp[i] = row[selectColumn[i]];
              }

              list.add(tmp);
            }
          }
        }
      } else {
        String[] tmp = new String[selectColumn.length];
        for (int i = 0; i < selectColumn.length; i++) {
          tmp[i] = row[selectColumn[i]];
        }

        list.add(tmp);
      }
    }

    String[][] val = new String[list.size()][];
    for (int j = 0; j < list.size(); j++) {
      val[j] = list.get(j);
    }

    return val;
  }

  public static class Builder {
    Csv target;
    Selector selector;
    String[] columns;

    public Builder where(Selector selector) {
      this.selector = selector;
      return this;
    }

    public Builder select(String[] columns) {
      this.columns = columns;
      return this;
    }

    public Builder from(Csv csv) {
      this.target = csv;
      return this;
    }

    public SelectRequest build() {
      Objects.nonNull(target);
      return new SelectRequest(target, selector, columns);
    }
  }
}
