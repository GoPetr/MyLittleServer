package demoDBService;


import csv.Csv;

import java.util.Objects;

public class UpdateRequest extends AbstractRequest<Csv> {
  private Selector whereSelector;
  private Selector updateToSelector;

  private UpdateRequest(Csv target, Selector whereSelector, Selector updateToSelector) {
    super(target);
    Objects.requireNonNull(whereSelector);
    Objects.requireNonNull(updateToSelector);
    this.whereSelector = whereSelector;
    this.updateToSelector = updateToSelector;
  }

  @Override
  protected Csv execute() throws RequestException {
    int indexOfWhere = -1;
    int indexOfUpdate = -1;
    String[] headerOfTarget = this.target.header();
    String[][] valuesOfTarget = this.target.values();

    for (int i = 0; i < headerOfTarget.length; i++) {
      if (headerOfTarget[i].equals(whereSelector.fileName())) {
        indexOfWhere = i;
      }
      if (headerOfTarget[i].equals(updateToSelector.fileName())) {
        indexOfUpdate = i;
      }
    }

    for (int i = 0; i < valuesOfTarget.length; i++) {
      for (int j = 0; j < valuesOfTarget[i].length; j++) {
        if (j == indexOfWhere && valuesOfTarget[i][j].equals(whereSelector.value())) {
          valuesOfTarget[i][indexOfUpdate] = updateToSelector.value();
        }
      }
    }

    return new Csv.Builder().header(headerOfTarget).values(valuesOfTarget).build();
  }

  public static class Builder {
    private Selector selector;
    private Selector updateSelector;
    private Csv csv;

    public Builder where(Selector selector) {
      this.selector = selector;
      return this;
    }

    public Builder update(Selector updateSelector) {
      this.updateSelector = updateSelector;
      return this;
    }

    public Builder from(Csv csv) {
      this.csv = csv;
      return this;
    }

    public UpdateRequest build() {
      return new UpdateRequest(this.csv, this.selector, this.updateSelector);
    }
  }
}
