package com.minghui.nio.demo;// $Id$

import java.nio.ByteBuffer;

/**
 * 注意：指定类型存储后，必须按照存储的顺序读取，否则数据不一致
 */
public class TypesInByteBuffer
{
  static public void main( String args[] ) throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate( 64 );

    buffer.putInt( 30 );
    buffer.putLong( 7000000000000L );
    buffer.putDouble( Math.PI );

    buffer.flip();

    System.out.println( buffer.getInt() );
    System.out.println( buffer.getLong() );
    System.out.println( buffer.getDouble() );
  }
}
