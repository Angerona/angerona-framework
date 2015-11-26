package com.github.kreaturesfw.core;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.github.kreaturesfw.core.AngeronaPluginAdapter;
import com.github.kreaturesfw.core.internal.PluginInstantiator;
import com.github.kreaturesfw.core.legacy.AgentComponent;
import com.github.kreaturesfw.core.legacy.BaseAgentComponent;

public class PluginInstantiatorTest {
	@Test
	public void testRegisterUnregister() {
		PluginInstantiator pi = PluginInstantiator.getInstance();
		MockPluginA pluginA = new MockPluginA();
		assertEquals(false, pi.isImplementationRegistered(MockComponent.class));
		pi.registerPlugin(pluginA);
		
		assertEquals(true, pi.isImplementationRegistered(MockComponent.class));
		pi.unregisterPlugin(pluginA);
		
		assertEquals(false, pi.isImplementationRegistered(MockComponent.class));
	}
	
	private class MockComponent extends BaseAgentComponent {

		@Override
		public MockComponent clone() {
			return new MockComponent();
		}
		
	}
	
	private static class MockPluginA extends AngeronaPluginAdapter {
		@Override
		public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
			List<Class<? extends AgentComponent>> reval = new LinkedList<>();
			reval.add(MockComponent.class);
			return reval;
		}
	}
}
