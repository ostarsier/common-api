package cn.xianbin.commonapi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class MD5Util {

    /**
     * MD5加密
     *
     * @param src     需要加密的字符串
     * @param isUpper 大小写
     * @param bit     加密长度（16,32,64）
     * @return
     */
    public static String getMD5(String src, boolean isUpper, Integer bit) {
        String md5 = "";
        try {
            // 创建加密对象
            MessageDigest md = MessageDigest.getInstance("md5");
            if (bit == 64) {
                Base64.Encoder encoder = Base64.getEncoder();
                md5 = encoder.encodeToString(md.digest(src.getBytes(StandardCharsets.UTF_8)));
            } else {
                // 计算MD5函数
                md.update(src.getBytes(StandardCharsets.UTF_8));
                byte b[] = md.digest();
                int i;
                StringBuffer buffer = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buffer.append("0");
                    buffer.append(Integer.toHexString(i));
                }
                md5 = buffer.toString();
                if (bit == 16) {
                    String md16 = md5.substring(8, 24);
                    md5 = md16;
                    if (isUpper) {
                        md5 = md5.toUpperCase();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isUpper) {
            md5 = md5.toUpperCase();
        }
        return md5;
    }
    public static String getMD5(String src){
        return getMD5(src, true, 64);
    }
}
