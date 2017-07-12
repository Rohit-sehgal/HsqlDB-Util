import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by rohitsehgal on 12/7/17.
 */
public class DBManagerTest {

    @Test
    public void dbTest() {

        DBManager db = null;

        try {
            db = new DBManager().initializeJDBCDriver("file","db_file");
        } catch (Exception ex1) {
            ex1.printStackTrace();    // could not start db

            return;                   // bye bye
        }

        try {

            db.update(
                    "CREATE TABLE sample_table ( id INTEGER IDENTITY, str_col VARCHAR(256), num_col INTEGER)");
        } catch (SQLException ex2) {

            ex2.printStackTrace();
        }

        try {

            // add some rows - will create duplicates if run more then once
            // the id column is automatically generated
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Ford', 100)");
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Toyota', 200)");
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('Honda', 300)");
            db.update(
                    "INSERT INTO sample_table(str_col,num_col) VALUES('GM', 400)");

            // do a query
            db.query("SELECT * FROM sample_table WHERE num_col < 500");

            // at end of program
            db.shutDown();
        } catch (SQLException ex3) {
            ex3.printStackTrace();
        }
    }
}
