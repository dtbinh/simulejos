package it.uniroma1.di.simulejos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class FullReader extends BufferedReader {
	public FullReader(Reader in) {
		super(in);
	}

	public FullReader(Reader in, int sz) {
		super(in, sz);
	}

	public String readAll() throws IOException {
		final char[] buffer = new char[0x1000];
		final StringBuffer stringBuffer = new StringBuffer();
		int read;
		while ((read = read(buffer)) >= 0) {
			stringBuffer.append(buffer, 0, read);
		}
		return new String(stringBuffer);
	}
}
