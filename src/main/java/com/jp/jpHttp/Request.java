package com.jp.jpHttp;

import java.io.IOException;
import java.io.InputStream;

/**
 * 创建Request类，获取HTTP的请求头所有信息并截取URL地址返回
 *
 */
public class Request {
	private InputStream is;
	private String url;

	public Request(InputStream input) {
		this.is = input;
	}

	public void parse() {
		// 从socket中读取一个2048长度字符
		StringBuffer request = new StringBuffer(Response.BUFFER_SIZE);
		int i;
		byte[] buffer = new byte[Response.BUFFER_SIZE];
		try {
			i = is.read(buffer);//从输入流is读取一定数量的字节，并存储到buffer字节数组中
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}
		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);//把buffer字符数组中的字节拼成字符串 request
		}
		// 打印读取的socket中的内容
		System.out.println("打印socket的输入流中的内容");
		System.out.print(request.toString());//打印字符串request，就是socket里的输入流，即来自客户端的内容
		url = parseUrL(request.toString()); //从字符串request中抽取出请求路径
	}

	//从字符串request中抽取出请求路径的函数
	private String parseUrL(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');// 看socket获取请求头是否有值
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1)
				System.out.println();
				System.out.println("获取到请求文件的路径（url）" + requestString.substring(index1 + 1, index2));
				return requestString.substring(index1 + 1, index2);
		}
		return null;
	}

	public String getUrL() {
		return url;
	}

}