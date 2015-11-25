package com.github.kreaturesfw.simple;

import java.util.ArrayList;
import java.util.List;

import com.github.kreaturesfw.core.AngeronaPluginAdapter;
import com.github.kreaturesfw.core.operators.BaseOperator;
import com.github.kreaturesfw.simple.operators.ExecuteOperator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * 
 * @author Manuel Barbi
 *
 */
@PluginImplementation
public class SimplePlugin extends AngeronaPluginAdapter {

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> operators = new ArrayList<>();
		operators.add(ExecuteOperator.class);
		return operators;
	}

}
