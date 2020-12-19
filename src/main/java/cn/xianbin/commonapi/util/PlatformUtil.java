package cn.xianbin.commonapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class PlatformUtil {

    /**
     * @param request
     * @return java.lang.String
     * @throws
     * @description: TODO 获取用户IP地址
     * @author HANJUN
     * @date 2020/3/5 18:33
     */
    public static String getIPByrequest(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    //返回客户端浏览器的版本号、类型
    public static String getBrows(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent").toLowerCase();
        String browser = "unknown";
        String version = "unknown";
        String msieP = "msie ([\\d.]+)";
        String firefoxP = "firefox\\/([\\d.]+)";
        String chromeP = "chrome\\/([\\d.]+)";
        String operaP = "opera.([\\d.]+)";
        String safariP = "version\\/([\\d.]+).*safari";

        Pattern pattern = Pattern.compile(msieP);
        Matcher mat = pattern.matcher(ua);
        if (mat.find()) {
            browser = mat.group();
            version = browser.split(" ")[1];
        }
        pattern = Pattern.compile(firefoxP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            browser = mat.group();
            version = browser.split("/")[1];
        }
        pattern = Pattern.compile(chromeP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            browser = mat.group();
            version = browser.split("/")[1];
        }
        pattern = Pattern.compile(operaP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            browser = mat.group();
            version = browser.split("\\.")[1];
        }
        pattern = Pattern.compile(safariP);
        mat = pattern.matcher(ua);
        if (mat.find()) {
            browser = mat.group();
            version = browser.split("/")[1].split(".")[0];
        }
        return String.format("%s/%s", browser, version);
    }

}
