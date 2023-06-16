package com.landsem.setting.upgrade;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class IOUtils {
	private IOUtils() {}

	private static final int DEFAULT_BUFFER_LENGTH = 1024 * 8;

	public static void close(Closeable... closeables) {
		if (closeables == null) return;
		for (Closeable closeable : closeables)
			try {
				closeable.close();
			}
			catch (Exception e) {}
	}

	public static void closeObjects(Object... closeableObjects) {
		if (closeableObjects == null) return;
		for (Object object : closeableObjects) {
			if (object == null) continue;
			try {
				if (object instanceof Closeable)
					((Closeable) object).close();
				else
					closeObjectReflexly(object);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void closeObjectReflexly(Object object) throws Exception {
		Method closeMethod = object.getClass().getDeclaredMethod("close", (Class<?>[]) null);
		closeMethod.invoke(object, (Object[]) null);
	}

	public static void write(OutputStream os, InputStream is) throws IOException {
		write(os, is, DEFAULT_BUFFER_LENGTH);
	}

	public static void write(OutputStream os, InputStream is, int bufferLength) throws IOException {
		byte[] buffer = new byte[bufferLength];
		int read;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
	}

	public static void saveObject(Object src, File dest) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(dest));
			oos.writeObject(src);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.close(oos);
		}
	}

	public static Object readObject(File src) {
		ObjectInputStream ois = null;
		Object result = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(src));
			result = ois.readObject();
		}
		catch (Exception e) {}
		finally {
			IOUtils.close(ois);
		}
		return result;
	}
}
