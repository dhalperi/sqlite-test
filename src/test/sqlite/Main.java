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
    SQLiteConnection conn = new SQLiteConnection(tempFile.toFile());
    conn.open();

    conn.dispose();
    System.exit(0);
  }

  /** Utility class, disable construction. */
  private Main() {
  }
}
