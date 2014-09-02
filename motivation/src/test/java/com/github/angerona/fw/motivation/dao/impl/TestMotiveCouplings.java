package com.github.angerona.fw.motivation.dao.impl;

import static com.github.angerona.fw.motivation.Maslow.ESTEEM;
import static com.github.angerona.fw.motivation.Maslow.LOVE_AND_BELONGING;
import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SAFETY_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.BUY_LOCAL_FRUITS;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.BUY_SPORTS_CAR;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.COUPL_AWARE;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.COUPL_FRUITS;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.COUPL_PRESTIGE;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.COUPL_WHALES;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.ENV_AWARENESS;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.PRESTIGE;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.SAVE_WHALES;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.CHEAP_FOOD;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.COUPL_CHEAP;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.COUPL_OWN;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.ENVIRONMENTAL_AWARENESS;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.FOOD;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.OWN_COUNTRY;

import java.util.Collection;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.junit.Assert;
import org.junit.Test;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dummies.DummyCouplingsOne;
import com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TestMotiveCouplings {

	@Test
	public void testGetMotivesL() {
		MotiveCouplings cpl1 = new DummyCouplingsOne();

		Assert.assertTrue("size cpl1, pn", cpl1.getMotives(PHYSIOLOGICAL_NEEDS).isEmpty());
		Assert.assertTrue("size cpl1, sn", cpl1.getMotives(SAFETY_NEEDS).isEmpty());
		Assert.assertTrue("size cpl1, lb", cpl1.getMotives(LOVE_AND_BELONGING).isEmpty());

		Collection<Motive<Maslow>> mEs1 = cpl1.getMotives(ESTEEM);
		Assert.assertEquals("size cpl1, es", 1, mEs1.size());
		Assert.assertTrue("cont cpl1, es", mEs1.contains(PRESTIGE));

		Collection<Motive<Maslow>> mSa1 = cpl1.getMotives(SELF_ACTUALIZATION);
		Assert.assertEquals("size cpl1, sa", 1, mSa1.size());
		Assert.assertTrue("cont cpl1, sa", mSa1.contains(ENV_AWARENESS));

		MotiveCouplings cpl2 = new DummyCouplingsTwo();

		Collection<Motive<Maslow>> mPn2 = cpl2.getMotives(PHYSIOLOGICAL_NEEDS);
		Assert.assertEquals("size cpl2, pn", 1, mPn2.size());
		Assert.assertTrue("cont cpl2, pn", mPn2.contains(FOOD));

		Assert.assertTrue("size cpl2, sn", cpl2.getMotives(SAFETY_NEEDS).isEmpty());
		Assert.assertTrue("size cpl2, lb", cpl2.getMotives(LOVE_AND_BELONGING).isEmpty());
		Assert.assertTrue("size cpl2, es", cpl2.getMotives(ESTEEM).isEmpty());

		Collection<Motive<Maslow>> mSa2 = cpl2.getMotives(SELF_ACTUALIZATION);
		Assert.assertEquals("size cpl2, sa", 1, mSa2.size());
		Assert.assertTrue("cont cpl2, sa", mSa2.contains(ENVIRONMENTAL_AWARENESS));
	}

	@Test
	public void testGetCouplingsM() {
		MotiveCouplings cpl1 = new DummyCouplingsOne();

		Collection<MotiveCoupling<Maslow, FolFormula>> cplEnv = cpl1.getCouplings(ENV_AWARENESS);
		Assert.assertEquals("size cplEnv", 3, cplEnv.size());
		Assert.assertTrue("cont cplEnv whale", cplEnv.contains(COUPL_WHALES));
		Assert.assertTrue("cont cplEnv fruit", cplEnv.contains(COUPL_FRUITS));
		Assert.assertTrue("cont cplEnv aware", cplEnv.contains(COUPL_AWARE));

		Collection<MotiveCoupling<Maslow, FolFormula>> cplPrt = cpl1.getCouplings(PRESTIGE);
		Assert.assertEquals("size cplPrt", 1, cplPrt.size());
		Assert.assertTrue("cont cplPrt prestige", cplPrt.contains(COUPL_PRESTIGE));

		MotiveCouplings cpl2 = new DummyCouplingsTwo();

		Collection<MotiveCoupling<Maslow, FolFormula>> cplAwr = cpl2.getCouplings(ENVIRONMENTAL_AWARENESS);
		Assert.assertEquals("size cplAwr", 1, cplAwr.size());
		Assert.assertTrue("cont cplAwr own", cplAwr.contains(COUPL_OWN));

		Collection<MotiveCoupling<Maslow, FolFormula>> cplFod = cpl2.getCouplings(FOOD);
		Assert.assertEquals("size cplFog", 1, cplFod.size());
		Assert.assertTrue("cont cplFog cheap", cplFod.contains(COUPL_CHEAP));
	}

	@Test
	public void testGetCouplingsD() {
		MotiveCouplings cpl1 = new DummyCouplingsOne();

		Collection<MotiveCoupling<Maslow, FolFormula>> cplWhl = cpl1.getCouplings(SAVE_WHALES);
		Assert.assertEquals("size cplWhl", 1, cplWhl.size());
		Assert.assertTrue("cont cplWhl whales", cplWhl.contains(COUPL_WHALES));

		Collection<MotiveCoupling<Maslow, FolFormula>> cplFrt = cpl1.getCouplings(BUY_LOCAL_FRUITS);
		Assert.assertEquals("size cplFrt", 1, cplFrt.size());
		Assert.assertTrue("cont cplFrt fruits", cplFrt.contains(COUPL_FRUITS));

		Collection<MotiveCoupling<Maslow, FolFormula>> cplCar = cpl1.getCouplings(BUY_SPORTS_CAR);
		Assert.assertEquals("size cplCar", 2, cplCar.size());
		Assert.assertTrue("cont cplCar aware", cplCar.contains(COUPL_AWARE));
		Assert.assertTrue("cont cplCar prestige", cplCar.contains(COUPL_PRESTIGE));

		MotiveCouplings cpl2 = new DummyCouplingsTwo();

		Collection<MotiveCoupling<Maslow, FolFormula>> cplOwn = cpl2.getCouplings(OWN_COUNTRY);
		Assert.assertEquals("size cplOwn", 1, cplOwn.size());
		Assert.assertTrue("cont cplOwn own", cplOwn.contains(COUPL_OWN));

		Collection<MotiveCoupling<Maslow, FolFormula>> cplChp = cpl2.getCouplings(CHEAP_FOOD);
		Assert.assertEquals("size cplChp", 1, cplChp.size());
		Assert.assertTrue("cont cplChp cheap", cplChp.contains(COUPL_CHEAP));
	}

	@Test
	public void testGetMeanCs() {
		MotiveCouplings cpl1 = new DummyCouplingsOne();
		Assert.assertEquals((2.2 / 3), cpl1.getMeanCouplingStrength(), 0.000001);

		MotiveCouplings cpl2 = new DummyCouplingsTwo();
		Assert.assertEquals(0.3, cpl2.getMeanCouplingStrength(), 0.0);
	}
}
