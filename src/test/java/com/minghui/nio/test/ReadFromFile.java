package com.minghui.nio.test;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 分别使用IO与NIO从文件中读取
 *
 * @author minghui.y
 * @create 2018-07-20 18:08
 **/
public class ReadFromFile {

    /**
     * 使用IO读取文件
     */
    @Test
    public void readFromFileWithIO() {
        InputStream is = null;
        try {
            String filePath = ReadFromFile.class.getClassLoader().getResource("demo.txt").getFile();
            is = new FileInputStream(filePath);
            //开始读取
            int hasRead = 0;
            byte[] buffer = new byte[1024];
            StringBuilder builder = new StringBuilder();
            while ((hasRead = is.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, hasRead));
            }
            System.out.println(builder.toString());
            //关闭流释放资源
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 使用NIO读取文件
     */
    @Test
    public void readFromFileWithNIO() {
        FileInputStream fis = null;
        try {
            String filePath = ReadFromFile.class.getClassLoader().getResource("demo.txt").getFile();
            fis = new FileInputStream(filePath);
            //获取渠道
            FileChannel channel = fis.getChannel();
            //创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //开始读取
            int hasRead = 0;
            StringBuilder builder = new StringBuilder();
            while ((hasRead = channel.read(buffer)) != -1) {
                //调整position和limit，使得数据位于二者之间
                buffer.flip();
                //判断position和limit之间是否还有数据未读
                while (buffer.hasRemaining()) {
                    builder.append((char)buffer.get());
                }
                //将所有未读数据移动到起始处
                buffer.compact();
            }
            System.out.println(builder.toString());

            //关闭连接释放资源
            fis.close();
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
