package com.github.angerona.fw.motivation.plan;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.createFormula;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.github.angerona.fw.motivation.plan.dto.ActionDTO;
import com.github.angerona.fw.motivation.plan.dto.PlanDTO;
import com.github.angerona.fw.motivation.plan.dto.SequenceDTO;
import com.github.angerona.fw.motivation.plan.dto.TrailDTO;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class PlanFactory {

	public static Map<String, Collection<StateNode>> assemble(Collection<TrailDTO> trails) {
		// overall structure
		Map<String, Collection<StateNode>> plans = new HashMap<>();

		// start nodes for one desire
		Collection<StateNode> origins;

		Map<String, StateNode> nodes;
		Map<String, Collection<ActionEdge>> src;
		Set<String> dst;

		for (TrailDTO trail : trails) {
			origins = getNodes(plans, trail.getDes());

			nodes = new HashMap<>();
			src = new HashMap<>();
			dst = new HashSet<>();

			for (PlanDTO pln : trail.getPln()) {
				if (pln instanceof ActionDTO) {
					createAction((ActionDTO) pln, nodes, src, dst);
				} else if (pln instanceof SequenceDTO) {
					for (ActionDTO dto : ((SequenceDTO) pln).getAct()) {
						createAction(dto, nodes, src, dst);
					}
				}
			}

			for (StateNode node : nodes.values()) {
				setEdges(node, src.get(node.getName()));

				if (!dst.contains(node.getName())) {
					origins.add(node);
				}
			}
		}

		return plans;
	}

	private static Collection<StateNode> getNodes(Map<String, Collection<StateNode>> map, String key) {
		Collection<StateNode> list = map.get(key);

		if (list == null) {
			list = new LinkedList<>();
			map.put(key, list);
		}

		return list;
	}

	private static void createAction(ActionDTO dto, Map<String, StateNode> nodes, Map<String, Collection<ActionEdge>> src, Set<String> dst) {
		getNode(nodes, dto.getSrc());
		ActionEdge action = new ActionEdge(dto.getId());

		if (dto.getCond() != null) {
			action.setCondition(createFormula(dto.getCond()));
		}

		if (dto.getDst() != null) {
			action.setGoal(getNode(nodes, dto.getDst()));
		}

		getActions(src, dto.getSrc()).add(action);

		if (dto.getDst() != null) {
			dst.add(dto.getDst());
		}
	}

	private static StateNode getNode(Map<String, StateNode> nodes, String key) {
		StateNode node = nodes.get(key);

		if (node == null) {
			node = new StateNode(key);
			nodes.put(key, node);
		}

		return node;
	}

	private static Collection<ActionEdge> getActions(Map<String, Collection<ActionEdge>> map, String key) {
		Collection<ActionEdge> list = map.get(key);

		if (list == null) {
			list = new LinkedList<>();
			map.put(key, list);
		}

		return list;
	}

	private static void setEdges(StateNode node, Collection<ActionEdge> edges) {
		if (edges != null) {
			node.setActions(edges.toArray(new ActionEdge[edges.size()]));
		}
	}

}
