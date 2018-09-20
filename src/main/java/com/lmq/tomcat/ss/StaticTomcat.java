package com.lmq.tomcat.ss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.lmq.tomcat.ss.request.HttpRequest;
import com.lmq.tomcat.ss.response.HttpResponse;

public class StaticTomcat extends DoNothingTomcat {

	private static final String TEMPLATES = "templates";
	private static final String TEXT_HTML = "text/html";
	private static final String CONTEXT_PATH = "/java";

	@Override
	protected void service(Socket socket, OutputStream out, HttpRequest request) throws IOException {
		String url = request.getUrl();
		int index = url.indexOf(CONTEXT_PATH);
		if (index > -1) {
			String path = url.substring(index + CONTEXT_PATH.length());
			InputStream in = StaticTomcat.class.getClassLoader().getResourceAsStream(TEMPLATES + path);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = in.read(buffer)) > -1) {
				bos.write(buffer, 0, length);
			}
			HttpResponse response = HttpResponse.ok(bos.toByteArray(), TEXT_HTML);
			out.write(response.toBytes());
			socket.close();
		} else {
			super.service(socket, out, request);
		}
	}

	
}
