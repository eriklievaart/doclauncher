package com.eriklievaart.doclauncher.boot;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.eriklievaart.doclauncher.doc.IndexCache;
import com.eriklievaart.doclauncher.util.FileUtils;

// TODO:
// allow fuzzy match
// check timestamp (< 30 days?) => create index if older or does not exist
public class Main {
	public static void main(String[] args) throws IOException {

		if (args.length < 2) {
			JOptionPane.showMessageDialog(null, "Usage: doc [root-dir] [library] [class-name]?");
			return;
		}
		String root = args[0];
		String library = args[1];
		String cls = args.length > 2 ? args[2] : null;

		print("Looking for: " + Arrays.toString(args));
		showJavadoc(root, library, cls);
	}

	private static void print(String msg) {
		System.out.println(new Date() + " => " + msg);
	}

	private static void showJavadoc(String path, String library, String cls) throws IOException {
		Map<String, File> index = new IndexCache(new File(path)).getIndexFor(library);

		File indexHtml = index.get(library);
		if (indexHtml == null) {
			indexHtml = selectManually(index);
		}
		print("Index: " + indexHtml);
		openClassOrIndex(indexHtml, cls);
	}

	private static void openClassOrIndex(File index, String cls) throws IOException {
		if (index == null) {
			return;
		}
		File show = index;
		if (cls != null) {
			show = findClassHtml(index, cls);
		}
		print("Opening: " + show);
		if (show != null) {
			Desktop.getDesktop().open(show);
		}
	}

	private static File findClassHtml(File index, String search) throws IOException {
		File dir = index.getParentFile();
		Optional<File> optional = getAllClassesFile(dir);
		if (optional.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Missing allclasses file, see console output for more details");
			return null;
		}
		List<String> lines = FileUtils.readLines(optional.get());
		String found = JavadocParser.findClassHtmlPath(search, lines);
		return found == null ? index : new File(dir, found);
	}

	private static Optional<File> getAllClassesFile(File dir) {
		String[] names = new String[] { "allclasses-noframe.html", "allclasses.html" };
		for (String name : names) {
			File classes = new File(dir, name);
			if (classes.exists()) {
				return Optional.of(classes);
			} else {
				System.err.println("not found: " + classes);
			}
		}
		return Optional.empty();
	}

	private static File selectManually(Map<String, File> index) {
		ArrayList<String> found = new ArrayList<>(index.keySet());
		Collections.sort(found);
		JList<String> list = new JList<>(found.toArray(new String[] {}));
		JOptionPane.showMessageDialog(null, new JScrollPane(list));

		if (list.getSelectedIndex() < 0) {
			return null;
		}
		File selected = index.get(list.getSelectedValue());
		return selected;
	}
}
