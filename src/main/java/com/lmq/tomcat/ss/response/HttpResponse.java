package com.lmq.tomcat.ss.response;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <img src="http://s1.51cto.com/images/20180426/1524748488887423.png?x-oss-process=image/watermark,size_16,text_QDUxQ1RP5Y2a5a6i,color_FFFFFF,t_100,g_se,x_10,y_10,shadow_90,type_ZmFuZ3poZW5naGVpdGk=" ></img>
 * @author luomq
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor @ToString(exclude = "body")
public class HttpResponse {

	private static final String OK = "OK";
	private static final String HTTP_1_1 = "HTTP/1.1";
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String CRLF = "\r\n";
	private String protocol;
	private int status;
	private String message;
	private Map<String, String> headers;
	
	private byte[] body;
	
	public byte[] toBytes() {
		String statusLine = this.protocol + " " + this.status + " " + this.message;
		StringBuilder headerLines = new StringBuilder(CRLF);
		headers.forEach((key, value) -> headerLines.append(key).append(": ").append(value).append(CRLF));
		headerLines.append(CRLF);
		byte[] statusLineBytes = statusLine.getBytes();
		byte[] headerLinesBytes = headerLines.toString().getBytes();
		int contentLength = statusLineBytes.length + headerLinesBytes.length + this.body.length;
		byte[] bytes = new byte[contentLength];
		System.arraycopy(statusLineBytes, 0, bytes, 0, statusLineBytes.length);
		System.arraycopy(headerLinesBytes, 0, bytes, statusLineBytes.length, headerLinesBytes.length);
		System.arraycopy(this.body, 0, bytes, statusLineBytes.length + headerLinesBytes.length, this.body.length);
		return bytes;
	}
	
	public static HttpResponse ok(byte[] body, String contentType) {
		Map<String, String> headers = new HashMap<>(2);
		headers.put(CONTENT_TYPE, contentType);
		headers.put(CONTENT_LENGTH, String.valueOf(body.length));
		HttpResponse response = HttpResponse.builder()
				.status(200)
				.protocol(HTTP_1_1)
				.message(OK)
				.headers(headers)
				.body(body)
				.build();
		return response;
	}
}
