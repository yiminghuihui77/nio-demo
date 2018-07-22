package com.minghui.nio.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞式Socket服务端
 * task：只负责接收并打印客户端发来的信息
 * @author minghui.y
 * @create 2018-07-18 18:20
 **/
public class BlockingServer {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        ServerSocket server = null;
        Socket client = null;
        BufferedReader reader = null;
        try {
            server = new ServerSocket(SERVER_PORT);
            System.out.println("服务端启动...");
            //服务端保持接收所有客户端请求
            while (true) {
                client = server.accept();
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                //打印客户端信息
                System.out.println("客户端地址：" + client.getRemoteSocketAddress());
                //针对一个服务端保持交互
                String clientMsg = null;
                while (true) {
                    clientMsg = reader.readLine();
                   /* if(clientMsg == null) {
                        //说明连接已经断开
                        break;
                    }*/
                    System.out.println("服务端接收到信息：" + clientMsg);
                    //判断客户端信息是否为exit
                    if ("exit".equalsIgnoreCase(clientMsg)) {
                        break;
                    }
                }
                //服务端断开与客户端连接
                System.out.println("服务端断开与客户端的连接");
                client.close();
                server.close();
                reader.close();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
