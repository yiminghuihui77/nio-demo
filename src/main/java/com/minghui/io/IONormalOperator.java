package com.minghui.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * 常规IO操作
 * @author minghui.y
 * @create 2018-07-18 12:30
 **/
public class IONormalOperator {

    public static void main(String[] args) {
        InputStream is = null;
        try {
            //获取类路径下的资源文件URL
            URL fileUrl = IONormalOperator.class.getClassLoader().getResource("demo.txt");
            System.out.println(fileUrl.getFile() + "------------------绝对路径");

            is = new BufferedInputStream(new FileInputStream(fileUrl.getFile()));
            int hasRead = 0;
            byte[] buffer = new byte[1024];
            StringBuilder builder = new StringBuilder();
            //执行读取
            while((hasRead = is.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, hasRead));
            }
            System.out.println(builder.toString());

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
