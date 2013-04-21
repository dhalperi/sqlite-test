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
    conn.exec("CREATE TABLE configuration (\n" + "    key STRING UNIQUE NOT NULL,\n"
        + "    value STRING NOT NULL);");
    conn.exec("CREATE TABLE workers (\n" + "    worker_id INTEGER PRIMARY KEY ASC,\n"
        + "    host_port STRING NOT NULL);");
    conn.exec("CREATE TABLE alive_workers (\n"
        + "    worker_id INTEGER PRIMARY KEY ASC REFERENCES workers(worker_id));");
    conn.exec("CREATE TABLE masters (\n" + "    master_id INTEGER PRIMARY KEY ASC,\n"
        + "    host_port STRING NOT NULL);");
    conn.exec("CREATE TABLE relations (\n" + "    user_name STRING NOT NULL,\n"
        + "    program_name STRING NOT NULL,\n" + "    relation_name STRING NOT NULL,\n"
        + "    PRIMARY KEY (user_name,program_name,relation_name));");
    conn.exec("CREATE TABLE relation_schema (\n" + "    user_name STRING NOT NULL,\n"
        + "    program_name STRING NOT NULL,\n" + "    relation_name STRING NOT NULL,\n"
        + "    col_index INTEGER NOT NULL,\n" + "    col_name STRING,\n"
        + "    col_type STRING NOT NULL,\n"
        + "    FOREIGN KEY (user_name,program_name,relation_name) REFERENCES relations);");
    conn.exec("CREATE TABLE stored_relations (\n"
        + "    stored_relation_id INTEGER PRIMARY KEY ASC,\n" + "    user_name STRING NOT NULL,\n"
        + "    program_name STRING NOT NULL,\n" + "    relation_name STRING NOT NULL,\n"
        + "    num_shards INTEGER NOT NULL,\n" + "    how_partitioned STRING NOT NULL,\n"
        + "    FOREIGN KEY (user_name,program_name,relation_name) REFERENCES relations);");
    conn.exec("CREATE TABLE shards (\n"
        + "    stored_relation_id INTEGER NOT NULL REFERENCES stored_relations(stored_relation_id),\n"
        + "    shard_index INTEGER NOT NULL,\n"
        + "    worker_id INTEGER NOT NULL REFERENCES workers(worker_id));");
    conn.exec("CREATE TABLE queries (\n" + "    query_id INTEGER NOT NULL PRIMARY KEY ASC,\n"
        + "    raw_query TEXT NOT NULL,\n" + "    logical_ra TEXT NOT NULL);");
    conn.dispose();
    System.err.println(prettyNanoTime(System.nanoTime() - start));

    System.exit(0);
  }

  /** Utility class, disable construction. */
  private Main() {
  }
}
