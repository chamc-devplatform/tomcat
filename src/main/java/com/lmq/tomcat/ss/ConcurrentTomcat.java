package com.lmq.tomcat.ss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmq.tomcat.ss.request.HttpRequest;
import com.lmq.tomcat.ss.response.HttpResponse;

public class ConcurrentTomcat extends DoNothingTomcat {

	private static final int THREADS = 10;
	private static final String TEMPLATES = "lte";
	private static final String TEXT_HTML = "text/html";
	private static final String CONTEXT_PATH = "/lte";
	
	private Executor executor;
	
	public ConcurrentTomcat() {
		this.executor = Executors.newFixedThreadPool(THREADS);
	}

	@Override
	protected void service(Socket socket, OutputStream out, HttpRequest request) throws IOException {
		System.out.println(request);
		this.executor.execute(new RequestHanlder(socket, out, request));
//		this.serviceOnce(socket, out, request);
	}
	
	private class RequestHanlder implements Runnable {
		
		private Socket socket;
		private OutputStream out;
		private HttpRequest request;
		
		public RequestHanlder(Socket socket, OutputStream out, HttpRequest request) {
			this.socket = socket;
			this.out = out;
			this.request = request;
		}
		
		@Override
		public void run() {
			try {
				ConcurrentTomcat.this.serviceOnce(socket, out, request);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void serviceOnce(Socket socket, OutputStream out, HttpRequest request) throws IOException {
		String url = request.getUrl();
		int index = url.indexOf(CONTEXT_PATH);
		if (index > -1) {
			String path = url.substring(index + CONTEXT_PATH.length());
			int index2 = path.indexOf('?');
			if (index2 > -1) {
				path = path.substring(0, index2);
			}
			InputStream in = ConcurrentTomcat.class.getClassLoader().getResourceAsStream(TEMPLATES + path);
			if (Objects.nonNull(in)) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) > -1) {
					bos.write(buffer, 0, length);
				}
				HttpResponse response = HttpResponse.ok(bos.toByteArray(), getContentTypeByPath(path));
				System.out.println(response);
				out.write(response.toBytes());
			}
			socket.close();
		} else {
			ConcurrentTomcat.this.doNothing(socket, out, request);
		}
	}
	
	private String getContentTypeByPath(String path) {
		int index = path.indexOf('?');
		if (index > -1) {
			path = path.substring(0, index);
		}
		index = path.lastIndexOf('.');
		String suffix = path;
		if (index > -1) {
			suffix = suffix.substring(index + 1);
		} else {
			return "img/png";
		}
		switch (suffix) {
		case "js":
			return "application/javascript";
		case "css":
			return "text/css";
		case "woff2":
			return "font/woff2";
		case "ttf":
			return "font/ttf";
		}
		return TEXT_HTML;
	}

}
