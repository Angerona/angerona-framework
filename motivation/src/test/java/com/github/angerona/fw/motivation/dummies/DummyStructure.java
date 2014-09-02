package com.github.angerona.fw.motivation.dummies;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.createDesire;

import java.util.Collections;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.model.MotStrcEntry;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DummyStructure extends MotStructure {

	public static final Desire GET_RICH = createDesire("get_rich");
	public static final Desire BUY_FAST_CAR = createDesire("buy_fast_car");
	public static final Desire BUY_LOCAL_FRUITS = createDesire("buy_local_fruits");
	public static final Desire PLANT_TREE = createDesire("plant_tree");
	public static final Desire SAVE_WHALES = createDesire("save_whales");
	public static final Desire BUILD_HOUSE = createDesire("build_house");
	public static final Desire RAISE_A_FAMILY = createDesire("raise_a_family");
	public static final Desire CLIMB_A_MOUNATIN = createDesire("climb_a_mounatin");
	public static final Desire GET_JOB = createDesire("get_job");
	public static final Desire TRAVEL_THE_WORLD = createDesire("travel_the_world");
	public static final Desire FLY_TO_MARS = createDesire("fly_to_mars");

	public static final double UPPER = 0.74;
	public static final double MEDIAN = 0.41;
	public static final double LOWER = 0.28;

	public DummyStructure() {
		this.entries.add(new MotStrcEntry(GET_RICH, 0.99));
		this.entries.add(new MotStrcEntry(BUY_FAST_CAR, 0.86));
		this.entries.add(new MotStrcEntry(BUY_LOCAL_FRUITS, UPPER));
		this.entries.add(new MotStrcEntry(PLANT_TREE, 0.67));
		this.entries.add(new MotStrcEntry(SAVE_WHALES, 0.53));
		this.entries.add(new MotStrcEntry(BUILD_HOUSE, MEDIAN));
		this.entries.add(new MotStrcEntry(RAISE_A_FAMILY, 0.39));
		this.entries.add(new MotStrcEntry(CLIMB_A_MOUNATIN, 0.34));
		this.entries.add(new MotStrcEntry(GET_JOB, LOWER));
		this.entries.add(new MotStrcEntry(TRAVEL_THE_WORLD, 0.13));
		this.entries.add(new MotStrcEntry(FLY_TO_MARS, 0.02));
		Collections.sort(entries);
	}

}
