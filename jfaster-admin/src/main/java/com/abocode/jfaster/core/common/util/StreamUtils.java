package com.abocode.jfaster.core.common.util;


import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class StreamUtils {

    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转换成String
     *
     * @param in InputStream
     * @return String
     * @throws Exception
     */
    public static String inputStreamToStr(InputStream in) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        String string = null;
        int count = 0;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try {
            string = new String(outStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return string;
    }

    /**
     * 将String转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream strToInputStream(String in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
        return is;
    }


    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        return outStream.toByteArray();
    }
}