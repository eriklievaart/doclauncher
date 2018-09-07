package com.eriklievaart.doclauncher.boot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavadocParser {

	public static String findClassHtmlPath(String search, List<String> lines) {
		String regex = ".*<A\\s++HREF\\s*+=\\s*+\"([^\"]++)[^>]++>(?:<[^<>]++>)*([^<]++)(?:</[^<>]++>)*</A>++.*+";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		for (String line : lines) {
			Matcher matcher = pattern.matcher(line);

			if (matcher.matches()) {
				String found = matcher.group(2).trim();
				if (found.equalsIgnoreCase(search)) {
					return matcher.group(1);
				}
			}
		}
		return null;
	}
}
