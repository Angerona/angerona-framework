package com.github.kreatures.simple.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Manuel Barbi
 *
 * @param <T>
 *            the data-structure collecting parsed items
 * @param <S>
 *            can be used as parser-state, typically an enum-class, but may also
 *            represent the context, since the parser itself should remain stateless
 */
public interface ChannelBasedParser<T, S> {

	public static final int BUFFER_SIZE = 64 * 1024;

	default T parse(File path) throws IOException {
		try (FileInputStream fileIn = new FileInputStream(path)) {
			return parse(fileIn);
		}
	}

	default T parse(InputStream in) throws IOException {
		try (ReadableByteChannel src = (in instanceof FileInputStream) ? ((FileInputStream) in).getChannel() : Channels.newChannel(in)) {
			return parse(src);
		}
	}

	default T parse(ReadableByteChannel src) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(getBufferSize());
		T structure = createStructure();
		S state = getInitialState();
		boolean hasChanged;
		int pos;

		while (src.read(buf) > 0) {
			buf.flip();
			hasChanged = true;
			while (buf.hasRemaining() && hasChanged) {
				pos = buf.position();
				state = parse(buf, structure, state);
				hasChanged = buf.position() != pos;
			}
			buf.compact();
		}

		return structure;
	}

	/**
	 * This parse method may return any time due to change of state, but will be
	 * invoked repeatedly until buffer is empty or nothing was consumed. This
	 * method must not throw BufferUnderflowException, but rather catch and
	 * reset the buffer's position to the last processed position, like...
	 * 
	 * 		int processed = buf.position();
	 * 
	 * 		try {
	 * 			// parse something
	 * 			processed = buf.position();
	 * 		} catch(BufferUnderflowException bu) {
	 * 			buf.position(processed);
	 * 			return state;
	 * 		}
	 * 
	 * @param buf
	 * @param structure
	 * @param state
	 * @return
	 */
	S parse(ByteBuffer buf, T structure, S state);

	S getInitialState();

	T createStructure();

	default int getBufferSize() {
		return BUFFER_SIZE;
	}

}
