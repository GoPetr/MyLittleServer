package demoBufferedReader;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

public class StdBufferedReader implements Closeable {
  Reader myReader;
  char[] buffer;
  int bufferCursor = 0;
  int charCount;
  int bufferCurrentLen;
  boolean lastN = false;

  public StdBufferedReader(Reader reader, int bufferSize) {
    if (reader == null) {
      throw new NullPointerException("Reader is null!");
    }
    if (bufferSize <= 0) {
      throw new IllegalArgumentException("Invalid buffer size!");
    }
    myReader = reader;
    buffer = new char[bufferSize];
  }

  public StdBufferedReader(Reader reader) {
    this(reader, 7);
  }

  public boolean hasNext() throws IOException {
    if (bufferCursor >= charCount) {
      fill();
    }
    return bufferCursor < charCount || lastN;
  }

  public char[] readLine() throws IOException {

    // Empty line case
    char[] outArray = null;
    int startChar; // place of new line in buffer

    while (true) {
      if (bufferCursor >= charCount) {
        fill();
      }
      if (bufferCursor >= charCount) {
        lastN = false;
        if (outArray != null && outArray.length > 0) {
          return outArray;
        } else {
          return new char[]{};
        }
      }

      //Check if \n or \r are in buffer
      boolean eol = false;
      int i;

      for (i = bufferCursor; i < charCount; i++) {
        char c = buffer[i];
        if ((c == '\n') || (c == '\r')) {
          eol = true;
          break;
        }
      }

      startChar = bufferCursor;
      bufferCursor = i;

      //if end of line is in buffer
      if (eol) {
        lastN = true;
        if (outArray == null) {
          outArray = new char[i - startChar];
          System.arraycopy(buffer, startChar, outArray, 0, i - startChar);
        } else {
          int newLength = outArray.length + (i - startChar);
          char[] tmp = new char[newLength];
          System.arraycopy(outArray, 0, tmp, 0, outArray.length);
          System.arraycopy(buffer, startChar, tmp, outArray.length, i - startChar);
          outArray = tmp.clone();
        }

        bufferCursor++;

        return outArray;
      }

      //If end of line not found
      if (outArray == null) {
        outArray = new char[i - startChar];
        System.arraycopy(buffer, startChar, outArray, 0, i - startChar);
      } else {
        char[] tmp = new char[outArray.length + (i - startChar)];
        System.arraycopy(outArray, 0, tmp, 0, outArray.length);
        System.arraycopy(buffer, startChar, tmp, outArray.length, i - startChar);
        outArray = tmp.clone();
      }
    }
  }

  private void fill() throws IOException {

    int indexOfRead = myReader.read(buffer, 0, buffer.length);
    bufferCurrentLen = indexOfRead; // for the garbage in the end buffer.

    if (indexOfRead > 0) {
      charCount = indexOfRead;
      bufferCursor = 0;
    }
  }

  public void close() throws IOException {
    if (myReader != null) {
      myReader.close();
    }
  }
}

