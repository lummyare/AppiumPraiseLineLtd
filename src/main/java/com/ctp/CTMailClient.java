package com.ctp;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CTMailClient {
    public static final String BASE_URL = "https://of9en6ylbb.execute-api.us-east-1.amazonaws.com/prod/ctmail/";
    public static final String TOKEN = "allow_ctmail";
    public static final String API_KEY = "***REMOVED***";

    public CTMailClient() {
    }

    public String getAccount(String start, String numAccount) {
        String response = "";

        try {
            URL url = new URL(BASE_URL + "accounts/offset/" + start + "/count/" + numAccount);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("AuthorizationToken", TOKEN);
            conn.setRequestProperty("x-api-key", API_KEY);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getMessages(String account, String since) {
        String response = "";

        try {
            URL url = new URL(BASE_URL + "accounts/" + account + "/messages/since/" + since);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("AuthorizationToken", TOKEN);
            conn.setRequestProperty("x-api-key", API_KEY);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String deleteMessage(String account, String id) {
        String response = "";

        try {
            URL url = new URL(BASE_URL + "accounts/" + account + "/messages/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("AuthorizationToken", TOKEN);
            conn.setRequestProperty("x-api-key", API_KEY);
            conn.setRequestMethod("DELETE");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String creatAccount(JSONObject accountObj) {
        String response = "";

        try {
            URL url = new URL(BASE_URL + "accounts");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("AuthorizationToken", TOKEN);
            conn.setRequestProperty("x-api-key", API_KEY);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            String account = accountObj.toString();

            OutputStream os = conn.getOutputStream();
            os.write(account.getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String modifyAccount(JSONObject passwordObj, String account) {
        String response = "";

        try {
            URL url = new URL(BASE_URL + "accounts/" + account);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("AuthorizationToken", TOKEN);
            conn.setRequestProperty("x-api-key", API_KEY);
            conn.setDoOutput(true);
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            conn.setRequestMethod("POST");

            String password = passwordObj.toString();

            OutputStream os = conn.getOutputStream();
            os.write(password.getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String deleteAccount(String account) {
        String response = "";

        try {
            URL url = new URL(BASE_URL + "accounts/" + account);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("AuthorizationToken", TOKEN);
            conn.setRequestProperty("x-api-key", API_KEY);
            conn.setRequestMethod("DELETE");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String CreateEmail(String strFirstName, String strLastName, String strTeamID, String strPassword) {
        JSONObject accountObj = new JSONObject();
        accountObj.put("firstName", strFirstName);
        accountObj.put("lastname", strLastName);
        accountObj.put("teamId", strTeamID);
        accountObj.put("password", strPassword);
        creatAccount(accountObj);
        return strFirstName + strLastName + strTeamID + "@mail.tmnact.io";
    }

    public String getActivationCode(String strEmail) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date()) + "-0-0-0";
        String messages = getMessages(strEmail, date);
        Pattern pattern = Pattern.compile(".*(\\d{6})");
        Matcher matcher = pattern.matcher(messages);
        List lstMessages = JsonPath.read(messages, "$..messageId");
        String strMessageID = lstMessages.get(lstMessages.size() - 1).toString();
        deleteMessage(strEmail, strMessageID);
        matcher.find();
        return matcher.group(1);
    }

    public String getEmailSubject(String strEmail) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date()) + "-0-0-0";
        String messages = getMessages(strEmail, date);
        List lstMessages1 = JsonPath.read(messages, "$.messages..subject");
        String emailSubject = lstMessages1.get(lstMessages1.size() - 1).toString();
        //delete email
        //List lstMessages = JsonPath.read(messages, "$..messageId");
        //String strMessageID = lstMessages.get(lstMessages.size() - 1).toString();
        //deleteMessage(strEmail, strMessageID);

        return emailSubject;
    }

    public String getPasswordResetCode(String strEmail) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date()) + "-0-0-0";
        String messages = getMessages(strEmail, date);
        Pattern pattern = Pattern.compile(".*(\\d{6})");
        Matcher matcher = pattern.matcher(messages);
        List lstMessages = JsonPath.read(messages, "$..messageId");
        String strMessageID = lstMessages.get(lstMessages.size() - 1).toString();
        //deleteMessage(strEmail, strMessageID);
        matcher.find();
        return matcher.group(1);
    }
}
