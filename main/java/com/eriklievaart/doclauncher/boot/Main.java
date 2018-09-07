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

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.eriklievaart.doc.IndexCache;
import org.eriklievaart.util.FileUtils;

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
		File classes = new File(dir, "allclasses-noframe.html");
		if (!classes.exists()) {
			JOptionPane.showMessageDialog(null, "Missing file: " + classes);
			return null;
		}
		List<String> lines = FileUtils.readLines(classes);
		String found = JavadocParser.findClassHtmlPath(search, lines);
		return found == null ? index : new File(dir, found);
	}

	private static File selectManually(Map<String, File> index) {
		ArrayList<String> found = new ArrayList<String>(index.keySet());
		Collections.sort(found);
		JList<String> list = new JList<String>(found.toArray(new String[] {}));
		JOptionPane.showMessageDialog(null, new JScrollPane(list));

		if (list.getSelectedIndex() < 0) {
			return null;
		}
		File selected = index.get(list.getSelectedValue());
		return selected;
	}

}
