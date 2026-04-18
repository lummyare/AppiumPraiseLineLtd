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
    void shouldReturnSuccessMapWithBooleanResultForBooleanCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.isElementFound(\"NATIVE\",\"xpath=//*\")");

        Map<String, Object> map = assertSuccessMap(result);
        assertEquals(Boolean.FALSE, map.get("result"));
    }

    @Test
    void shouldReturnSuccessMapWithNumericResultForNumericCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.getElementCount(\"NATIVE\",\"xpath=//*\")");

        Map<String, Object> map = assertSuccessMap(result);
        assertEquals(0, map.get("result"));
    }

    @Test
    void shouldReturnSuccessMapWithEmptyArrayForArrayCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.getAllValues(\"NATIVE\",\"xpath=//*\",\"text\")");

        Map<String, Object> map = assertSuccessMap(result);
        assertTrue(map.get("result") instanceof String[]);
        assertEquals(0, ((String[]) map.get("result")).length);
    }

    @Test
    void shouldReturnSuccessMapForUnknownCommandsInsteadOfNull() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.someFutureCommand()", "arg");

        Map<String, Object> map = assertSuccessMap(result);
        assertEquals("", map.get("result"));
    }

    @Test
    void shouldReturnSuccessMapForVoidLikeCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.startStepsGroup(\"Subaru email Login\")");

        Map<String, Object> map = assertSuccessMap(result);
        assertEquals("", map.get("result"));
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
    void shouldAlsoApplySuccessMapDefaultsForAsyncScripts() {
        StubRemoteWebDriver delegate = new StubRemoteWebDriver();
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(delegate);

        Object result = wrapper.executeAsyncScript("seetest:client.stopStepsGroup()");

        Map<String, Object> map = assertSuccessMap(result);
        assertEquals("", map.get("result"));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> assertSuccessMap(Object result) {
        assertNotNull(result);
        assertTrue(result instanceof Map);
        Map<String, Object> map = (Map<String, Object>) result;
        assertEquals("success", map.get("status"));
        assertTrue(map.containsKey("result"));
        return map;
    }
}
