package com.ctp;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * RemoteWebDriver wrapper that no-ops all SeeTest executeScript commands for local Appium runs.
 *
 * <p>For non-SeeTest scripts, execution is delegated to the real driver.</p>
 */
public class NoOpSeeTestRemoteWebDriver extends RemoteWebDriver {

    private static final Logger logger = LoggerFactory.getLogger(NoOpSeeTestRemoteWebDriver.class);
    private static final String SEE_TEST_PREFIX = "seetest:client";

    private final RemoteWebDriver delegate;

    public NoOpSeeTestRemoteWebDriver(RemoteWebDriver delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object executeScript(String script, Object... args) {
        if (isSeeTestClientScript(script)) {
            String methodName = extractSeeTestMethodName(script);
            logger.info("[LOCAL-NOOP] Skipping SeeTest command: {}", script);
            return getDefaultReturnValue(methodName);
        }
        return delegate.executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        if (isSeeTestClientScript(script)) {
            String methodName = extractSeeTestMethodName(script);
            logger.info("[LOCAL-NOOP] Skipping async SeeTest command: {}", script);
            return getDefaultReturnValue(methodName);
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

    private Object getDefaultReturnValue(String methodName) {
        if (methodName == null) {
            return "";
        }

        switch (methodName) {
            // boolean methods
            case "addTestProperty":
            case "applicationClose":
            case "clearPasscode":
            case "elementListVisible":
            case "elementSwipeWhileNotFound":
            case "getNetworkConnection":
            case "install":
            case "installConfigurationProfile":
            case "installWithCustomKeystore":
            case "isElementFound":
            case "isFoundIn":
            case "isPasscodeEnabled":
            case "pinch":
            case "reboot":
            case "setLocationPlaybackFile":
            case "setLocationPlaybackFileByReceiver":
            case "swipeWhileNotFound":
            case "syncElements":
            case "uninstall":
            case "uninstallConfigurationProfile":
            case "waitForElement":
            case "waitForElementToVanish":
            case "waitForSetLocationEnd":
                return Boolean.FALSE;

            // numeric methods
            case "elementGetTableRowsCount":
            case "getAvailableAgentPort":
            case "getDefaultTimeout":
            case "getElementCount":
            case "getElementCountIn":
            case "p2cx":
            case "p2cy":
                return 0;

            // array methods
            case "findElements":
            case "getAllValues":
            case "getBrowserTabIdList":
            case "getContextList":
            case "getDeviceSupportedLanguages":
            case "getDeviceSupportedRegions":
            case "getNVProfiles":
            case "getPickerValues":
            case "getRunningApplications":
            case "getSimCards":
                return new String[0];

            // object/map return
            case "getLastCommandResultMap":
                return Collections.emptyMap();

            // default object return: never null
            default:
                return "";
        }
    }
}