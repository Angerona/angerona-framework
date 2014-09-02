package com.github.angerona.fw.motivation.functions.impl;

import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.BUY_LOCAL_FRUITS;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.BUY_SPORTS_CAR;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsOne.SAVE_WHALES;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.CHEAP_FOOD;
import static com.github.angerona.fw.motivation.dummies.DummyCouplingsTwo.OWN_COUNTRY;

import org.junit.Assert;
import org.junit.Test;

import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dummies.DummyBeliefState;
import com.github.angerona.fw.motivation.dummies.DummyMotiveStateOne;
import com.github.angerona.fw.motivation.dummies.DummyMotiveStateTwo;
import com.github.angerona.fw.motivation.functional.impl.StructureAdjustmentImpl;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class StructureAdjustmentImplTest {

	private static final double MU_FRUITS = 0.075;
	private static final double MU_CAR = 0.225 / 0.775;

	private static final double MU_OWN = 0.18;
	private static final double MU_CHEAP = 0.12;

	@Test
	public void testCreateAdjustment() {
		@SuppressWarnings("unused")
		StructureAdjustmentImpl adjustment = new StructureAdjustmentImpl();
	}

	@Test
	public void testAdjustOne() {
		MotStructure structure = new MotStructure();

		StructureAdjustmentImpl adjustment = new StructureAdjustmentImpl();
		adjustment.adjust(new DummyMotiveStateOne(), new DummyBeliefState(), structure);

		Assert.assertTrue(structure.getValue(SAVE_WHALES) == null);
		Assert.assertEquals(MU_FRUITS, structure.getValue(BUY_LOCAL_FRUITS), 0.0);
		Assert.assertEquals(MU_CAR, structure.getValue(BUY_SPORTS_CAR), 0.0);
	}

	@Test
	public void testAdjustTwo() {
		MotStructure structure = new MotStructure();

		StructureAdjustmentImpl adjustment = new StructureAdjustmentImpl();
		adjustment.adjust(new DummyMotiveStateTwo(), new DummyBeliefState(), structure);

		Assert.assertEquals(MU_OWN, structure.getValue(OWN_COUNTRY), 0.0);
		Assert.assertEquals(MU_CHEAP, structure.getValue(CHEAP_FOOD), 0.0);
	}

}
