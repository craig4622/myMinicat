package com.lagou.demo.classloader;

import java.io.*;

/**
 * @ClassName MyClassloader 自定义类加载器,在目前的资源目录加载不到需要的资源的时候,需要自定义加载器来加载对应目录的资源文件(.class)
 * @Description TODO
 * @Author xsq
 * @Date 2020/4/30 16:21
 **/

public class MyClassloader extends ClassLoader {

    private String path;

    public MyClassloader(String path) {
        this.path = path;
    }

    //用于寻找类文件
    @Override
    public Class findClass(String name) {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }

    public byte[] loadClassData(String name) {
         name = (path +name).replaceAll("\\.","/")+ ".class";
        InputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            in = new FileInputStream(new File(name));
            out = new ByteArrayOutputStream();
            int i = 0;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }
}

