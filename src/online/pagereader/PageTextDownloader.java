package online.pagereader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageTextDownloader {
    private static final String USER_AGENT = "Mozilla/5.0";

    @FunctionalInterface
    private interface Identity {
        String identity(String s);
    }
    public static String download(String url, String cookies, int firstLine, String lineSeparator){
        return download(url, cookies, firstLine, -1, lineSeparator);
    }
    public static String download(String url, String cookies, int firstLine, int lastLine, String lineSeparator) {
        if (url == null) {
            return "";
        }

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            // add request header
            con.setRequestProperty("User-Agent", PageTextDownloader.USER_AGENT);

            if (cookies != null && cookies.length() > 0) {
                con.setRequestProperty("Cookie", cookies);
            }

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return "";
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuffer response = new StringBuffer();
            String inputLine;

            Identity implementation = (lineSeparator == null) ? (input) -> input : (input) -> input + lineSeparator;

            int lineIndex = 0;
            // can merge if and else but this is faster
            if (lastLine > 0) {
                while ( (lineIndex <= lastLine) && ((inputLine = in.readLine()) != null)) {
                    if (lineIndex++ >= firstLine) {
                        // response.append(identity(myLambda, inputLine));
                        response.append( implementation.identity( inputLine));
                    }
                }
            }
            else {
                while ( ((inputLine = in.readLine()) != null)) {
                    if (lineIndex++ >= firstLine) {
                        // response.append(identity(myLambda, inputLine));
                        response.append( implementation.identity( inputLine));
                    }
                }
            }

            in.close();

            return response.toString();
        } catch (Exception e) {
            System.out.println("connection failed : " + e);
        }

        return "";
    }


}
