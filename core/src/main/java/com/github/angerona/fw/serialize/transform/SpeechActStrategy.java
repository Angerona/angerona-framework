package com.github.angerona.fw.serialize.transform;

import java.util.Map;

import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import com.github.angerona.fw.comm.Inform;
import com.github.angerona.fw.comm.Justification;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.comm.Revision;
import com.github.angerona.fw.comm.SpeechAct;
import com.github.angerona.fw.comm.Update;

public class SpeechActStrategy implements Strategy{

	public boolean write(Type arg0, Object arg1,
			NodeMap<OutputNode> arg2, Map arg3) throws Exception {
		return false;
	}

	public Value read(Type arg0, NodeMap<InputNode> arg1, Map arg2)
			throws Exception {
		
			if (arg0.getType().equals(SpeechAct.class)) {
				InputNode node = arg1.getNode().getNext();
			InputNode typeNode = node.getAttribute("type");
			
			String typeValue = typeNode.getValue();

			// remove namespace prefix
			if (typeValue.equals("Query")) {

				return new Value() {
					@Override
					public void setValue(Object arg0) {
					}
					@Override
					public boolean isReference() {
						return false;
					}
					@Override
					public Object getValue() {
						return null;
					}
					@Override
					public Class getType() {
						return Query.class;
					}
					@Override
					public int getLength() {
						return 0;
					}
				};
			} else if (typeValue.equals("Revision")) {

				return new Value() {
					@Override
					public void setValue(Object arg0) {
					}

					@Override
					public boolean isReference() {
						return false;
					}

					@Override
					public Object getValue() {
						return null;
					}

					@Override
					public Class getType() {
						return Revision.class;
					}

					@Override
					public int getLength() {
						return 0;
					}
				};
			} else if (typeValue.equals("Inform")) {

				return new Value() {
					@Override
					public void setValue(Object arg0) {
					}

					@Override
					public boolean isReference() {
						return false;
					}

					@Override
					public Object getValue() {
						return null;
					}

					@Override
					public Class getType() {
						return Inform.class;
					}

					@Override
					public int getLength() {
						return 0;
					}
				};
			}else if (typeValue.equals("Justification")) {

				return new Value() {
					@Override
					public void setValue(Object arg0) {
					}

					@Override
					public boolean isReference() {
						return false;
					}

					@Override
					public Object getValue() {
						return null;
					}

					@Override
					public Class getType() {
						return Justification.class;
					}

					@Override
					public int getLength() {
						return 0;
					}
				};
			}else if (typeValue.equals("Update")) {

				return new Value() {
					@Override
					public void setValue(Object arg0) {
					}

					@Override
					public boolean isReference() {
						return false;
					}

					@Override
					public Object getValue() {
						return null;
					}

					@Override
					public Class getType() {
						return Update.class;
					}

					@Override
					public int getLength() {
						return 0;
					}
				};
			}
		}
		return null;
	}
}
