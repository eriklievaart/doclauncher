package main;

import java.io.IOException;

import com.eriklievaart.doclauncher.boot.Main;

public class Run {

	public static void main(String[] args) throws IOException {
		Main.main(new String[] { System.getProperty("user.home") + "/Applications/doclauncher/api", "java11", "map" });
	}
}