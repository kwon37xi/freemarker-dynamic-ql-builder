package qltest.jdbc;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.EmployeeType;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.User;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hamcrest.Matcher;
import org.junit.*;
import org.slf4j.Logger;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.sql.*;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class FreemarkerDynamicQlBuilderJdbcTest extends AbstractFreemarkerDynamicQlBuilderTest {
    private Logger log = getLogger(FreemarkerDynamicQlBuilderJdbcTest.class);

    private static JdbcConnectionPool pool;
    private Connection connection;


    @BeforeClass
    public static void initialize() throws ClassNotFoundException, SQLException {
        pool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");

        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE users (" +
                    "userId INT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "birthyear INT, " +
                    "employeeType VARCHAR(16)) " +
                    "AS SELECT * FROM CSVREAD('src/test/resources/META-INF/qltest/jdbc/users.csv')");
        } finally {
            closeQuietly(connection);
        }
    }

    @AfterClass
    public static void dispose() {
        pool.dispose();
    }

    @Before
    public void createConnection() throws Exception {
        connection = getConnection();
    }

    @After
    public void tearDown() throws Exception {
        closeQuietly(connection);

    }

    private static Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    private static void closeQuietly(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private void closeQuietly(PreparedStatement psmt) throws SQLException {
        if (psmt != null) {
            psmt.close();
        }
    }

    @Test
    public void insert_and_find_by_userId_just_inserted() throws Exception {
        dataModel().put("userId", 6);
        User user = new User();
        user.setName("test1");
        user.setBirthyear(2014);
        user.setEmployeeType(EmployeeType.FULLTIME);

        // insert
        dataModel().put("user", user);
        DynamicQuery insertDynamicQuery = processTemplate("jdbc/insert");

        assertThat(insertDynamicQuery.getQueryString(), containsString("VALUES(6, ?, 2014, ?)"));
        assertThat(insertDynamicQuery.getQueryParameters().size(), is(2));
        assertThat(insertDynamicQuery.getQueryParameters(), hasItems((Object) "test1", EmployeeType.FULLTIME.name()));

        PreparedStatement psmt = null;
        try {
            psmt = connection.prepareStatement(insertDynamicQuery.getQueryString());
            insertDynamicQuery.bindParameters(psmt);
            psmt.execute();
        } finally {
            closeQuietly(psmt);
        }

        // select the row just inserted
        dataModel().clear();
        dataModel().put("userId", 6);

        DynamicQuery selectDynamicQuery = processTemplate("jdbc/find_by_userId");
        assertThat(selectDynamicQuery.getQueryParameters().size(), is(1));
        assertThat(selectDynamicQuery.getQueryParameters(), hasItem((Object) 6));

        try {
            psmt = connection.prepareStatement(selectDynamicQuery.getQueryString());
            selectDynamicQuery.bindParameters(psmt);

            ResultSet rs = psmt.executeQuery();

            assertThat(rs.next(), notNullValue());

            int userId = rs.getInt("userId");
            String name = rs.getString("name");
            int birthyear = rs.getInt("birthyear");
            String employeeType = rs.getString("employeeType");

            log.debug("SELECT userId 6 : {}/{}/{}/{}", userId, name, birthyear, employeeType);
            assertThat(userId, is(6));
            assertThat(name, is("test1"));
            assertThat(birthyear, is(2014));
            assertThat(employeeType, is(EmployeeType.FULLTIME.name()));

        } finally {
            closeQuietly(psmt);
        }
    }

    @Test
    public void select_in_clause() throws Exception {
        dataModel().put("userIds", new int[]{1, 3, 5});

        DynamicQuery dynamicQuery = processTemplate("jdbc/find_by_userIds");
        assertThat(dynamicQuery.getQueryString(), containsString("WHERE userId in ("));
        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) 1, 3, 5));

        PreparedStatement psmt = null;
        try {
            psmt = connection.prepareStatement(dynamicQuery.getQueryString());
            dynamicQuery.bindParameters(psmt);

            ResultSet rs = psmt.executeQuery();
            assertThat(rs.next(), is(true));
            assertThat(rs.getInt("userId"), is(1));
            assertThat(rs.getString("name"), is("Freemarker"));

            assertThat(rs.next(), is(true));
            assertThat(rs.getInt("userId"), is(3));
            assertThat(rs.getString("name"), is("Jane"));

            assertThat(rs.next(), is(true));
            assertThat(rs.getInt("userId"), is(5));
            assertThat(rs.getString("name"), is("Kate"));

            assertThat(rs.next(), is(false));

        } finally {
            closeQuietly(psmt);
        }
    }

    @Test
    public void select_like_name() throws Exception {
        dataModel().put("name", "J%");

        DynamicQuery dynamicQuery = processTemplate("jdbc/like");
        PreparedStatement psmt = null;
        try {
            psmt = connection.prepareCall(dynamicQuery.getQueryString());
            dynamicQuery.bindParameters(psmt);

            ResultSet rs = psmt.executeQuery();

            assertThat(rs.next(), is(true));
            assertThat(rs.getString("name"), is("Jane"));

            assertThat(rs.next(), is(true));
            assertThat(rs.getString("name"), is("John"));

            assertThat(rs.next(), is(false));
        } finally {
            closeQuietly(psmt);
        }


    }
}
