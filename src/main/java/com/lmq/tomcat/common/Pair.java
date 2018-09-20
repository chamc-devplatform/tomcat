package com.lmq.tomcat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Pair<F, S> {

	private F first;
	private S second;
	
	public static <F, S> Pair<F, S> of(F first, S second) {
		return new Pair<>(first, second);
	}
	
	public static Pair<String, String> of(String str) {
		String[] ss = str.split(":");
		return new Pair<>(ss[0].trim(), ss[1].trim());
	}
}
