package com.github.angerona.fw.motivation.parser;

import static com.github.angerona.fw.island.beliefbase.FormulaUtils.createDesire;
import static com.github.angerona.fw.island.beliefbase.FormulaUtils.createFormula;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.island.parsing.ChannelBasedParser;
import com.github.angerona.fw.island.parsing.ParserException;
import com.github.angerona.fw.motivation.data.Maslow;
import com.github.angerona.fw.motivation.data.Motive;
import com.github.angerona.fw.motivation.data.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class CouplingParser extends ChannelBasedParser<Set<MotiveCoupling<Maslow>>> {

	@Override
	protected void parse(ByteBuffer buf, Set<MotiveCoupling<Maslow>> couplings) throws ParserException {
		int processed = buf.position();

		try {
			while (buf.hasRemaining()) {
				couplings.add(readCoupling(buf));
				skipWhiteSpaces(buf);
				processed = buf.position();
			}
		} catch (BufferUnderflowException e) {
			buf.position(processed);
		}
	}

	protected MotiveCoupling<Maslow> readCoupling(ByteBuffer buf) throws ParserException {
		consume(buf, '(');
		skipWhiteSpaces(buf);

		Motive<Maslow> m = readMotive(buf);
		skipWhiteSpaces(buf);

		consume(buf, ';');
		skipWhiteSpaces(buf);

		Desire d = createDesire(readKeyword(buf));
		skipWhiteSpaces(buf);

		consume(buf, ';');
		skipWhiteSpaces(buf);

		double cs = readNumber(buf);
		skipWhiteSpaces(buf);

		consume(buf, ';');
		skipWhiteSpaces(buf);

		FolFormula f = createFormula(readKeyword(buf));
		skipWhiteSpaces(buf);

		consume(buf, ')');
		skipWhiteSpaces(buf);

		consume(buf, ';');
		return new MotiveCoupling<>(m, d, cs, f);
	}

	protected Motive<Maslow> readMotive(ByteBuffer buf) throws ParserException {
		consume(buf, '[');
		skipWhiteSpaces(buf);

		String identifier = readKeyword(buf);
		skipWhiteSpaces(buf);

		consume(buf, ',');
		skipWhiteSpaces(buf);

		Maslow level = Maslow.valueOf(readKeyword(buf));
		skipWhiteSpaces(buf);

		consume(buf, ']');
		return new Motive<>(identifier, level);
	}

	@Override
	protected Set<MotiveCoupling<Maslow>> createStructure() {
		return new HashSet<>();
	}

}
