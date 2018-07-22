package com.minghui.nio.test;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO实现文件拷贝
 *
 * @author minghui.y
 * @create 2018-07-20 18:43
 **/
public class CopyFile {

    @Test
    public void copyFile() {
        String srcFile = CopyFile.class.getClassLoader().getResource("demo.txt").getFile();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis =  new FileInputStream(srcFile);
            fos = new FileOutputStream("/W:/Git_Repository/nio-demo/target/classes/target.txt", true);
            //获取通道
            FileChannel iChannel = fis.getChannel();
            FileChannel oChannel = fos.getChannel();
            //创建缓冲区
            ByteBuffer  buffer = ByteBuffer.allocate(1024);

            while (true) {
                //清空缓存(实际上position设为0，limit移动到capacity处)
                buffer.clear();
                int hasRead = iChannel.read(buffer);

                if (hasRead == -1) {
                    break;
                }
                //调整position和limit使得缓存区可读
                buffer.flip();
                oChannel.write(buffer);

            }
            System.out.println("文件拷贝完成！");
            //关闭连接，释放资源
            fis.close();
            fos.close();
            iChannel.close();
            oChannel.close();
        } catch (Exception e ){
            e.printStackTrace();
        }

    }


}
