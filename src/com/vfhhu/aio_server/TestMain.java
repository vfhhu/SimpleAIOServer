package com.vfhhu.aio_server;

import java.net.InetSocketAddress;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InetSocketAddress addr = new InetSocketAddress(80);	
		TestAioServer as=TestAioServer.getInstance(addr);
		as.start();
	}

}
