import java.io.File;
import java.sql.*;

/**
 * Created by rohitsehgal on 12/7/17.
 */
public class DBManager {


    Connection conn;

    public DBManager initializeJDBCDriver(String dbType, String fileName) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        conn = DriverManager.getConnection(getConnectionStringFor(dbType, fileName), "sa", "");
        return this;
    }

    private String getConnectionStringFor(String dbType, String fileName) {
        String connectionString;
        if (dbType.equalsIgnoreCase("file")) {
            String temp = System.getProperty("user.dir") + File.separator + "abc" + File.separator + fileName;
//            jdbcDataSource jdbcDataSource  =  new jdbcDataSource();
            connectionString = String.format("jdbc:hsqldb:file:%s", temp);
        } else {
            connectionString = String.format("jdbc:hsqldb:mem:%s", fileName);
        }
        System.out.println(connectionString);
        return connectionString;
    }

    public void shutDown() throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("SHUTDOWN");
        statement.close();
        conn.close();
    }


    public synchronized void query(String sql) throws SQLException {
        Statement st = null;
        ResultSet resultSet = null;

        st = conn.createStatement();
        resultSet = st.executeQuery(sql);
        dump(resultSet);
        st.close();
    }

    public static void dump(ResultSet rs) throws SQLException {

        // the order of the rows in a cursor
        // are implementation dependent unless you use the SQL ORDER statement
        ResultSetMetaData meta = rs.getMetaData();
        int colmax = meta.getColumnCount();
        int i;
        Object o = null;

        // the result set is a cursor into the data.  You can only
        // point to one row at a time
        // assume we are pointing to BEFORE the first row
        // rs.next() points to next row and returns true
        // or false if there is no next row, which breaks the loop
        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed

                // with 1 not 0
                System.out.print(o.toString() + " ");
            }

            System.out.println(" ");
        }
    }                                       //void dump( ResultSet rs )

    public synchronized void update(String expression) throws SQLException {

        Statement st = null;

        st = conn.createStatement();    // statements

        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        st.close();
    }

}
