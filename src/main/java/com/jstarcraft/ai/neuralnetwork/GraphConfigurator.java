package com.jstarcraft.ai.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jstarcraft.ai.neuralnetwork.vertex.Vertex;
import com.jstarcraft.core.utility.KeyValue;

/**
 * 图配置器
 * 
 * @author Administrator
 *
 */
public class GraphConfigurator {

	/** 节点映射(索引,名称,实例) */
	private Map<String, KeyValue<Integer, Vertex>> vertices;

	/** 边集合(索引,索引) */
	private Set<KeyValue<Integer, Integer>> edges;

	public GraphConfigurator() {
		vertices = new LinkedHashMap<>();
		edges = new LinkedHashSet<>();
	}

	public void connect(Vertex vertex, Collection<String> dependencies) {
		String name = vertex.getVertexName();
		int index = vertices.size();
		KeyValue<Integer, Vertex> vertexKeyValue = new KeyValue<>(index, vertex);
		if (vertices.putIfAbsent(name, vertexKeyValue) != null) {
			throw new IllegalArgumentException("节点冲突");
		}

		for (String dependency : dependencies) {
			vertexKeyValue = vertices.get(dependency);
			if (vertexKeyValue == null) {
				throw new IllegalArgumentException("节点缺失");
			}
			KeyValue<Integer, Integer> edgeKeyValue = new KeyValue<>(vertexKeyValue.getKey(), index);
			if (!edges.add(edgeKeyValue)) {
				throw new IllegalArgumentException("边冲突");
			}
		}
	}

	public void connect(Vertex vertex, String... dependencies) {
		String name = vertex.getVertexName();
		int index = vertices.size();
		KeyValue<Integer, Vertex> vertexKeyValue = new KeyValue<>(index, vertex);
		if (vertices.putIfAbsent(name, vertexKeyValue) != null) {
			throw new IllegalArgumentException("节点冲突");
		}

		for (String dependency : dependencies) {
			vertexKeyValue = vertices.get(dependency);
			if (vertexKeyValue == null) {
				throw new IllegalArgumentException("节点缺失");
			}
			KeyValue<Integer, Integer> edgeKeyValue = new KeyValue<>(vertexKeyValue.getKey(), index);
			if (!edges.add(edgeKeyValue)) {
				throw new IllegalArgumentException("边冲突");
			}
		}
	}

	/**
	 * Calculate a topological sort order for the vertices in the graph. Note
	 * that this is used for (a) working out what order to do forward pass, (b)
	 * what order to do backprop (i.e., reverse of this) (c) order to flatten
	 * parameters (and gradients)
	 * <p>
	 * Specifically, gradients/params/forward pass are executed on
	 * vertex[topologicalSortOrder[i]], for i=0..nVertices-1
	 */
	// https://en.wikipedia.org/wiki/Topological_sorting#Kahn.27s_algorithm
	public KeyValue<int[], KeyValue<List<Integer>[], List<Integer>[]>> calculateTopologicalOrder() {
		int position = 0;
		int size = vertices.size();
		// 拓扑排序
		int[] topologicalOrder = new int[size];

		// First: represent the graph more usefully as a
		// Map<Integer,Set<Integer>>, where map represents edges i -> j
		// key represents j, set is set of i (inputs) for vertices j
		// 节点的入度与出度
		Set<Integer>[] inputEdges = new Set[size];
		Set<Integer>[] outputEdges = new Set[size];
		for (int index = 0; index < size; index++) {
			inputEdges[index] = new HashSet<>();
			outputEdges[index] = new HashSet<>();
		}
		for (KeyValue<Integer, Integer> keyValue : edges) {
			inputEdges[keyValue.getValue()].add(keyValue.getKey());
			outputEdges[keyValue.getKey()].add(keyValue.getValue());
		}
		// 正向依赖与反向依赖
		List<Integer>[] forwardDependencies = new List[size];
		List<Integer>[] backwardDependencies = new List[size];
		for (int index = 0; index < size; index++) {
			forwardDependencies[index] = new ArrayList<>(inputEdges[index].size());
			backwardDependencies[index] = new ArrayList<>(outputEdges[index].size());
		}
		for (KeyValue<Integer, Integer> keyValue : edges) {
			// 必须排序
			forwardDependencies[keyValue.getValue()].add(keyValue.getKey());
			backwardDependencies[keyValue.getKey()].add(keyValue.getValue());
		}

		// Now: do topological sort
		// Set of all nodes with no incoming edges: (this would be: input
		// vertices)
		LinkedList<Integer> zeroEdges = new LinkedList<>();
		for (int index = 0; index < size; index++) {
			Set<Integer> dependencies = inputEdges[index];
			if (dependencies.isEmpty()) {
				zeroEdges.addLast(index);
			}
		}

		while (!zeroEdges.isEmpty()) {
			int index = zeroEdges.removeFirst();
			topologicalOrder[position++] = index; // Add to sorted list
			Set<Integer> dependencies = outputEdges[index];

			// Remove edges next -> vertexOuputsTo[...] from graph;
			for (Integer dependency : dependencies) {
				inputEdges[dependency].remove(index);
				if (inputEdges[dependency].isEmpty()) {
					zeroEdges.addLast(dependency);
				}
			}
		}

		// If any edges remain in the graph: graph has cycles:
		if (position != size) {
			throw new IllegalStateException("循环图");
		}
		return new KeyValue<>(topologicalOrder, new KeyValue<>(forwardDependencies, backwardDependencies));
	}

	public Map<String, KeyValue<Integer, Vertex>> getVertices() {
		return vertices;
	}

	public Set<KeyValue<Integer, Integer>> getEdges() {
		return edges;
	}

}
