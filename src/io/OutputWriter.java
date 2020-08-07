package io;

public interface OutputWriter {
	void print(String s);
	void println(String s);
	String getLineSeparator();
}
