package com.github.angerona.fw.motivation.parser;

import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.COUPL_FRUITS;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.COUPL_WHALES;
import static com.github.angerona.fw.motivation.dummies.DummyRanges.WR_PN;
import static com.github.angerona.fw.motivation.dummies.DummyRanges.WR_SA;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.model.MotiveCoupling;
import com.github.angerona.fw.motivation.model.WeightRange;
import com.github.angerona.fw.util.Pair;

public class MotivationParserTest {

	private static final Logger LOG = LoggerFactory.getLogger(MotivationParserTest.class);

	private static final String COUPLINGS = COUPL_WHALES + ";\n" + COUPL_FRUITS + ";";

	private static final String RANGES = "(" + PHYSIOLOGICAL_NEEDS + ";\t" + WR_PN + ");\n" + "(" + SELF_ACTUALIZATION + ";\t" + WR_SA + ");";

	@Test
	public void testCreateParser() {
		@SuppressWarnings("unused")
		MotivationParser parser = new MotivationParser(new ByteArrayInputStream(new byte[0]));
	}

	@Test
	public void testReadNumber() throws ParseException {
		String lower = String.valueOf(WR_PN.getLower());
		LOG.debug(lower);

		InputStream in = new ByteArrayInputStream(lower.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Assert.assertEquals(WR_PN.getLower(), parser.readNumber(), 0);
	}

	@Test
	public void testReadNumPair() throws ParseException {
		String range = WR_PN.toString();
		LOG.debug(range);

		InputStream in = new ByteArrayInputStream(range.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Pair<Double, Double> numbers = parser.readNumPair();
		Assert.assertEquals(WR_PN.getLower(), numbers.first, 0);
		Assert.assertEquals(WR_PN.getUpper(), numbers.second, 0);
	}

	@Test
	public void testReadKeyword() throws ParseException {
		String identifier = COUPL_WHALES.getMotive().getIdentifier();
		LOG.debug(identifier);

		InputStream in = new ByteArrayInputStream(identifier.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Assert.assertEquals(COUPL_WHALES.getMotive().getIdentifier(), parser.readKeyword());
	}

	@Test
	public void testReadKeyPair() throws ParseException {
		String mot = COUPL_WHALES.getMotive().toString();
		LOG.debug(mot);

		InputStream in = new ByteArrayInputStream(mot.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Pair<String, String> words = parser.readKeyPair();
		Assert.assertEquals(COUPL_WHALES.getMotive().getIdentifier(), words.first);
		Assert.assertEquals(COUPL_WHALES.getMotive().getLevel().toString(), words.second);
	}

	@Test
	public void testGatherCouplings() throws ParseException {
		LOG.debug(COUPLINGS);

		InputStream in = new ByteArrayInputStream(COUPLINGS.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Set<MotiveCoupling<Maslow, FolFormula>> couplings = parser.gatherCouplings();
		Assert.assertTrue(couplings.size() == 2);
		Assert.assertTrue(couplings.contains(COUPL_WHALES));
		Assert.assertTrue(couplings.contains(COUPL_FRUITS));
	}

	@Test
	public void testGatherRanges() throws ParseException {
		LOG.debug(RANGES);

		InputStream in = new ByteArrayInputStream(RANGES.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Map<Maslow, WeightRange> ranges = parser.gatherRanges();
		Assert.assertTrue(ranges.size() == 2);
		Assert.assertEquals(WR_PN, ranges.get(Maslow.PHYSIOLOGICAL_NEEDS));
		Assert.assertEquals(WR_SA, ranges.get(Maslow.SELF_ACTUALIZATION));
	}
}
