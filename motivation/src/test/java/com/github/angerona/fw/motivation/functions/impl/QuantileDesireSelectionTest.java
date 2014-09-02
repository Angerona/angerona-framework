package com.github.angerona.fw.motivation.functions.impl;

import static com.github.angerona.fw.motivation.dummies.DummyStructure.BUILD_HOUSE;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.BUY_FAST_CAR;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.BUY_LOCAL_FRUITS;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.CLIMB_A_MOUNATIN;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.GET_JOB;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.GET_RICH;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.LOWER;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.MEDIAN;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.PLANT_TREE;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.RAISE_A_FAMILY;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.SAVE_WHALES;
import static com.github.angerona.fw.motivation.dummies.DummyStructure.UPPER;

import java.util.Collection;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.junit.Assert;
import org.junit.Test;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dummies.DummyBeliefState;
import com.github.angerona.fw.motivation.dummies.DummyStructure;
import com.github.angerona.fw.motivation.functional.impl.QuantileDesireSelection;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class QuantileDesireSelectionTest {

	@Test
	public void testCreateSelection() {
		@SuppressWarnings("unused")
		QuantileDesireSelection<FolFormula> selection = new QuantileDesireSelection<>();
	}

	@Test
	public void testCalcLower() {
		QuantileDesireSelection<FolFormula> selection = new QuantileDesireSelection<>(0.25);
		Assert.assertEquals(LOWER, selection.calcQuantile(new DummyStructure().getOrderedEntries()), 0.0);
	}

	@Test
	public void testCalcMedian() {
		QuantileDesireSelection<FolFormula> selection = new QuantileDesireSelection<>(0.5);
		Assert.assertEquals(MEDIAN, selection.calcQuantile(new DummyStructure().getOrderedEntries()), 0.0);
	}

	@Test
	public void testCalcUpper() {
		QuantileDesireSelection<FolFormula> selection = new QuantileDesireSelection<>(0.75);
		Assert.assertEquals(UPPER, selection.calcQuantile(new DummyStructure().getOrderedEntries()), 0.0);
	}

	@Test
	public void testSelectLower() {
		QuantileDesireSelection<FolFormula> selection = new QuantileDesireSelection<>(0.25);
		Collection<Desire> desires = selection.select(new DummyBeliefState(), new DummyStructure());
		Assert.assertEquals(9, desires.size());
		Assert.assertTrue(desires.contains(GET_RICH));
		Assert.assertTrue(desires.contains(BUY_FAST_CAR));
		Assert.assertTrue(desires.contains(BUY_LOCAL_FRUITS));
		Assert.assertTrue(desires.contains(PLANT_TREE));
		Assert.assertTrue(desires.contains(SAVE_WHALES));
		Assert.assertTrue(desires.contains(BUILD_HOUSE));
		Assert.assertTrue(desires.contains(RAISE_A_FAMILY));
		Assert.assertTrue(desires.contains(CLIMB_A_MOUNATIN));
		Assert.assertTrue(desires.contains(GET_JOB));
	}

	@Test
	public void testSelectMedian() {
		QuantileDesireSelection<FolFormula> selection = new QuantileDesireSelection<>(0.5);
		Collection<Desire> desires = selection.select(new DummyBeliefState(), new DummyStructure());
		Assert.assertEquals(6, desires.size());
		Assert.assertTrue(desires.contains(GET_RICH));
		Assert.assertTrue(desires.contains(BUY_FAST_CAR));
		Assert.assertTrue(desires.contains(BUY_LOCAL_FRUITS));
		Assert.assertTrue(desires.contains(PLANT_TREE));
		Assert.assertTrue(desires.contains(SAVE_WHALES));
		Assert.assertTrue(desires.contains(BUILD_HOUSE));
	}

	@Test
	public void testSelectUpper() {
		QuantileDesireSelection<FolFormula> selection = new QuantileDesireSelection<>(0.75);
		Collection<Desire> desires = selection.select(new DummyBeliefState(), new DummyStructure());
		Assert.assertEquals(3, desires.size());
		Assert.assertTrue(desires.contains(GET_RICH));
		Assert.assertTrue(desires.contains(BUY_FAST_CAR));
		Assert.assertTrue(desires.contains(BUY_LOCAL_FRUITS));
	}

}
