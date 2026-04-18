package com.ctp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.junit.jupiter.api.Assertions.*;

class NoOpSeeTestRemoteWebDriverTest {

    private static final Gson gson = new Gson();

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
    void shouldReturnStatusTrueJsonWithBooleanResultForBooleanCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.isElementFound(\"NATIVE\",\"xpath=//*\")");

        JsonObject json = assertStatusTrueJson(result);
        assertFalse(json.get("result").getAsBoolean());
    }

    @Test
    void shouldReturnStatusTrueJsonWithNumericResultForNumericCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.getElementCount(\"NATIVE\",\"xpath=//*\")");

        JsonObject json = assertStatusTrueJson(result);
        assertEquals(0, json.get("result").getAsInt());
    }

    @Test
    void shouldReturnStatusTrueJsonWithEmptyArrayForArrayCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.getAllValues(\"NATIVE\",\"xpath=//*\",\"text\")");

        JsonObject json = assertStatusTrueJson(result);
        JsonArray array = json.getAsJsonArray("result");
        assertNotNull(array);
        assertEquals(0, array.size());
    }

    @Test
    void shouldReturnStatusTrueJsonForUnknownCommandsInsteadOfNull() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.someFutureCommand()", "arg");

        JsonObject json = assertStatusTrueJson(result);
        assertEquals("", json.get("result").getAsString());
    }

    @Test
    void shouldReturnStatusTrueJsonForVoidLikeCommands() {
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(new StubRemoteWebDriver());

        Object result = wrapper.executeScript("seetest:client.startStepsGroup(\"Subaru email Login\")");

        JsonObject json = assertStatusTrueJson(result);
        assertEquals("", json.get("result").getAsString());
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
    void shouldAlsoApplyStatusTrueJsonDefaultsForAsyncScripts() {
        StubRemoteWebDriver delegate = new StubRemoteWebDriver();
        NoOpSeeTestRemoteWebDriver wrapper = new NoOpSeeTestRemoteWebDriver(delegate);

        Object result = wrapper.executeAsyncScript("seetest:client.stopStepsGroup()");

        JsonObject json = assertStatusTrueJson(result);
        assertEquals("", json.get("result").getAsString());
    }

    private JsonObject assertStatusTrueJson(Object result) {
        assertNotNull(result);
        assertTrue(result instanceof String, "SeeTest default response should be a JSON string");
        JsonObject json = gson.fromJson((String) result, JsonObject.class);
        assertNotNull(json);
        assertTrue(json.get("status").getAsBoolean());
        assertTrue(json.has("result"));
        return json;
    }
}
