package com.github.kreatures.simple;

import java.util.ArrayList;
import java.util.List;

import com.github.kreatures.core.KReaturesPluginAdapter;
import com.github.kreatures.core.operators.BaseOperator;
import com.github.kreatures.simple.operators.ExecuteOperator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * 
 * @author Manuel Barbi
 *
 */
@PluginImplementation
public class SimplePlugin extends KReaturesPluginAdapter {

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> operators = new ArrayList<>();
		operators.add(ExecuteOperator.class);
		return operators;
	}

}
