package com.minghui.nio.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 非阻塞的Socket客户端
 * task：客户端只负责向服务端发送信息
 * 测试方法：分别修改SERVER_PORT为8001、8001、8003启动三个客户端
 * @author minghui.y
 * @create 2018-07-18 17:57
 **/
public class NonBlockingClient {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8003;

    public static void main(String[] args) {

        SocketChannel channel = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        BufferedReader reader = null;

        System.out.println("客户端[" + SERVER_PORT + "]启动...");
        try {
            //渠道准备
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            //读取键盘输入准备
            reader = new BufferedReader(new InputStreamReader(System.in));
            String inputMsg = null;
            //用于拼接服务端返回数据
            StringBuilder builder = new StringBuilder();
            //渠道连接完毕（该方法一致阻塞直到连接建立）
            if (channel.finishConnect()) {

                System.out.println("请输入信息：");
                while ((inputMsg = reader.readLine()) != null) {
                    //用户输入>>>通道
                    byteBuffer.clear();
                    //数据写入buffer
                    byteBuffer.put(inputMsg.getBytes());
                    byteBuffer.flip();
                    //buffer中的数据写入到渠道中
//                    while (byteBuffer.hasRemaining()) {
                        channel.write(byteBuffer);
//                    }

                    //通道>>>读取服务端返回的数据
                    //拼接前清空
                    builder.delete(0, builder.length());
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        builder.append((char)byteBuffer.get());
                    }
                    System.out.println("接收到服务端返回：" + builder.toString());


                    //当用户输入exit时退出
                    if ("exit".equalsIgnoreCase(inputMsg)) {
                        System.out.println("关闭连接中...");
                        break;
                    }
                    System.out.println("请输入信息：");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(channel != null) {
                    channel.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
