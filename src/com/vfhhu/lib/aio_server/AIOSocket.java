package com.vfhhu.lib.aio_server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AIOSocket {
	private AsynchronousSocketChannel socket;
	public AIOSocket(AsynchronousSocketChannel socket){
		this.socket=socket;
	}
	public void send(String data){
		ByteBuffer bb = ByteBuffer.wrap( data.getBytes() );
		socket.write(bb);
	}
	
}
