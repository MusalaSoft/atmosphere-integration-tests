package com.musala.atmosphere.client.builder;

import static com.musala.atmosphere.test.util.Constants.SERVER_PORT;

import org.junit.Ignore;
import org.junit.Test;

import com.musala.atmosphere.client.Builder;
import com.musala.atmosphere.client.exceptions.MissingServerConnectionProperiesException;
import com.musala.atmosphere.client.exceptions.ServerConnectionFailedException;
import com.musala.atmosphere.client.util.Server;

public class BuilderIntegrationTest {
    private static final int RETRY_LIMIT = 10;

    // TODO: update this test case
    @Ignore
    @Test(expected = MissingServerConnectionProperiesException.class)
    public void missingSeverAnnotationTest() {
        class SampleTestNoAnnotation {
            public Builder getBuilderInstance() {
                Builder testerBuilder = Builder.getInstance();
                return testerBuilder;
            }
        }
        SampleTestNoAnnotation sampleBuilderClassInstance = new SampleTestNoAnnotation();
    }

    @Test(expected = ServerConnectionFailedException.class)
    public void wrongIpTest() {
        @Server(ip = "149.111.400.154", port = SERVER_PORT, connectionRetryLimit = RETRY_LIMIT)
        class SampleTestWrongIpOne {
            public Builder getBuilderInstance() {
                Builder testerBuilder = Builder.getInstance();
                return testerBuilder;
            }
        }
        SampleTestWrongIpOne sampleBuilderClassInstance = new SampleTestWrongIpOne();
        Builder builderInstance = sampleBuilderClassInstance.getBuilderInstance();
    }

    @Test(expected = ServerConnectionFailedException.class)
    public void wrongIpTestTwo() {
        @Server(ip = "random_string", port = SERVER_PORT, connectionRetryLimit = RETRY_LIMIT)
        class SampleTestWrongIpTwo {
            public Builder getBuilderInstance() {
                Builder testerBuilder = Builder.getInstance();
                return testerBuilder;
            }
        }
        SampleTestWrongIpTwo sampleBuilderClassInstance = new SampleTestWrongIpTwo();
        Builder builderInstance = sampleBuilderClassInstance.getBuilderInstance();
    }

    @Test(expected = ServerConnectionFailedException.class)
    public void wrongPortTest() {
        @Server(ip = "localhost", port = SERVER_PORT + 148, connectionRetryLimit = RETRY_LIMIT)
        class SampleTestWrongPort {
            public Builder getBuilderInstance() {
                Builder testerBuilder = Builder.getInstance();
                return testerBuilder;
            }
        }
        SampleTestWrongPort sampleBuilderClassInstance = new SampleTestWrongPort();
        Builder builderInstance = sampleBuilderClassInstance.getBuilderInstance();
    }
}
