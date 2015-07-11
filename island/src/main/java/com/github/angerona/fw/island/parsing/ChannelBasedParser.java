package com.github.angerona.fw.island.parsing;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class ChannelBasedParser<STR> {

	protected ByteBuffer buf = ByteBuffer.allocate(16 * 1024);

	public STR read(FileInputStream fileIn) throws IOException {
		try (ReadableByteChannel src = fileIn.getChannel()) {
			buf.clear();
			boolean changed = true;
			int pos;
			STR structure = createStructure();

			while (src.read(buf) > 0 && changed) {
				buf.flip();
				pos = buf.position();
				parse(buf, structure);
				changed = buf.position() != pos;
				buf.compact();
			}

			return structure;
		}
	}

	protected abstract void parse(ByteBuffer buf, STR structure) throws ParserException;

	protected abstract STR createStructure();

	public static void skipWhiteSpaces(ByteBuffer buf) {
		int pos = buf.position();
		char c;

		while (buf.hasRemaining()) {
			c = (char) buf.get();

			if (c != ' ' && c != '\t' && c != '\n' && c != '\r')
				break;

			pos = buf.position();
		}

		buf.position(pos);
	}

	public static double readNumber(ByteBuffer buf) throws ParserException {
		int start = buf.position();
		int end = start;
		char c;

		while (buf.hasRemaining()) {
			c = (char) buf.get();

			if (!(c >= '0' && c <= '9' || c == '-' || c == '.'))
				break;

			end = buf.position();
		}

		buf.position(start);
		try {
			return Double.parseDouble(readString(buf, start, end));
		} catch (NumberFormatException e) {
			throw new ParserException(e);
		}
	}

	public static String readKeyword(ByteBuffer buf) {
		int start = buf.position();
		int end = start;
		char c;

		while (buf.hasRemaining()) {
			c = (char) buf.get();

			if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c >= '0' && c <= '9'))
				break;

			end = buf.position();
		}

		buf.position(start);
		return readString(buf, start, end);
	}

	protected static String readString(ByteBuffer buf, int start, int end) {
		byte[] word = new byte[end - start];
		buf.get(word);
		return new String(word, StandardCharsets.UTF_8);
	}

	protected static void consume(ByteBuffer buf, char c) throws ParserException {
		if (buf.get() != c)
			throw new ParserException();
	}

	protected static void consume(ByteBuffer buf, String str) throws ParserException {
		for (byte b : str.getBytes(StandardCharsets.UTF_8)) {
			if (buf.get() != b)
				throw new ParserException();
		}
	}

}
