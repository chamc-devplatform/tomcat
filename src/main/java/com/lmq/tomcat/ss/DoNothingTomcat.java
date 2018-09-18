package com.lmq.tomcat.ss;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

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
				System.out.println(new String(Arrays.copyOf(buffer, length)));
			}
			out.write("HTTP/1.1 OK\r\nContent-Type: text/plain\r\nContent-Length: 2\r\n\r\nOK".getBytes());
			in.close();
			out.close();
			socket.close();
		}
		ss.close();
	}
}
