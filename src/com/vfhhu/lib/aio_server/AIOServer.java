package com.vfhhu.lib.aio_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AIOServer {
	private static AIOServer as;
	private static Charset charset = Charset.forName("UTF-8");
    private static CharsetEncoder encoder = charset.newEncoder();
    private static CharsetDecoder decoder = charset.newDecoder();
    
    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel server;
	public AIOServer( InetSocketAddress address ) {
		try{
			int Processors=Runtime.getRuntime().availableProcessors();
			group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(Processors*4));
			server = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress("0.0.0.0", address.getPort()));
		}catch(Exception e){e.printStackTrace();}
	}
	public void start(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{					
					server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
						@Override
						public void completed(AsynchronousSocketChannel result, Void attachment) {
							server.accept(null, this); // 接受下一个连接
							startRead(result);							
						}
						@Override
						public void failed(Throwable exc, Void attachment) {
							//System.err.println("start run failed ");
							exc.printStackTrace();
						}
					});
					group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
				}catch(Exception e){}
			}
			
		}).start();
	}	
	
	public void startRead(final AsynchronousSocketChannel socket) { 	
		final AIOSocket aios=new AIOSocket(socket);
        ByteBuffer clientBuffer = ByteBuffer.allocate(1024); 
        socket.read(clientBuffer, clientBuffer, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer buf) {
				if (result > 0) { 
		            buf.flip(); 
		            
		            try { 
		            	String data=decoder.decode(buf).toString();
		                //System.out.println("收到" + socket.getRemoteAddress().toString() + "的消息:" + data); 
		                //System.out.println(Thread.currentThread().getName());
		                buf.compact(); 
		                onData(aios,data);
		            } catch (CharacterCodingException e) { 
		            	System.err.println("socket.read CharacterCodingException");
		                e.printStackTrace(); 
		            } catch (IOException e) { 
		            	System.err.println("socket.read IOException");
		                e.printStackTrace(); 
		            } 
		            //buf.clear();
		            socket.read(buf, buf, this); 
		            //socket.write(clientBuffer);
		        } else if (result == -1) { 
		        	onClose(aios);
//		            try { 
//		            	onClose(aios);
//		                System.out.println("客户端断线:" + socket.getRemoteAddress().toString()); 
//		                buf = null; 
//		            } catch (IOException e) { 
//		            	//System.err.println("socket.read IOException result == -1");
//		                e.printStackTrace(); 
//		            } 
		        } 
			}
			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				// TODO Auto-generated method stub
				System.err.println("socket.read failed ");
			}
		});
        
        onConnect(aios);
       
    } 
	public abstract void onConnect( AIOSocket conn);
	public abstract void onClose( AIOSocket conn);
	public abstract void onData( AIOSocket conn,String data);
    public String bin2hexstr(byte[] src) {
       return bin2hexstr(src, 0, src.length);
    }
 
    public String bin2hexstr(byte[] src, int start, int len) {
       char[] hex = new char[2];
       StringBuffer strBuffer = new StringBuffer(len * 2);
       int abyte;
       for (int i = start; i < start + len; i++) {
           abyte = src[i] < 0 ? 256 + src[i] : src[i];
           hex[0] = HEX[abyte / 16];
           hex[1] = HEX[abyte % 16];
           strBuffer.append(hex);
       }
       return strBuffer.toString();
    }
 
    public final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

}
