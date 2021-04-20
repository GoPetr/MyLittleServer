package demoDBService;

public record Selector(String fileName, String value) {

  public static class Builder {
    String fileName;
    String value;

    public Builder fieldName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder value(String value) {
      this.value = value;
      return this;
    }

    public Selector build() {
      return new Selector(this.fileName, this.value);
    }
  }
}
