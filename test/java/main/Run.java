package main;

import java.io.IOException;

import com.eriklievaart.doclauncher.boot.Main;

public class Run {

	public static void main(String[] args) throws IOException {
		Main.main(new String[] { "/links/api", "java8", "map" });
	}
}
