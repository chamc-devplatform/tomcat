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
 * <img src="http://s1.51cto.com/images/20180426/1524747772856125.png?x-oss-process=image/watermark,size_16,text_QDUxQ1RP5Y2a5a6i,color_FFFFFF,t_100,g_se,x_10,y_10,shadow_90,type_ZmFuZ3poZW5naGVpdGk="></img>
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
