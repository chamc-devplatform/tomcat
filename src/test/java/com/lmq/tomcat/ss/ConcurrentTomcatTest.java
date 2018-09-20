package com.lmq.tomcat.ss;

import java.io.IOException;

public class ConcurrentTomcatTest {

	public static void main(String[] args) throws IOException {
		new ConcurrentTomcat().start();
	}
}
