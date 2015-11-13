package test;


import java.net.InetSocketAddress;

import com.vfhhu.lib.aio_server.AIOServer;
import com.vfhhu.lib.aio_server.AIOSocket;

public class TestAioServer extends AIOServer{
	private static TestAioServer aio;
	public TestAioServer( InetSocketAddress address ) {
		super( address );
		//CustDataMap=new HashMap<Integer,CustData>();
	}
	public static TestAioServer getInstance(InetSocketAddress address){
		if(aio==null){
			aio=new TestAioServer(address);
		}
		return aio;
	}
	public static TestAioServer getInstance(){
		return aio;
	}
	@Override
	public void onConnect(AIOSocket conn) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClose(AIOSocket conn) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onData(AIOSocket conn, String data) {
		// TODO Auto-generated method stub
		
	}
}
