package com.suban.framework.utils;

import com.suban.framework.config.ConfigReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Utility to fetch OTP codes from the staging OpenIDM endpoint.
 *
 * Secrets are read from environment variables or config — never hard-coded.
 *
 * Required environment variables (or config keys):
 *   OTP_BEARER_TOKEN  → the Bearer token for Authorization header
 *   OTP_COOKIE        → the full Cookie header value (optional, may be empty)
 */
public class OTPCodeUtils {

    private static final Logger logger = LoggerFactory.getLogger(OTPCodeUtils.class);
    private static final String CONTENT_TYPE = "application/json";
    private static final String OTP_USERNAME = "anonymous";
    private static final String OTP_PASSWORD = "anonymous";

    // Hardcoded fallback URL — Toyota staging OpenIDM endpoint.
    private static final String DEFAULT_OTP_URL =
        "https://openidm.stg.toyotadriverslogin.com/openidm/endpoint/getOTPCode";

    // Fallback bearer token — used when OTP_BEARER_TOKEN env var is not set.
    private static final String DEFAULT_OTP_BEARER_TOKEN = "H8XkZLbwfKi42mHuYSEoKg";

    /**
     * Fetches the OTP code for the given email address.
     *
     * The email is passed as the {@code id_value} in the request body so that
     * the correct OTP is generated for whichever account is being logged in.
     * Never falls back to config — the caller (step definition) is responsible
     * for supplying the exact email used to sign in.
     *
     * @param email the account email address to fetch the OTP for
     */
    public static String fetchOTP(String email) throws Exception {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "fetchOTP: email must not be null or blank — " +
                "pass the same email address used for login");
        }

        String bearerToken = resolveSecret("OTP_BEARER_TOKEN", "otp.bearer.token");
        String cookie      = resolveSecret("OTP_COOKIE",       "otp.cookie");

        String otpUrl;
        try {
            otpUrl = ConfigReader.getProperty("otp.url");
            if (otpUrl == null || otpUrl.trim().isEmpty()) otpUrl = DEFAULT_OTP_URL;
        } catch (Exception e) {
            otpUrl = DEFAULT_OTP_URL;
        }
        logger.info("OTP endpoint URL: {}", otpUrl);
        logger.info("Fetching OTP for email: {}", email);

        HttpURLConnection conn = buildConnection(otpUrl, bearerToken, cookie);

        // Build the JSON body with the caller-supplied email — never hardcoded.
        String jsonBody = "{\"id_value\": \"" + email + "\"}";
        logger.info("OTP request body: {}", jsonBody);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        logger.debug("OTP endpoint response code: {}", responseCode);

        Scanner scanner;
        if (responseCode / 100 == 2) {
            scanner = new Scanner(conn.getInputStream());
        } else {
            scanner = new Scanner(conn.getErrorStream());
        }

        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        conn.disconnect();

        String responseStr = response.toString();
        // Always log the full response so failures are diagnosable
        logger.info("OTP endpoint HTTP status: {}", responseCode);
        logger.info("OTP raw response: {}", responseStr);

        if (!responseStr.trim().startsWith("{")) {
            throw new RuntimeException(
                "OTP endpoint returned non-JSON (HTTP " + responseCode + "). " +
                "Raw response: " + responseStr + "\n" +
                "Fix: set OTP_BEARER_TOKEN and OTP_COOKIE env vars before running mvn clean test."
            );
        }

        JSONObject json = new JSONObject(responseStr);
        if (!json.has("otpCode")) {
            throw new RuntimeException(
                "OTP response JSON does not contain 'otpCode'. Full response: " + responseStr
            );
        }
        String otpCode = json.getString("otpCode");
        logger.info("OTP code fetched successfully");
        return otpCode;
    }

    /**
     * @deprecated Use {@link #fetchOTP(String)} instead, passing the exact email
     *             address used to sign in.  This overload always reads
     *             {@code login.email} from config, which returns the wrong OTP
     *             when a different account profile is active.
     */
    @Deprecated
    public static String fetchOTP() throws Exception {
        String email = ConfigReader.getProperty("login.email");
        logger.warn("fetchOTP() called without explicit email — using config value '{}'. "
                  + "Use fetchOTP(email) to ensure the correct OTP is fetched.", email);
        return fetchOTP(email);
    }

    private static HttpURLConnection buildConnection(String otpUrl, String bearerToken, String cookie)
            throws IOException {
        URL url = new URL(otpUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE);
        conn.setRequestProperty("X-OpenIDM-Username", OTP_USERNAME);
        conn.setRequestProperty("X-OpenIDM-Password", OTP_PASSWORD);
        conn.setRequestProperty("LegacyToGlobal", "true");

        if (bearerToken != null && !bearerToken.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + bearerToken);
        }
        if (cookie != null && !cookie.isEmpty()) {
            conn.setRequestProperty("Cookie", cookie);
        }

        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        return conn;
    }

    // Fallback cookie — used when OTP_COOKIE env var is not set.
    // This is the session cookie required by the Toyota OpenIDM staging endpoint.
    private static final String DEFAULT_OTP_COOKIE =
        "INGRESSCOOKIE=1730394742.661.2176.900818|5c8d5ffaf4a96b489c1d23ba36b2fd0f; " +
        "session-jwt=eyJ0eXAiOiJKV1QiLCJraWQiOiJvcGVuaWRtLWp3dHNlc3Npb25obWFjLWtleSIsImN0eSI6IkpXVCIsImFsZyI6IkhTMjU2In0" +
        ".ZXlKMGVYQWlPaUpLVjFRaUxDSnJhV1FpT2lKdmNHVnVhV1J0TFd4dlkyRnNhRzl6ZENJc0ltVnVZeUk2SWtFeE1qaERRa010U0ZNeU5UWWlMQ0po" +
        "YkdjaU9pSlNVMEV4WHpVaWZRLmJPb0pqdjFyUmJlZXJ6MGVkLVU2WTBWX2d1bXo2WGphaDNZU2UtUU1OTTcwNEdrVlVGY3J6WEMzZE1tSWNMUEZI" +
        "aU5ZZDRtQmhZblBrcVdya0dsLWE1bVc3WmZKZ3RrTU8wbUJsSGI4NjZHd0w4S0hkZkZtNFNENWZXZTB1Y0hBdUthbnFQOTFHdGV4Q1ZyMFQ1MmtP" +
        "a3F2SmNNUTlWVHVzY2lzNE9iVDVPZ3hVNXdEdkoxWFBldVI5d2dZQWstd1hYeFJRcENfQ2VSdkJPSTNGTUstUFRxd0g3TjdiN2dUM29NTWlPRjdPLV" +
        "NxcEtaWjd3WGhCM1N6c2VMZkNMd2lWTFlDNl9xVXlaVFU4LXJVcDFqUEY5Zzhsd1NwU1d3Q2dyOTk2R1o4RTRSNWZ2NUFxZXozbkZNRVZoN3JtelVM" +
        "UFJvNHdTNTJqdkljd3I4eUc0M0c1US44TlJHMmdBRmI2VzdNSE1RaGtiUGd3LlplWDFaRmktNTdlY1dRemRkTVluTldKY1VzSEt0ZXJfODNMMmxKaXM5" +
        "dVlEZDg0eS1EVXhQMkhRVzJ5ckFpeV9OSnRpVVdDWmtGZi1JdjhWTFdOT1BzUDZ0ZFQ1dzhsdXR6N1hmNzFFUG1veVc1R3Nqb3pNdW84MWNTN3BXekhB" +
        "T3lWQUVEWExOQmJhOVA3WEpVMVBDLU9zZmN3UlQ2cjZkall1NFRfLUJERHJlTURUNlJVcllWdHg4Z0toWkRUbC1XejNwVEc1dkdBejVFZ21EVTVlZ2hF" +
        "VjE2VFZ6ZUxRNHJNNTR1Y1JnWUVyQXhOaE9zaW1mZGdBb2F2UmVPSURqZTE1ZFR4WmxwbUppSTlfbHo1VmN1MDVpRTJZMVQ3N2dUMEU4UEFGNDE3bHpl" +
        "QW56QTc2U0pkT2YwcGdieEl6TUhhdUZWZnhSdXVLZV9yVE50MWdoaVE2WEtjMGhTcENFd0lHZHFkN3E3TnhfU0dOdXU0YlVFMk51Y19KQURBdDhFNUNr" +
        "RWNVZDRBeE1xMVhTcGlFWEt6WkgwR3E0NHU3RTdqTmZ5eENXLUdNcERJWDVBc096V09rOC1kbnBPYlZZVmtYd0d4SUpSOE93RWxxek1fTGxwR3E4bjZR" +
        "U2Iwbk50SmNLWUtCemRRaTlmcHJBYjlTMml4UWppdzdkMmxRaXZvemFPOGZ0MUNkUUpJVXA0bzE5azhEMi1JSnpycFp5ZW1sYnEtcGhjRHh1c2p5OUMy" +
        "a0hqbzBIeTZQR3JXdkpMSnR4cEVxeDF2Y0x4UklPb1N3RFBsRUVMcXdySmc2Z0NvbWlwMnZvb014ODRTRXZXN3NObFlQMjh0dElDUzZYZDNvZnd0MHRF" +
        "X2pYRHhwQmtELTc3QS5CV09vWTREa2ZZZEJudzhhZkFlTW9nLl9NLUtzVVBKN2VKbFFXU1VSZlo1YlJBbzZVLUEtR21MZmxVVlhldUNPdWs";

    /**
     * Resolves a secret from environment variable first, then system property,
     * then config file, then built-in default. Returns empty string if not found.
     */
    private static String resolveSecret(String envKey, String configKey) {
        String value = System.getenv(envKey);
        if (value != null && !value.isEmpty()) {
            logger.debug("Secret '{}' resolved from environment variable", envKey);
            return value;
        }
        value = System.getProperty(envKey);
        if (value != null && !value.isEmpty()) {
            logger.debug("Secret '{}' resolved from system property", envKey);
            return value;
        }
        try {
            value = ConfigReader.getProperty(configKey);
            if (value != null && !value.isEmpty()) {
                logger.debug("Secret '{}' resolved from config", configKey);
                return value;
            }
        } catch (Exception e) {
            // not in config — fall through to default
        }
        // Fall back to built-in defaults for known secrets
        if ("OTP_COOKIE".equals(envKey)) {
            logger.info("OTP_COOKIE not set — using built-in staging cookie fallback");
            return DEFAULT_OTP_COOKIE;
        }
        if ("OTP_BEARER_TOKEN".equals(envKey)) {
            logger.info("OTP_BEARER_TOKEN not set — using built-in staging token fallback");
            return DEFAULT_OTP_BEARER_TOKEN;
        }
        logger.warn("Secret '{}' not found in env, system props, or config — proceeding without it", envKey);
        return "";
    }
}
