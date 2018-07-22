package com.minghui.nio.socket;


import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 非阻塞Socket服务端
 * 单线程监听多个通道中事件
 * @author minghui.y
 * @create 2018-07-21 13:36
 **/
public class NonBlockingServer {

    /**
     * 服务端监听的端口号
     */
    private static final int[] LISTEN_PORT = new int[]{8001, 8002, 8003};

    /**
     * 共享的Buffer
     */
    private static final ByteBuffer BUFFER = ByteBuffer.allocate(1024);

    /**
     * 通过监听事件来解放多线程，即单线程即可高效完成工作
     * @param args
     */
    public static void main(String[] args) {
        try {
            //创建一个Selector
            Selector selector = Selector.open();

            //
            for (int i = 0; i < LISTEN_PORT.length; i++) {
                ServerSocketChannel ssc = ServerSocketChannel.open();
                //通道设置为不阻塞
                ssc.configureBlocking(false);
                //检索与当前通道关联的Server Socket
                ServerSocket server = ssc.socket();
                //监听端口
                server.bind(new InetSocketAddress(LISTEN_PORT[i]));

                //注册到Selector上，监听"接收连接"事件
                SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("监听端口号：[" + LISTEN_PORT[i] + "]...");
            }
            //死循环--监听事件
            while (true) {
                //该方法会一直阻塞，直到事件触发
                int num = selector.select();
                //获取事件对应的SelectionKey
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                //遍历key，处理不同的事件
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    //根据key判断时间类型
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        //"接收连接"事假类型
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        //获取连接的客户端通道
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);

                        //监听当前通道的接收事件
                        SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);

                        //移除已经处理过的key(必须移除，否则重复处理；这是使用Iterator的原因)
                        iterator.remove();
                        System.out.println("Connect with [" + sc + "]");
                    } else if ((key.readyOps() & SelectionKey.OP_READ) ==SelectionKey.OP_READ) {
                        //"读取"事件类型
                        SocketChannel sc = (SocketChannel)key.channel();

                        //执行读写
                        int hasRead = 0;
                        int totalRead = 0;
                        StringBuilder builder = new StringBuilder();
                        while (true) {
                            //执行读取
                            BUFFER.clear();
                            hasRead = sc.read(BUFFER);

                            if (hasRead <= 0) {
                                break;
                            }

                            //从Buffer中获取数据(1、拼接成字串打印  2、直接返回给客户端通道)
                            BUFFER.flip();
                            while (BUFFER.hasRemaining()) {
                                builder.append((char)BUFFER.get());
                            }
                            //重新调整position和limit
                            BUFFER.flip();
                            sc.write(BUFFER);
                            totalRead += hasRead;
                        }
                        System.out.println("从[" + sc + "]读取了" + totalRead + "bytes数据：" + builder.toString());
                        //删除key
                        iterator.remove();

                    } else {
                        System.out.println("未知事件类型：" + key.readyOps());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
