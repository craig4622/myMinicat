package com.lagou.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ClassName StaticAbsolutePathUtil
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/27 10:58
 **/
public class StaticAbsolutePathUtil {

    /**
     * 获取静态文件绝对路径
     *
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticAbsolutePathUtil.class.getResource("/").getPath();
        absolutePath = absolutePath.replaceAll("\\\\", "/") + path;
        return absolutePath;
    }

    /**
     * 读取静态文件并用输出流输出
     *
     * @param inputStream
     * @param outputStream
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        int resourcesize = count;
        //先输出请求头信息
        outputStream.write(HttpProtocolUtil.getHttpHeaderInfo200(resourcesize).getBytes());
        int written = 0; //已经读取的数据长度
        int byteSize = 1024;//计划缓冲数据的长度
        byte[] bytes = new byte[byteSize];
        while (written < resourcesize) {
            if (byteSize + written > resourcesize) {//说明剩余读取的数据长度不足1024,就按真实数据长度读取
                byteSize = resourcesize - written;
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);//数据读取到字节中
            outputStream.write(bytes);//输出数据
            outputStream.flush();
            written += byteSize;
        }
    }
}
