package com.lmq.tomcat.ss.request;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.lmq.tomcat.common.Pair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <img src="https://upload-images.jianshu.io/upload_images/2964446-fdfb1a8fce8de946.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"></img>
 * @author luomq
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HttpRequest {

	private Method method;
	private String url;
	private String protocol;
	private Map<String, String> headers;
	
	public static HttpRequest from(String requestContent) {
		String[] contents = requestContent.split("\r\n");
		String[] requestLine = contents[0].split(" ");
		String[] headers = Arrays.copyOfRange(contents, 1, contents.length - 1);
		HttpRequest request = HttpRequest.builder()
				.method(Method.valueOf(requestLine[0]))
				.url(requestLine[1])
				.protocol(requestLine[2])
				.headers(Stream.of(headers).map(Pair::of).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))
				.build();
		return request;
	}
}
