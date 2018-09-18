package com.lmq.tomcat.ss;

import java.io.IOException;

public class DoNothingTomcatTest {

	public static void main(String[] args) throws IOException {
		DoNothingTomcat tomcat = new DoNothingTomcat();
		tomcat.start();
	}
}
