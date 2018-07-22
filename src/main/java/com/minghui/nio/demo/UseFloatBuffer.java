package com.minghui.nio.demo;// $Id$

import java.nio.*;

/**
 * 使用FloatBuffer进行数据存取
 * 这里不涉及通道的操作
 * Buffer.put()和Buffer.get()方法分别从缓冲区存取数据
 * buffer.flip就是将position和limit定位到数据起止处，使得数据可读
 */
public class UseFloatBuffer
{
  static public void main( String args[] ) throws Exception {
    FloatBuffer buffer = FloatBuffer.allocate( 10 );

    for (int i=0; i<buffer.capacity(); ++i) {
      float f = (float)Math.sin( (((float)i)/10)*(2*Math.PI) );
      buffer.put( f );
    }

    buffer.flip();

    while (buffer.hasRemaining()) {
      float f = buffer.get();
      System.out.println( f );
    }
  }
}
