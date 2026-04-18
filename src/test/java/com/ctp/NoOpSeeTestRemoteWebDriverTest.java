package com.ctp;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NoOpSeeTestRemoteWebDriverTest {

    private static class StubRemoteWebDriver extends RemoteWebDriver {
        private String lastScript;
        private Object[] lastArgs;

        @Override
        public Object executeScript(String script, Object... args) {
            this.lastScript = script;
            this.lastArgs = args;
            return "delegated-sync";
        }

        @Override
        public Object executeAsyncScript(String script, Object... args) {
            this.lastScript = script;
            this.lastArgs = args;
            return "delegated-async";
        }
    }

    @Test
    void shouldReturnBooleanDefaultForBooleanCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.isElementFound(\"NATIVE\",\"xpath=//*\")");

        assertNotNull(result);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    void shouldReturnNumericDefaultForNumericCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.getElementCount(\"NATIVE\",\"xpath=//*\")");

        assertNotNull(result);
        assertEquals(0, result);
    }

    @Test
    void shouldReturnEmptyArrayForArrayCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.getAllValues(\"NATIVE\",\"xpath=//*\",\"text\")");

        assertNotNull(result);
        assertTrue(result instanceof String[]);
        assertEquals(0, ((String[]) result).length);
    }

    @Test
    void shouldReturnEmptyMapForMapCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.getLastCommandResultMap()");

        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertTrue(((Map<?, ?>) result).isEmpty());
    }

    @Test
    void shouldReturnEmptyStringForUnknownCommandsInsteadOfNull() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.someFutureCommand()", "arg");

        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void shouldReturnEmptyStringForVoidLikeCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.startStepsGroup(\"Subaru email Login\")");

        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    void shouldDelegateNonSeeTestScripts() {
        StubRemoteWebDriver delegate = new StubRemoteWebDriver();
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(delegate);

        Object result = wrapper.executeScript("return 1+1", 123);

        assertEquals("delegated-sync", result);
        assertEquals("return 1+1", delegate.lastScript);
        assertArrayEquals(new Object[]{123}, delegate.lastArgs);
    }

    @Test
    void shouldAlsoApplyDefaultsForAsyncScripts() {
        StubRemoteWebDriver delegate = new StubRemoteWebDriver();
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(delegate);

        Object result = wrapper.executeAsyncScript("seetest:client.stopStepsGroup()");

        assertNotNull(result);
        assertEquals("", result);
    }
}
