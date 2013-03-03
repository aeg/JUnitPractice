package chapter12;

import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static chapter12.ITableMatcher.tableOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Enclosed.class)
public class UserDaoYAMLTest {

    public static class usersHas2RecordCase {
        @ClassRule
        public static H2DatabaseServer server = new H2UTDatabaseServer();
        @Rule
        public DBUnitTester tester = new UserDaoDbUnitTester("fixture.yaml");

        UserDao sut;

        @Before
        public void setUp() throws Exception {
            this.sut = new UserDao();
        }

        @Test
        public void getListで2件取得できる事() throws Exception {
            // Excercise
            List<String> actual = sut.getList();
            // Verify
            assertThat(actual, is(notNullValue()));
            assertThat(actual.size(), is(2));
            assertThat(actual.get(0), is("一郎"));
            assertThat(actual.get(1), is("次郎"));

        }

        @Test
        public void insertで1件追加できる() throws Exception {
            // Excercise
            sut.insert("三郎");
            // Verify
            ITable actual = tester.getConnection().createDataSet().getTable("users");

            ITable expected = new YamlDataSet("expected.yaml").getTable("users");
            assertThat(actual, is(tableOf(expected)));
        }

    }

    public static class usersHas0RecordCase {
        @ClassRule
        public static H2DatabaseServer server = new H2UTDatabaseServer();
        @Rule
        public DBUnitTester testr = new UserDaoDbUnitTester("zero_fixtures.yaml");

        UserDao sut;

        @Before
        public void setUp() throws Exception {
            this.sut = new UserDao();
        }


        @Test
        public void getListで0件取得できる事() throws Exception {
            // Exercise
            List<String> actual = sut.getList();
            // Verify
            assertThat(actual, is(notNullValue()));
            assertThat(actual.size(), is(0));
        }

        @Test
        public void insertで2件追加できる() throws Exception {
            // Exercise
            sut.insert("Sirou");
            sut.insert("Gorou");
            //Verify
            ITable actual = testr.getConnection().createDataSet().getTable("users");
            ITable expected = new YamlDataSet("zero_expected.yaml").getTable("users");
            assertThat(actual, is(tableOf(expected)));
        }

    }

    public static class usersHas2RecordAndUpdateCase {
        @ClassRule
        public static H2DatabaseServer server = new H2UTDatabaseServer();
        @Rule
        public DBUnitTester tester = new UserDaoDbUnitTester("fixture.yaml");

        UserDao sut;

        @Before
        public void setUp() throws Exception {
            this.sut = new UserDao();
        }

        @Test
        public void updateで1件更新ができる() throws Exception {
            // Exercise
            tester.executeQuery("update users set name='トム' where id=1");

            //Verify
            ITable actual = tester.getConnection().createDataSet().getTable("users");
            ITable expected = new YamlDataSet("updated_expected.yaml").getTable("users");

            SortedTable sortedActual = new SortedTable(actual, new String[]{"id"});
            SortedTable sortedExpected = new SortedTable(expected, new String[]{"id"});

            assertThat(sortedActual, is(tableOf(sortedExpected)));
        }

    }

    static class H2UTDatabaseServer extends H2DatabaseServer {
        public H2UTDatabaseServer() {
            super("h2", "db", "ut");
        }
    }

    static class UserDaoDbUnitTester extends DBUnitTester {
        private final String fixture;

        public UserDaoDbUnitTester(String fixture) {
            super("org.h2.Driver", "jdbc:h2:tcp://localhost/db;SCHEMA=ut",
                    "sa", "", "ut");
            this.fixture = fixture;
        }

        @Override
        protected void before() throws Exception {
            executeQuery("DROP TABLE IF EXISTS users");
            executeQuery(
                    "CREATE TABLE users(id INT AUTO_INCREMENT, name VARCHAR(64))"
            );
        }

        @Override
        protected org.dbunit.dataset.IDataSet createDataSet() throws Exception {
            //InputStream fixtureIn = getClass().getResourceAsStream(fixture);
            return new YamlDataSet(fixture);
        }
    }


}


