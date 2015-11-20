package kr.pe.kwonnam.fdqlbuilder.objectunwrapper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Integration test template for {@link TemplateModelObjectUnwrapper}.
 * Every {@link TemplateModelObjectUnwrapper} implementation must be tested with this test template.
 */
public abstract class AbstractTemplateModelObjectUnwrapperTest {
    public static final String UNWRAP_TEMPLATE_NAME = "unwrap";

    /**
     * marker when dataModel is not an instance of TemplateModel
     */
    public static final Object NOT_TEMPLATE_MODEL = new Object();

    private Logger log = getLogger(AbstractTemplateModelObjectUnwrapperTest.class);

    protected Configuration configuration;

    private StringTemplateLoader templateLoader;

    protected Template template;

    protected ParameterUnwrapMethod parameterUnwrapMethod;

    /**
     * implement this method and return TemplateModelObjectUnwrapper instance to be tested
     */
    protected abstract TemplateModelObjectUnwrapper populateUnwrapper(Configuration configuration);

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration(Configuration.VERSION_2_3_23);

        templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate(UNWRAP_TEMPLATE_NAME, "${parameterUnwrap(param)}");

        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setNumberFormat("0.######");

        template = configuration.getTemplate(UNWRAP_TEMPLATE_NAME);
        parameterUnwrapMethod = new ParameterUnwrapMethod(populateUnwrapper(configuration));
    }

    /**
     * Call this method to test if the param correctly unwrapped.
     *
     * @param param templatemethod parameter object.
     * @return unwrapped object. If {@link TemplateModelObjectUnwrapper} works properly, param object and return object must be the same.
     */
    protected Object testUnwrap(Object param) throws IOException, TemplateException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parameterUnwrap", parameterUnwrapMethod);
        params.put("param", param);

        StringWriter out = new StringWriter();
        template.process(params, out);
        log.debug("Template process result for dataModel {} - {}", param, out.toString());

        return parameterUnwrapMethod.getUnwrapped();
    }

    @Test
    public void testInputSream() throws Exception {
        final InputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        assertThat(testUnwrap(is)).as("InputStream").isSameAs(is);
    }

    @Test
    public void testBigDecimal() throws Exception {
        final BigDecimal bigDecimal = new BigDecimal("100.123");
        assertThat(testUnwrap(bigDecimal)).as("BigDecimal").isSameAs(bigDecimal);
    }

    @Test
    public void testBlob() throws Exception {
        final Blob blob = new SerialBlob(new byte[]{1, 2, 3});
        assertThat(testUnwrap(blob)).as("blob").isSameAs(blob);
    }

    @Test
    public void testReader() throws Exception {
        final Reader reader = new StringReader("Hello");
        assertThat(testUnwrap(reader)).as("Reader").isSameAs(reader);
    }

    @Test
    public void testClob() throws Exception {
        final Clob clob = new SerialClob("Hello Clob".toCharArray());
        assertThat(testUnwrap(clob)).as("Clob").isSameAs(clob);
    }

    @Test
    public void testDate() throws Exception {
        final Date date = new Date();
        assertThat(testUnwrap(date)).as("java.util.Date").isSameAs(date);
    }

    @Test
    public void testSqlDate() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        assertThat(testUnwrap(date)).as("java.sql.Date").isSameAs(date);
    }

    @Test
    public void testSqlTime() throws Exception {
        final java.sql.Time time = new java.sql.Time(new Date().getTime());
        assertThat(testUnwrap(time)).as("java.sql.Time").isSameAs(time);
    }

    @Test
    public void testSqlTimestamp() throws Exception {
        final java.sql.Timestamp timestamp = new Timestamp(new Date().getTime());
        assertThat(testUnwrap(timestamp)).as("java.sql.Timestamp").isSameAs(timestamp);
    }

    @Test
    public void setCalendar() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        assertThat(testUnwrap(calendar)).as("Calendar").isSameAs(calendar);
    }

    @Test
    public void testDouble() throws Exception {
        final Double numberDouble = new Double(123.45d);
        assertThat(testUnwrap(numberDouble)).as("Double").isSameAs(numberDouble);
    }

    @Test
    public void testFloat() throws Exception {
        final Float numberFloat = new Float(123.45f);
        assertThat(testUnwrap(numberFloat)).as("Float").isSameAs(numberFloat);
    }

    @Test
    public void testInteger() throws Exception {
        final Integer numberInteger = new Integer(123);
        assertThat(testUnwrap(numberInteger)).as("Integer").isSameAs(numberInteger);
    }

    @Test
    public void testLong() throws Exception {
        final Long numberLong = new Long(12345678901234567L);
        assertThat(testUnwrap(numberLong)).as("Long").isSameAs(numberLong);
    }

    @Test
    public void testString() throws Exception {
        final String string = "Hello ql";
        assertThat(testUnwrap(string)).as("String").isSameAs(string);
    }

    @Test
    public void testStringArray() throws Exception {
        final String[] stringArray = new String[]{"hello", "world"};
        assertThat(testUnwrap(stringArray)).isSameAs(stringArray);
    }

    @Test
    public void testValueObjectArray() throws Exception {
        final User[] users = new User[]{new User("hello", 12), new User("world", 30)};
        assertThat(testUnwrap(users)).as("VO Array").isSameAs(users);
    }

    @Test
    public void testNumberList() throws Exception {
        final List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        assertThat(testUnwrap(integers)).as("Integer List").isSameAs(integers);
    }

    @Test
    public void testValueObjectList() throws Exception {
        final List<User> users = new ArrayList<User>();
        users.add(new User("hello", 10));
        users.add(new User("freemarker", 20));
        users.add(new User("ql", 30));

        assertThat(testUnwrap(users)).as("ValueObject List").isSameAs(users);
    }

    @Test
    @Ignore(value = "The current version of freemarker does not have any way to get original Set from SimpleSequence.")
    public void testStringSet() throws Exception {
        final Set<String> strings = new HashSet<String>();
        strings.add("hello");
        strings.add("world");

        assertThat(testUnwrap(strings)).as("String Set").isSameAs(strings);
    }

    @Test
    @Ignore(value = "The current version of freemarker does not have any way to get original Set from SimpleSequence.")
    public void testValueObjectSet() throws Exception {
        final Set<User> users = new HashSet<User>();
        users.add(new User("hello", 20));
        users.add(new User("ql", 50));

        assertThat(testUnwrap(users)).as("ValueObject Set").isSameAs(users);
    }

    @Test
    public void testMap() throws Exception {
        final Map<String, User> userMap = new HashMap<String, User>();
        userMap.put("hello", new User("hello", 70));
        userMap.put("world", new User("world", 100));

        assertThat(testUnwrap(userMap)).as("Map").isSameAs(userMap);
    }

    @Test
    public void testJodaTimeDateTime() throws Exception {
        final DateTime dateTime = new DateTime(new Date());
        assertThat(testUnwrap(dateTime)).as("JodaTime DateTime").isSameAs(dateTime);
    }

    @Test
    public void testJodaTimeLocalDateTime() throws Exception {
        final LocalDateTime localDateTime = new LocalDateTime(new Date());
        assertThat(testUnwrap(localDateTime)).as("JodaTime LocalDateTime").isSameAs(localDateTime);
    }

    @Test
    public void testJodaMoney() throws Exception {
        final Money money = Money.of(CurrencyUnit.USD, new BigDecimal("999.99"));
        assertThat(testUnwrap(money)).as("JodaTime Money").isSameAs(money);
    }

    @Test
    public void testUnknownWrapper() throws Exception {

        final Set<User> users = new HashSet<User>();
        users.add(new User("hello", 20));
        users.add(new User("ql", 50));

        try {
            testUnwrap(users);
            failBecauseExceptionWasNotThrown(TemplateException.class);
        } catch (TemplateException ex) {
            assertThat(ex).as("Current version of Freemarker does not support SimpleSequence.")
                    .hasMessageContaining("freemarker.template.SimpleSequence is not supported.");
        }
    }

    private static class ParameterUnwrapMethod implements TemplateMethodModelEx {
        private Logger log = getLogger(ParameterUnwrapMethod.class);
        private TemplateModelObjectUnwrapper unwrapper;
        private Object unwrapped;

        public ParameterUnwrapMethod(TemplateModelObjectUnwrapper unwrapper) {
            this.unwrapper = unwrapper;
        }

        public Object getUnwrapped() {
            return unwrapped;
        }

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            Object dataModel = arguments.get(0);

            if (dataModel instanceof TemplateModel) {
                log.info("Unwrapping dataModel {} - {}", dataModel.getClass(), dataModel);
                unwrapped = unwrapper.unwrap((TemplateModel) dataModel);
            } else {
                log.warn("dataModel is not TemplateModel - {}", dataModel);
                unwrapped = NOT_TEMPLATE_MODEL;
            }

            return new SimpleScalar(unwrapped.getClass().getCanonicalName());
        }
    }

    public static class User {
        private String name;
        private int birthyear;

        public User() {
        }

        public User(String name, int birthyear) {
            this.name = name;
            this.birthyear = birthyear;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBirthyear() {
            return birthyear;
        }

        public void setBirthyear(int birthyear) {
            this.birthyear = birthyear;
        }
    }
}