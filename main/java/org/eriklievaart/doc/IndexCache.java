package org.eriklievaart.doc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eriklievaart.util.SerializableIO;

public class IndexCache {

	private static final long ONE_DAY = 24 * 60 * 60 * 1000;

	private final File docRoot;
	private final File cacheFile;
	private final SerializableIO io;

	public IndexCache(File docRoot) {
		this.docRoot = docRoot;
		cacheFile = new File(docRoot, "doclauncher-index.dat");
		io = new SerializableIO(cacheFile);
	}

	public Map<String, File> getIndexFor(String library) throws IOException {
		boolean upToDate = System.currentTimeMillis() - cacheFile.lastModified() < ONE_DAY;

		Map<String, File> stored = getStoredIndex();
		if (upToDate && stored.containsKey(library)) {
			return stored;

		} else {
			return createIndex();
		}
	}

	private Map<String, File> createIndex() throws IOException {
		Map<String, File> index = new Indexer(docRoot).index();
		io.save(new HashMap<>(index));
		return index;
	}

	private Map<String, File> getStoredIndex() {
		try {
			return io.load();
		} catch (Exception e) {
			System.err.println("index not found");
		}
		return new Hashtable<>();
	}
}
