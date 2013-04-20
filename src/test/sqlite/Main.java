package test.sqlite;

import java.nio.file.Files;
import java.nio.file.Path;

import com.almworks.sqlite4java.SQLiteConnection;

/**
 * This class is intended to profile the execution time of sqlite4java on various systems.
 * 
 * @author dhalperi
 * 
 */
public final class Main {
  public static String prettyNanoTime(long nanoTime) {
    StringBuilder sb = new StringBuilder();
    /* Start in hours */;
    long current = 1000L * 1000L * 1000L * 60L * 60L;
    if (nanoTime >= current) {
      long hours = nanoTime / (current);
      sb.append(hours).append(" hours ");
      nanoTime %= current;
    }
    /* Minutes */
    current = current / 60L;
    if (nanoTime >= current) {
      long minutes = nanoTime / (current);
      sb.append(minutes).append(" minutes ");
      nanoTime %= current;
    }
    /* Seconds */
    current = current / 60L;
    if (nanoTime >= current) {
      long seconds = nanoTime / (current);
      sb.append(seconds).append(" seconds ");
      nanoTime %= current;
    }
    /* Milliseconds */
    current = current / 1000L;
    if (nanoTime >= current) {
      long msecs = nanoTime / (current);
      sb.append(msecs).append(" ms ");
      nanoTime %= current;
    }
    /* Microseconds */
    current = current / 1000L;
    if (nanoTime >= current) {
      long usecs = nanoTime / (current);
      sb.append(usecs).append(" us ");
      nanoTime %= current;
    }
    if (nanoTime != 0) {
      sb.append(nanoTime).append(" ns");
    }
    return sb.toString();
  }

  /**
   * Run the main program.
   * 
   * @param args The command-line arguments.
   * @throws Exception if something goes wrong
   */
  public static void main(final String[] args) throws Exception {
    System.err.println("In main!");

    Path tempFile = Files.createTempFile("temp", "db");
    tempFile.toFile().deleteOnExit();

    long start = System.nanoTime();
    SQLiteConnection conn = new SQLiteConnection(tempFile.toFile());
    conn.open();
    conn.dispose();
    System.err.println(prettyNanoTime(System.nanoTime() - start));

    System.exit(0);
  }

  /** Utility class, disable construction. */
  private Main() {
  }
}
