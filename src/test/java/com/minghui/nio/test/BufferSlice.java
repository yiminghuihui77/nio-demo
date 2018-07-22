package com.minghui.nio.test;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * 缓冲区其他知识：
 * 分片：从原缓冲区生成子缓冲区(注意：子缓冲区与原缓冲区共享同一个底层数组)
 * 只读缓冲区
 *
 * @author minghui.y
 * @create 2018-07-20 22:10
 **/
public class BufferSlice {

    /**
     * 分片：共享底层同一数组
     */
    @Test
    public void bufferSlice() {
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //向缓冲区中插入数据
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }
        //分片
        buffer.position(3);
        buffer.limit(9);
        ByteBuffer buffer2 = buffer.slice();
        //从分片中获取数据
        for (int i = 0; i < buffer2.capacity(); i++) {
            System.out.println(buffer2.get());
        }
    }

    /**
     * 向只读缓冲区写入数据会抛出ReadOnlyBufferException异常
     */
    @Test
    public void readOnlyBuffer() {
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //向缓冲区中填充数据
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)(i + 1));
        }
        //获取对应的只读缓冲区
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        //尝试向只读缓冲区写入数据
//        readOnlyBuffer.put((byte)11);
        //从只读缓冲区中读取数据
        readOnlyBuffer.flip();
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
    }

}
