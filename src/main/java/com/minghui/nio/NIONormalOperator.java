package com.minghui.nio;

import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author minghui.y
 * @create 2018-07-18 16:59
 **/
public class NIONormalOperator {

    public static void main(String[] args) {
        RandomAccessFile accessFile = null;
        try {
            URL fileUrl = NIONormalOperator.class.getClassLoader().getResource("demo.txt");
            accessFile = new RandomAccessFile(fileUrl.getFile(), "rw");
            //获取channel
            FileChannel fileChannel = accessFile.getChannel();
            //创建字节缓存区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int hasRead = 0;
            StringBuilder builder = new StringBuilder();
            while ((hasRead = fileChannel.read(byteBuffer)) != -1) {
                //filp方法：调整position和limit方法，使得数据可读
                byteBuffer.flip();

                //Tells whether there are any elements between the current position and the limit.
                while (byteBuffer.hasRemaining()) {
                    //从buffer中读取一个字节
                    builder.append((char)byteBuffer.get());
                }

                //compact方法：将所有未读的数据拷贝到Buffer起始处
                byteBuffer.compact();
            }
            System.out.println(builder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接释放资源
            try {
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
