package com.github.angerona.fw.motivation.parser;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.github.angerona.fw.island.parsing.ChannelBasedParser;
import com.github.angerona.fw.island.parsing.ParserException;
import com.github.angerona.fw.motivation.data.Maslow;
import com.github.angerona.fw.motivation.data.WeightRange;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class RangeParser extends ChannelBasedParser<Map<Maslow, WeightRange>> {

	@Override
	protected void parse(ByteBuffer buf, Map<Maslow, WeightRange> ranges) throws ParserException {
		int processed = buf.position();

		try {
			while (buf.hasRemaining()) {
				addRangeEntry(buf, ranges);
				skipWhiteSpaces(buf);
				processed = buf.position();
			}
		} catch (BufferUnderflowException e) {
			buf.position(processed);
		}

	}

	protected void addRangeEntry(ByteBuffer buf, Map<Maslow, WeightRange> ranges) throws ParserException {
		consume(buf, '(');
		skipWhiteSpaces(buf);

		Maslow level = Maslow.valueOf(readKeyword(buf));
		skipWhiteSpaces(buf);

		consume(buf, ';');
		skipWhiteSpaces(buf);

		WeightRange range = readRange(buf);
		skipWhiteSpaces(buf);

		consume(buf, ')');
		skipWhiteSpaces(buf);

		consume(buf, ';');
		ranges.put(level, range);
	}

	protected WeightRange readRange(ByteBuffer buf) throws ParserException {
		consume(buf, '[');
		skipWhiteSpaces(buf);

		double lower = readNumber(buf);
		skipWhiteSpaces(buf);

		consume(buf, ',');
		skipWhiteSpaces(buf);

		double upper = readNumber(buf);
		skipWhiteSpaces(buf);

		consume(buf, ']');
		return new WeightRange(lower, upper);
	}

	@Override
	protected Map<Maslow, WeightRange> createStructure() {
		return new HashMap<>();
	}

}
