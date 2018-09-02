package org.eriklievaart.doc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Indexer {
	private static final String MARKER_FILE_NAME = "allclasses-frame.html";
	private final Logger log = Logger.getLogger(getClass().getName());

	private final Map<String, File> index = new Hashtable<String, File>();
	private final File root;

	public Indexer(File root) throws IOException {
		this.root = root;
		if (!root.isDirectory()) {
			throw new IOException("`" + root.getPath() + "` is not a directory");
		}
	}

	public Map<String, File> index() {
		for (File file : root.listFiles()) {
			indexJavadoc(file);
		}
		validate();
		return index;
	}

	private void validate() {
		List<String> missing = new ArrayList<>();
		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				missing.add(file.getAbsolutePath());
			}
		}
		removeIndexedRoots(missing);
		if (!missing.isEmpty()) {
			notify("Missing " + MARKER_FILE_NAME + " for " + missing.get(0));
		}
	}

	private void removeIndexedRoots(List<String> roots) {
		for (File found : index.values()) {
			for (int i = 0; i < roots.size(); i++) {
				if (found.getAbsolutePath().startsWith(roots.get(i))) {
					roots.remove(i);
				}
			}
		}
	}

	private void indexJavadoc(File dir) {
		System.out.println("Indexing: " + dir);
		if (dir.isFile()) {
			return;
		}
		for (File child : dir.listFiles()) {
			if (child.isFile() && child.getName().equals(MARKER_FILE_NAME)) {
				if (index.containsKey(dir.getName())) {
					log.info("duplicate: " + index.get(dir.getName()));
					log.info("duplicate: " + child.toString());
					notify(String.format("duplicate entry %s containing %s", dir.getName(), MARKER_FILE_NAME));
				}
				index.put(dir.getName(), new File(dir, "index.html"));
				return;
			}
			if (child.isDirectory()) {
				indexJavadoc(child);
			}
		}
	}

	private void notify(String message) {
		log.info(message);
		JOptionPane.showMessageDialog(null, message);
	}
}
