package com.eriklievaart.doclauncher.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableIO {

	private final File file;

	/**
	 * Construct a {@link SerializableIO} that read and writes to the specified
	 * file path.
	 */
	public SerializableIO(final String path) {
		file = new File(path);
	}

	/**
	 * Construct a {@link SerializableIO} that read and writes to the specified
	 * file path.
	 */
	public SerializableIO(final File file) {
		this.file = file;
	}

	/**
	 * Write the Object graph.
	 */
	public void save(final Serializable root) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		try {
			out.writeObject(root);
		} finally {
			out.close();
		}
	}

	/**
	 * Read the Object graph.
	 */
	@SuppressWarnings("unchecked")
	public <E> E load() throws IOException {
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
		try {
			return (E) in.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to read data.", e);
		} finally {
			in.close();
		}
	}
}
