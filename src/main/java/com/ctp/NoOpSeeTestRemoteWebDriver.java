package com.ctp;

import com.google.gson.Gson;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * RemoteWebDriver wrapper that no-ops all SeeTest executeScript commands for local Appium runs.
 *
 * <p>For non-SeeTest scripts, execution is delegated to the real driver.</p>
 */
public class NoOpSeeTestRemoteWebDriver extends RemoteWebDriver {

    private static final Logger logger = LoggerFactory.getLogger(NoOpSeeTestRemoteWebDriver.class);
    private static final String SEE_TEST_PREFIX = "seetest:client";
    private static final Gson gson = new Gson();

    private final RemoteWebDriver delegate;

    public NoOpSeeTestRemoteWebDriver(RemoteWebDriver delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object executeScript(String script, Object... args) {
        if (isSeeTestClientScript(script)) {
            String methodName = extractSeeTestMethodName(script);
            logger.info("[LOCAL-NOOP] Skipping SeeTest command: {}", script);
            logger.debug("[LOCAL-NOOP] Returning default response for method: {}", methodName);
            return getDefaultReturnValue(script);
        }
        return delegate.executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        if (isSeeTestClientScript(script)) {
            String methodName = extractSeeTestMethodName(script);
            logger.info("[LOCAL-NOOP] Skipping async SeeTest command: {}", script);
            logger.debug("[LOCAL-NOOP] Returning default async response for method: {}", methodName);
            return getDefaultReturnValue(script);
        }
        return delegate.executeAsyncScript(script, args);
    }

    @Override
    public SessionId getSessionId() {
        return delegate.getSessionId();
    }

    @Override
    public Capabilities getCapabilities() {
        return delegate.getCapabilities();
    }

    private boolean isSeeTestClientScript(String script) {
        return script != null && script.trim().startsWith(SEE_TEST_PREFIX);
    }

    private String extractSeeTestMethodName(String script) {
        if (script == null) {
            return "unknown";
        }

        String trimmed = script.trim();
        int dotIndex = trimmed.indexOf(".");
        if (dotIndex < 0 || dotIndex + 1 >= trimmed.length()) {
            return "unknown";
        }

        int methodStart = dotIndex + 1;
        int parenIndex = trimmed.indexOf('(', methodStart);
        if (parenIndex < 0) {
            return trimmed.substring(methodStart).trim();
        }
        return trimmed.substring(methodStart, parenIndex).trim();
    }

    private Object getDefaultReturnValue(String script) {
        String methodName = extractSeeTestMethodName(script);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", "success");

        if (methodName.startsWith("is") || methodName.startsWith("wait")) {
            resultMap.put("result", false);
        } else if (methodName.contains("Count") || methodName.contains("2c")) {
            resultMap.put("result", 0);
        } else if (methodName.contains("All") || methodName.contains("Elements")) {
            resultMap.put("result", new String[0]);
        } else {
            resultMap.put("result", "");
        }

        return gson.toJson(resultMap);
    }
}
