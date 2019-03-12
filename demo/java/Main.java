package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Main {

    public static void main(String[] args){
        String publicKey = "wb55j-jrdw8-byghj-n8b8y-zcc3y-r88z9-i7ykm";
        String privateKey = "zx6pA-hXm7/-sBg~z-nCiP4-[Dr;H-[Uk25-mSOkK";

        String url = "https://api.coinegg.im/api/v1/balance/";

        String params = "key=" + publicKey;

        String signature = hmac_sha256(params, MD5(privateKey));

        params += "&signature=" + signature;

        String result = postRequest(url, params);

        System.out.println(result);
    }

    /**
     * hmac_sha256加密
     *
     * @param value
     * @param key
     * @return
     */
    private static String hmac_sha256(String value, String key) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            String hexBytes = byte2hex(rawHmac);
            return hexBytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String byte2hex(final byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0xFF));
            if (stmp.length() == 1) hs = hs + "0" + stmp;
            else hs = hs + stmp;
        }
        return hs;
    }


    /**
     * 发送http请求 获取结果
     *
     * @param requestUrl 请求的地址
     * @param params     post的参数
     * @return 返回的结果
     */
    private static String postRequest(String requestUrl, String params) {

        String result = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            DataOutputStream dop = new DataOutputStream(
                    connection.getOutputStream());
            if (params != null) {
                dop.writeBytes(params);
            }
            dop.flush();
            dop.close();
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    private static String MD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
