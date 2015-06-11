package pl.uj.edu.student.tsp.testdemo;

/**
 * Created by michal on 11.06.15.
 */

import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.uj.edu.student.tsp.guicedemo.FacebookService;
import pl.uj.edu.student.tsp.guicedemo.MessageService;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DemoTest {

    @Mock
    FacebookService cls;

    @InjectMocks
    ContainsInject testedObject = new ContainsInject();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void nothing() throws Exception {

    }

    @Test
    public void mockAnnotation() throws Exception {
        assertThat(cls, notNullValue());
    }

    @Test
    public void mockInjection() throws Exception {
        assertThat(testedObject.getMessageService(), notNullValue());
    }

    private static class ContainsInject {

        @Inject
        private MessageService messageService;

        public MessageService getMessageService() {
            return messageService;
        }
    }
}
