package angerona.fw.motivation.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;
import com.github.angerona.fw.motivation.model.WeightRange;
import com.github.angerona.fw.motivation.parser.MotivationParser;
import com.github.angerona.fw.motivation.parser.ParseException;
import com.github.angerona.fw.util.Pair;

public class MotivationParserTest {

	private static final Logger LOG = LoggerFactory.getLogger(MotivationParserTest.class);

	private static final MotiveCoupling<Maslow, FolFormula> COUPLING_1 = new MotiveCoupling<Maslow, FolFormula>(new Motive<Maslow>("hunger",
			Maslow.PHYSIOLOGICAL_NEEDS), new Desire(new FOLAtom(new Predicate("fill_battery"))), 0.85, new FOLAtom(new Predicate("low_battery")));

	private static final MotiveCoupling<Maslow, FolFormula> COUPLING_2 = new MotiveCoupling<Maslow, FolFormula>(new Motive<Maslow>(
			"self_preservation", Maslow.SAFETY_NEEDS), new Desire(new FOLAtom(new Predicate("find_shelter"))), 0.65, new FOLAtom(new Predicate(
			"danger")));

	private static final String COUPLINGS = COUPLING_1 + ";\n" + COUPLING_2 + ";";

	private static final WeightRange RANGE_1 = new WeightRange(0.85, 1);
	private static final WeightRange RANGE_2 = new WeightRange(0.65, 0.9);

	private static final String RANGES = "(" + Maslow.PHYSIOLOGICAL_NEEDS + ";\t" + RANGE_1 + ");\n" + "(" + Maslow.SAFETY_NEEDS + ";\t"
			+ RANGE_2 + ");";

	@Test
	public void testCreateParser() {
		@SuppressWarnings("unused")
		MotivationParser parser = new MotivationParser(new ByteArrayInputStream(new byte[0]));
	}

	@Test
	public void testReadNumber() throws ParseException {
		String lower = String.valueOf(RANGE_1.getLower());
		LOG.debug(lower);

		InputStream in = new ByteArrayInputStream(lower.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Assert.assertEquals(RANGE_1.getLower(), parser.readNumber(), 0);
	}

	@Test
	public void testReadNumPair() throws ParseException {
		String range = RANGE_1.toString();
		LOG.debug(range);

		InputStream in = new ByteArrayInputStream(range.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Pair<Double, Double> numbers = parser.readNumPair();
		Assert.assertEquals(RANGE_1.getLower(), numbers.first, 0);
		Assert.assertEquals(RANGE_1.getUpper(), numbers.second, 0);
	}

	@Test
	public void testReadKeyword() throws ParseException {
		String identifier = COUPLING_1.getMotive().getIdentifier();
		LOG.debug(identifier);

		InputStream in = new ByteArrayInputStream(identifier.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Assert.assertEquals(COUPLING_1.getMotive().getIdentifier(), parser.readKeyword());
	}

	@Test
	public void testReadKeyPair() throws ParseException {
		String mot = COUPLING_1.getMotive().toString();
		LOG.debug(mot);

		InputStream in = new ByteArrayInputStream(mot.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Pair<String, String> words = parser.readKeyPair();
		Assert.assertEquals(COUPLING_1.getMotive().getIdentifier(), words.first);
		Assert.assertEquals(COUPLING_1.getMotive().getLevel().toString(), words.second);
	}

	@Test
	public void testGatherCouplings() throws ParseException {
		LOG.debug(COUPLINGS);

		InputStream in = new ByteArrayInputStream(COUPLINGS.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Set<MotiveCoupling<Maslow, FolFormula>> couplings = parser.gatherCouplings();
		Assert.assertTrue(couplings.size() == 2);
		Assert.assertTrue(couplings.contains(COUPLING_1));
		Assert.assertTrue(couplings.contains(COUPLING_2));
	}

	@Test
	public void testGatherRanges() throws ParseException {
		LOG.debug(RANGES);

		InputStream in = new ByteArrayInputStream(RANGES.getBytes(StandardCharsets.UTF_8));
		MotivationParser parser = new MotivationParser(in);

		Map<Maslow, WeightRange> ranges = parser.gatherRanges();
		Assert.assertTrue(ranges.size() == 2);
		Assert.assertEquals(RANGE_1, ranges.get(Maslow.PHYSIOLOGICAL_NEEDS));
		Assert.assertEquals(RANGE_2, ranges.get(Maslow.SAFETY_NEEDS));
	}
}
