package org.eriklievaart.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static List<String> readLines(File classes) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(classes));

		List<String> lines = new ArrayList<String>();
		try {
			String line = in.readLine();
			while (line != null) {
				lines.add(line);
				line = in.readLine();
			}
		} finally {
			in.close();
		}
		return lines;
	}
}
