package demoDBService;


import csv.Csv;
import java.util.Objects;

public abstract class AbstractRequest<T> {
  protected final Csv target;

  protected AbstractRequest(Csv target) {
    Objects.nonNull(target);
    this.target = target;
  }

  protected abstract T execute() throws RequestException;

}