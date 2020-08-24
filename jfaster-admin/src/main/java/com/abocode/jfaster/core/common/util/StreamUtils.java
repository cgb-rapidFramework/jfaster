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
    public static String inputStreamTOString(InputStream in) {

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
     * 将InputStream转换成某种字符编码的String
     *
     * @param in
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding) {
        String string = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try {
            string = new String(outStream.toByteArray(), encoding);
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
    public static InputStream StringTOInputStream(String in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
        return is;
    }

    /**
     * 将String转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] StringTObyte(String in) {
        byte[] bytes = null;
        try {
            bytes = InputStreamTOByte(StringTOInputStream(in));
        } catch (IOException e) {
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return bytes;
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        return outStream.toByteArray();
    }


    /**
     * 将byte数组转换成InputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static InputStream byteTOInputStream(byte[] in) {

        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }

    /**
     * 将byte数组转换成String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String byteTOString(byte[] in) {

        InputStream is = null;
        try {
            is = byteTOInputStream(in);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return InputStreamTOString(is, "UTF-8");
    }

    /**
     * 将byte数组转换成String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String getString(String in) {

        String is = null;
        try {
            is = byteTOString(StringTObyte(in));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return is;
    }


    public static File getFile(String filepath) {
        return new File(filepath);
    }

}