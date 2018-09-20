package com.lmq.tomcat.ss;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import com.lmq.tomcat.ss.request.HttpRequest;
import com.lmq.tomcat.ss.response.HttpResponse;

public class DoNothingTomcat {

	private volatile boolean shutdown = false;
	
	public void start() throws IOException {
		ServerSocket ss = new ServerSocket(8080);
		System.out.println("tomcat start");
		while (!this.shutdown) {
			Socket socket = ss.accept();
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			BufferedInputStream bin = new BufferedInputStream(in);
			byte[] buffer = new byte[2048];
			int length = bin.read(buffer);
			if (length >= 0) {
				HttpRequest request = HttpRequest.from(new String(Arrays.copyOf(buffer, length)));
				service(socket, out, request);
			} else {
				socket.close();
			}
		}
		ss.close();
	}

	protected void service(Socket socket, OutputStream out, HttpRequest request) throws IOException {
		doNothing(socket, out, request);
	}

	protected void doNothing(Socket socket, OutputStream out, HttpRequest request) throws IOException {
		System.out.println(request);
		if ("/shutdown".equalsIgnoreCase(request.getUrl())) {
			byte[] body = "shutdown".getBytes();
			HttpResponse response = HttpResponse.ok(body, "text/plain");
			System.out.println(response);
			out.write(response.toBytes());
//					out.write("HTTP/1.1 OK\r\nContent-Type: text/plain\r\nContent-Length: 8\r\n\r\nshutdown".getBytes());
			this.shutdown();
		} else if ("/favicon.ico".equalsIgnoreCase(request.getUrl())) {
			InputStream iconIns = DoNothingTomcat.class.getClassLoader().getResourceAsStream("favicon.ico");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int i = 0;
			byte[] buffer2 = new byte[1024];
			while ((i = iconIns.read(buffer2)) > 0) {
				bos.write(buffer2, 0, i);
			}
			byte[] body = bos.toByteArray();
			HttpResponse response = HttpResponse.ok(body, "image/x-icon");
			System.out.println(response);
			out.write(response.toBytes());
//					out.write("HTTP/1.1 200 OK\r\nContent-Type: image/x-icon\r\nContent-Length: 1150\r\n\r\n".getBytes());
		} else {
			byte[] body = "OK".getBytes();
			HttpResponse response = HttpResponse.ok(body, "text/plain");
			System.out.println(response);
			out.write(response.toBytes());
//					out.write("HTTP/1.1 OK\r\nContent-Type: text/plain\r\nContent-Length: 2\r\n\r\nOK".getBytes());
		}
		socket.close();
	}
	
	public void shutdown() {
		this.shutdown = true;
	}
}
