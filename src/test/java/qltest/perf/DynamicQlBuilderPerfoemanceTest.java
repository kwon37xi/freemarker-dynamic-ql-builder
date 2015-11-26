package qltest.perf;

import freemarker.cache.MruCacheStorage;
import freemarker.template.Configuration;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.FreemarkerDynamicQlBuilder;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.FreemarkerDynamicQlBuilderFactory;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.util.*;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Test for figure out concurrency problem and performace.
 */
public class DynamicQlBuilderPerfoemanceTest {

    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();


    private static FreemarkerDynamicQlBuilder builder;

    @Before
    public void setUp() throws Exception {
        Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_23);
        freemarkerConfiguration.setClassForTemplateLoading(AbstractFreemarkerDynamicQlBuilderTest.class, "/META-INF/qltest");
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setNumberFormat("0.######");
        freemarkerConfiguration.setTemplateUpdateDelayMilliseconds(3600000L);
        freemarkerConfiguration.setCacheStorage(new MruCacheStorage(500, 5000));
        builder = new FreemarkerDynamicQlBuilderFactory(freemarkerConfiguration)
                .getFreemarkerDynamicQlBuilder();
    }

    @Test
    @PerfTest(threads = 50, duration = 60000, rampUp = 100, warmUp = 5000)
    public void perftest() throws Exception {
        final String name = UUID.randomUUID().toString();
        final Date date = new Date();
        Random random = new Random();
        final List<Integer> userIds = Collections.unmodifiableList(Arrays.asList(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt()));

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("name", name);
        dataModel.put("createdAt", date);
        dataModel.put("userIds", userIds);

        DynamicQuery dynamicQuery = builder.buildQuery("perf/perf", dataModel);

        List<Object> params = dynamicQuery.getQueryParameters();

        assertThat(params.size(), is(7));
        assertThat(params, Matchers.hasItems((Object) name, date));
        for (Integer userId : userIds) {
            assertThat(params, hasItem(userId));
        }
    }
}
