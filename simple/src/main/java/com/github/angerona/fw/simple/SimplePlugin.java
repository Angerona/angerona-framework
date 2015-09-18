package com.github.angerona.fw.simple;

import java.util.ArrayList;
import java.util.List;

import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.fw.simple.operators.ExecuteOperator;

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
