package com.jp.jpHttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HttpServer核心处理类，用于接受用户请求，传递HTTP请求头信息，关闭容器
 * 
 */
public class HttpServer {
	// 用于判断是否需要关闭容器
	private boolean shutdown = false;

	public void acceptWait() {
		ServerSocket serverSocket = null;
		try {
			/**
			 * serverSocket的三个参数
			 * TCP端口号：0-65535，端口号 0 在所有空闲端口上创建套接字
			 * 最大连接数：传入连接指示（对连接的请求）的最大队列长度被设置为 backlog 参数。如果队列满时收到连接指示，则拒绝该连接。
			 * ip地址： 参数可以在 ServerSocket 的多宿主主机 (multi-homed host) 上使用，ServerSocket 仅接受对其地址之一的连接请求。如果 bindAddr 为 null，则默认接受任何/所有本地地址上的连接
			 */
			serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// 等待用户发请求
		while (!shutdown) {
			try {
				Socket socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				// 接受请求参数
				Request request = new Request(is);
				request.parse();
				
				// 创建用于返回浏览器的对象
				Response response = new Response(os);
				response.setRequest(request);
				response.sendStaticResource();
				
				// 关闭一次请求的socket,因为http请求就是采用短连接的方式
				socket.close();
				System.out.println("服务端的serverSocket关闭");
				// 如果请求地址是/shutdown 则关闭容器
				if (null != request) {
					shutdown = request.getUrL().equals("/shutdown");
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		server.acceptWait();
	}
}