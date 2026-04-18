package utils;

import com.ctp.CTMailClient;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.ctp.SeeTestKeywords.createLog;

public class CommonUtils {
    // private static final Logger logger = managers.Logger.getLogger();

    public static void delay(int waitTime) throws InterruptedException {
        TimeUnit.SECONDS.sleep(waitTime);
    }

    public static String getRandomName() {
        Faker faker = new Faker();
        return faker.name().firstName();
    }

    public static String getRandomPhoneNumber() {
        Random rnd = new Random();
        int number1 = rnd.nextInt(800) + 200;
        int number2 = rnd.nextInt(999);
        int number3 = rnd.nextInt(9999);
        return String.format("%03d", number1) + String.format("%03d", number2) + String.format("%04d", number3);
    }

  /*  public static Claim getClaim(String token, String field) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(field);
    }
*/
   /* public static String getGUIDUsingToken(String token) {
        token = token.replace("Bearer ", "").replace("Basic ", "");
        String guid = getClaim(token, "sub").asString();
        logger.debug("guid is - " + guid);
        return guid;
    }*/

    public static Date getTodayDate(int offset) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.SECOND, offset);
        return cal.getTime();
    }

    public static String generatePassword() {
        String dCase = "abcdefghijklmnopqrstuvwxyz";
        String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String sChar = "!@#$%^&*";
        String intChar = "0123456789";
        Random r = new Random();
        StringBuilder pass = new StringBuilder();

        createLog("Generating pass...");
        while (pass.length() != 16) {
            int rPick = r.nextInt(4);
            if (rPick == 0) {
                int spot = r.nextInt(26);
                pass.append(dCase.charAt(spot));
            } else if (rPick == 1) {
                int spot = r.nextInt(26);
                pass.append(uCase.charAt(spot));
            } else if (rPick == 2) {
                int spot = r.nextInt(8);
                pass.append(sChar.charAt(spot));
            } else {
                int spot = r.nextInt(10);
                pass.append(intChar.charAt(spot));
            }
        }
        createLog("Generated Pass: " + pass.toString());
        return pass.toString();
    }

    /* public static boolean checkIfTokenExpired(String token) {
        //Check if the token already exists and not expired
        Date expiryTime = getClaim(token, "exp").asDate();
        Date currTime = getTodayDate(0);
        logger.debug("Token expiry time: " + expiryTime + ", Current Time: " + currTime);
        if (expiryTime.getTime() > currTime.getTime()) {
            long seconds = (expiryTime.getTime() - currTime.getTime()) / 1000;
            if (seconds < 60) {
                logger.debug("Token about to expire in less than 60 seconds, generate new one");
                return true;
            } else {
                logger.debug("Token good to use");
                return false;
            }
        } else {
            logger.debug("Token expired, generate new one");
            return true;
        }
    }
*/
    public static String fetchAccountActivationCodeFromUserEmail(String purpose, String account, Date emailTriggeredAfter, String strPackageName) throws Exception {
        //  logger.info("fetch activatsion code from user's email");
        int duration = 30000;
        String msgContent = null;
        String subject = null;
        switch (purpose) {
            case "passwordReset":
                if (strPackageName.contains("lexus")) {
                    subject = "Your Lexus app Password Reset Verification Code";
                } else if (strPackageName.contains("Toyota")) {
                    subject = "Your Toyota app Password Reset Verification Code";
                }
                break;
            case "CreateanAccount":
                if (strPackageName.contains("lexus")) {
                    subject = "Your Lexus App Verification Code";
                } else if (strPackageName.contains("Toyota")) {
                    subject = "Your Toyota App Verification Code";
                }
                break;

        }
        while (duration > 0) {
            msgContent = fetchMessageFromMatchingEmail(account, emailTriggeredAfter, subject);
            if (msgContent != null) {
              /*  logger.info("\nemail with matching criteria found:\naccount: " + account + "\nemailTriggeredAfter: "
                        + emailTriggeredAfter + "\nsubject: " + subject + "\n");
              */
                break;
            }
         /*   logger.info("\nemail with matching criteria not found:\naccount: " + account + "\nemailTriggeredAfter: "
                    + emailTriggeredAfter + "\nsubject: " + subject + "\ntry after 10 secs\n");
         */
            duration = duration - 10000;
            Thread.sleep(10000);
        }
        if (msgContent == null) {
            throw new Exception("email with matching criteria not found!");
        }
        String activationCode = StringUtils.substringBetween(msgContent, "Here is your 6-digit verification code:",
                "Sincerely").trim().replaceAll("\\*", "");
        //  logger.info("activation code: " + activationCode);
        return activationCode;
    }

    public static Date formatDate(Date date, String pattern, String timeZone) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return formatter.parse(formatter.format(date));
    }

    public static Date stringToDate(String strDate, String pattern, String timeZone) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return formatter.parse(strDate);
    }

    public static String dateToString(Date date, String pattern, String timeZone) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return formatter.format(date);
    }

    //create a new email account
 /*   public static JSONObject createTMNAEmailAccount() throws Exception {
        CtMailClient ctMailClient = new CtMailClient();
        JSONObject accountObj = new JSONObject();
        accountObj.put("firstName", getRandomName());
        accountObj.put("lastName", getRandomName());
        accountObj.put("teamId", Props.getProp("tmnactForgeRockTeamId"));
        accountObj.put("password", Props.getProp("tmnactEmailPassword"));
        String strEmailObj = ctMailClient.creatAccount(accountObj);
        JSONObject jsonEmailObj = new JSONObject(strEmailObj);
        String email = jsonEmailObj.get("email").toString();
        accountObj.put("email", email);
        logger.debug("new tmna email account created: " + email);
        return accountObj;
    }
*/
    //create a new email account
 /*   public static JSONObject createTMNAEmailAccountwithfirstLastName(String firstName, String lastName) throws Exception {
        CTMailClient ctMailClient = new CTMailClient();
        JSONObject accountObj = new JSONObject();
        accountObj.put("firstName", firstName);
        accountObj.put("lastName", lastName);
        accountObj.put("teamId", Props.getProp("tmnactForgeRockTeamId"));
        accountObj.put("password", Props.getProp("tmnactEmailPassword"));
        String strEmailObj = ctMailClient.creatAccount(accountObj);
        JSONObject jsonEmailObj = new JSONObject(strEmailObj);
        String email = jsonEmailObj.get("email").toString();
        accountObj.put("email", email);
        logger.debug("new tmna email account created: " + email);
        return accountObj;
    }
*/
    //create a new random email account
  /*  public static JSONObject createRandomEmailAccount(int counter) throws Exception {
        JSONObject accountObj = new JSONObject();
        accountObj.put("firstName", getRandomName());
        accountObj.put("lastName", getRandomName());
        accountObj.put("password", Props.getProp("tmnactEmailPassword"));
        String email = "ramobjazuretst" + counter + "@gmail.com";
        accountObj.put("email", email);
        logger.debug("new random email account created: " + email);
        return accountObj;
    }
*/

    //testoneapp1@mail.tmnact.io
    public static String fetchMessageFromMatchingEmail(String account, Date emailTriggeredAfter, String subject) throws Exception {
        String msgContent = null;
        CTMailClient ctMailClient = new CTMailClient();
        String messages = ctMailClient.getMessages(account,
                dateToString(getTodayDate(-60 * 60 * 24), "yyyy-MM-dd-HH-mm-ss", "UTC")); // retrieve last 1 day e-mails
        assert messages != null;
        //   logger.info("messages: " + messages);
        JSONObject json = new JSONObject(messages);
        JSONArray jsonArray = json.getJSONArray("messages");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject message = jsonArray.getJSONObject(i);
            Date receivedOnDate = stringToDate(message.get("date").toString(), "E, d MMM yyyy HH:mm:ss zzzz", "UTC");
            if (receivedOnDate.compareTo(emailTriggeredAfter) > 0 &&
                    message.get("subject").toString().equalsIgnoreCase(subject)) {
                msgContent = message.get("body").toString();
            }
        }
        return msgContent;
    }

 /*   public static void deleteTMNAEmailAccount(String email) {
        CtMailClient ctMailClient = new CtMailClient();
        String deletedAccount = ctMailClient.deleteAccount(email);
        JSONObject jsonDeletedAccount = new JSONObject(deletedAccount);
        logger.info("tmna email account deleted: " + jsonDeletedAccount.get("email").toString());
    }


    public static void printStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.info(sw.toString());
    }

    // Takes the json file with arguments and replaces with parameter values
    public static String buildJsonBody(String jsonReqFile, String paramValues) throws Exception {
        // Read JSON request from file into a String
        String fullFilePath = System.getProperty("user.dir") + jsonReqFile;
        String jsonInStr = new String(Files.readAllBytes(Paths.get(fullFilePath)));
        if (jsonInStr.contains("PARAM")) {
            int requestArgsCnt;
            // Split the delimited string  values
            StringTokenizer st = new StringTokenizer(paramValues, "|");
            List<String> paramsList = new LinkedList<>();
            while (st.hasMoreTokens()) {
                paramsList.add(st.nextToken());
            }
            int totParams = paramsList.size();
            requestArgsCnt = countMatches(jsonInStr, "PARAM");
            logger.debug("# of Params=" + requestArgsCnt);
            if (requestArgsCnt != totParams) {
                logger.error("Total PARAMS not matching with total input columns");
                throw new Exception("Matching Error");
            }

            if (requestArgsCnt > 0) {
                // Replace ARG with actual values
                for (int j = 0; j < requestArgsCnt; j++) {
                    jsonInStr = jsonInStr.replaceAll("PARAM" + String.format("%02d", j + 1), paramsList.get(j));
                }
            }
        }
        return jsonInStr;
    }

    public static void getDebugLogs(String logStr, Date startDateTime, String testName, String className, String methodName) throws Exception {
        try {
            String outputDir = System.getProperty("user.dir") + "/build/test-output/" + System.getProperty("JOB_NAME", "DEFAULT_JOB_NAME") + "_" + System.getProperty("BUILD_NUMBER", "1") + "/logs";

            logger.debug("");
            logger.debug("**************************************************DEBUG LOGS****************************************************");
            logger.debug("Refer - " + outputDir + "/" + testName + "_DEBUG.log");
            logger.debug("**************************************************DEBUG LOGS****************************************************");
            logger.debug("");

            InputStream stream = new ByteArrayInputStream(logStr.getBytes(StandardCharsets.UTF_8));
            Reader targetReader = new InputStreamReader(stream);
            BufferedReader in = new BufferedReader(targetReader);

            String line;
            String holdLine = null;
            boolean nextLine = false;
            boolean isCompare = false;

            Calendar cal = Calendar.getInstance();
            String year = Integer.toString(cal.get(Calendar.YEAR));

            File debugLog = new File(outputDir + "/" + testName + "_DEBUG.log");
            debugLog.createNewFile();

            PrintStream fileStream = new PrintStream(debugLog);
            fileStream.println("                                                                                    ");
            fileStream.println(
                    "                                                                                    ************************************************************************************");
            fileStream.println(
                    "                                                                                                test: "
                            + className + "_" + methodName);
            fileStream.println(
                    "                                                                                    ************************************************************************************");
            fileStream.println("                                                                                    ");

            while ((line = in.readLine()) != null) {
                if (!isCompare) {
                    if (StringUtils.startsWith(line, "[ ")) {
                        String[] keyVals = StringUtils.substringBetween(line, "[", "]").trim().split(" ");
                        String logDateTimeStr = year + "-" + keyVals[0] + "T" + keyVals[1] + "Z";

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        Date logDateTime = formatter.parse(logDateTimeStr);
                        logDateTime = formatter.parse(formatter.format(logDateTime));
//						logger.debug("Log Date Time = " + logDateTime + ", Start Date Time = " + getStartDateTime());

                        if (logDateTime.compareTo(startDateTime) > 0) {
                            isCompare = true;
                        }
                    }
                } else {
                    if (line.contains("D/OkHttp")) {
                        holdLine = line;
                        nextLine = true;
                    } else {
                        if (nextLine) {
//							logger.debug(holdLine + " " + line);
                            fileStream.println(holdLine + " " + line);
                            nextLine = false;
                        }
                    }
                }
            }
            in.close();
            fileStream.close();

        } catch (Exception e) {
            logger.debug(e.toString());
            throw e;
        }
    }

    public static String monthShortToLong(String shortMonth) {
        Map<String, String> shortToLong = new HashMap<>();
        DateFormatSymbols symbols = new DateFormatSymbols();

        for (int i = 0; i < symbols.getMonths().length; i++) {
            shortToLong.put(symbols.getShortMonths()[i], symbols.getMonths()[i]);
        }
        return shortToLong.get(shortMonth);
    }

    public static int getRandumNumberLimits(int min, int max) {
        int numb = (int) (Math.random() * (max - min) + min);
        return numb;
    }

    public static String getDeviceDate(String timeZone, int diff, JSONArray businesshours) throws ParseException {
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, diff);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy");
        dateFormat.setTimeZone(tz);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd");
        sdf1.setTimeZone(tz);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm a");
        sdf2.setTimeZone(tz);
        String date = dateFormat.format(cal.getTime());
        String month = sdf1.format(cal.getTime());
        String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        JSONArray time = new JSONArray();
        DateFormat sdf = new SimpleDateFormat("MMdd");
        sdf.setTimeZone(tz);
        DateFormat tf1 = new SimpleDateFormat("h:mm a");
        tf1.setTimeZone(tz);
        DateFormat tf = new SimpleDateFormat("HHmm");
        tf.setTimeZone(tz);
        DateFormat tf2 = new SimpleDateFormat("Hmm");
        tf2.setTimeZone(tz);
        DateFormat queryDate = new SimpleDateFormat("MMddyyyy");
        queryDate.setTimeZone(tz);
        DateFormat queryTime = new SimpleDateFormat("hh:mm a");
        queryTime.setTimeZone(tz);
        String fromatDate = queryDate.format(cal.getTime());
        String currentDateMonth = sdf.format(cal.getTime());
        String currentDayofWeek = days[cal.get(Calendar.DAY_OF_WEEK) - 1];
        dateFormat.setTimeZone(tz);
        JSONObject openCloseHours = new JSONObject();
        for (Object business : businesshours) {
            JSONObject cat = (JSONObject) business;
            int fromDate = cat.getInt("fromDate");
            int toDate = cat.getInt("toDate");
            if (Integer.parseInt(currentDateMonth) >= fromDate || Integer.parseInt(currentDateMonth) <= toDate) {
                openCloseHours = cat.getJSONObject("openCloseHours");
                break;
            }
        }

        while (openCloseHours.get(currentDayofWeek).toString().equals("null")) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            currentDayofWeek = days[cal.get(Calendar.DAY_OF_WEEK) - 1];
            date = dateFormat.format(cal.getTime());
            fromatDate = queryDate.format(cal.getTime());
            month = sdf1.format(cal.getTime());
        }

        JSONArray openCloseArray = openCloseHours.getJSONArray(currentDayofWeek);

        JSONObject openCloseObject = openCloseArray.getJSONObject(getRandumNumberLimits(0, openCloseArray.length()));
        int openingTime = Integer.parseInt(openCloseObject.getString("openingTime"));
        int closingTime = Integer.parseInt(openCloseObject.getString("closingTime"));
        int rentTime = openingTime;
        while (rentTime <= closingTime) {

            if (rentTime == openingTime) {

                if (String.valueOf(rentTime).length() == 3)
                    cal.setTime(tf2.parse(String.valueOf(rentTime)));
                else
                    cal.setTime(tf.parse(String.valueOf(rentTime)));
                time.put(tf1.format(cal.getTime()));
                cal.add(Calendar.MINUTE, 30);
                rentTime = Integer.parseInt(tf.format(cal.getTime()));
                continue;
            }
            if (String.valueOf(rentTime).length() == 3)
                cal.setTime(tf2.parse(String.valueOf(rentTime)));
            else
                cal.setTime(tf.parse(String.valueOf(rentTime)));
            time.put(tf1.format(cal.getTime()));
            cal.add(Calendar.MINUTE, 30);
            rentTime = Integer.parseInt(tf.format(cal.getTime()));
        }
        String randomAvailable_time = time.get(getRandumNumberLimits(0, time.length() - 1)).toString();
        logger.debug("Time Test " + date + " " + randomAvailable_time);
        return date + " " + randomAvailable_time + " " + fromatDate + " " + queryTime.format(queryTime.parse(randomAvailable_time)) + " " + month + " " + sdf2.format(sdf2.parse(randomAvailable_time));
    }

    public static String todayDateInMMMDDYYYYFormat() {
        //today's date in MMM DD YYYY format
        LocalDate todaysDate = LocalDate.now();
        String month = todaysDate.getMonth().toString().substring(0, 1) + todaysDate.getMonth().toString().substring(1, 3).toLowerCase();
        int day = todaysDate.getDayOfMonth();
        int year = todaysDate.getYear();
        String date = month + " " + day + ", " + year;
        logger.debug("today's date in MMM DD YYYY format: " + date);
        return date;
    }
*/

    // to check numeric value
    public static boolean isNumeric(String str) {
        createLog(String.format("Parsing string: \"%s\"", str));
        if (str == null || str.equals("")) {
            createLog("String cannot be parsed, it is null or empty.");
            return false;
        }
        try {
            int intValue = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            createLog("Input String cannot be parsed to Integer.");
        }
        return false;
    }
}