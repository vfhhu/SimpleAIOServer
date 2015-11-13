package com.vfhhu.lib.aio_server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AIOSocket {
	private AsynchronousSocketChannel socket;
	private long ID=0;
	public AIOSocket(AsynchronousSocketChannel socket){
		this.socket=socket;
		try {
			ID=System.currentTimeMillis()+socket.getRemoteAddress().hashCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ID=System.currentTimeMillis()+socket.toString().hashCode();
		}
		
	}
	public void send(String data){
		ByteBuffer bb = ByteBuffer.wrap( data.getBytes() );
		socket.write(bb);
	}
	public AsynchronousSocketChannel getSocket() {
		return socket;
	}
	public long getID() {
		return ID;
	}
	
}
