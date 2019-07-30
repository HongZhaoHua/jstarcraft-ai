package com.jstarcraft.ai.model.neuralnetwork;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.jstarcraft.ai.model.neuralnetwork.vertex.Vertex;
import com.jstarcraft.core.utility.Integer2IntegerKeyValue;
import com.jstarcraft.core.utility.KeyValue;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * 图配置器
 * 
 * @author Birdy
 *
 */
public class GraphConfigurator {

    /** 节点映射(索引,名称,实例) */
    private Map<String, KeyValue<Integer, Vertex>> vertices;

    /** 边集合(索引,索引) */
    private Set<Integer2IntegerKeyValue> edges;

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
            Integer2IntegerKeyValue edgeKeyValue = new Integer2IntegerKeyValue(vertexKeyValue.getKey(), index);
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
            Integer2IntegerKeyValue edgeKeyValue = new Integer2IntegerKeyValue(vertexKeyValue.getKey(), index);
            if (!edges.add(edgeKeyValue)) {
                throw new IllegalArgumentException("边冲突");
            }
        }
    }

    /**
     * 拓扑排序算法
     * 
     * @return
     */
    // https://en.wikipedia.org/wiki/Topological_sorting#Kahn.27s_algorithm
    public KeyValue<int[], KeyValue<IntList[], IntList[]>> calculateTopologicalOrder() {
        int position = 0;
        int size = vertices.size();
        // 拓扑排序
        int[] topologicalOrder = new int[size];

        // First: represent the graph more usefully as a
        // Map<Integer,Set<Integer>>, where map represents edges i -> j
        // key represents j, set is set of i (inputs) for vertices j
        // 节点的入度与出度
        IntSet[] inputEdges = new IntSet[size];
        IntSet[] outputEdges = new IntSet[size];
        for (int index = 0; index < size; index++) {
            inputEdges[index] = new IntOpenHashSet();
            outputEdges[index] = new IntOpenHashSet();
        }
        for (Integer2IntegerKeyValue edge : edges) {
            inputEdges[edge.getValue()].add(edge.getKey());
            outputEdges[edge.getKey()].add(edge.getValue());
        }
        // 正向依赖与反向依赖
        IntList[] forwardDependencies = new IntList[size];
        IntList[] backwardDependencies = new IntList[size];
        for (int index = 0; index < size; index++) {
            forwardDependencies[index] = new IntArrayList(inputEdges[index].size());
            backwardDependencies[index] = new IntArrayList(outputEdges[index].size());
        }
        for (Integer2IntegerKeyValue edge : edges) {
            // 必须排序
            forwardDependencies[edge.getValue()].add(edge.getKey());
            backwardDependencies[edge.getKey()].add(edge.getValue());
        }

        // Now: do topological sort
        // Set of all nodes with no incoming edges: (this would be: input
        // vertices)
        IntArrayFIFOQueue zeroEdges = new IntArrayFIFOQueue(size);
        for (int index = 0; index < size; index++) {
            IntSet dependencies = inputEdges[index];
            if (dependencies.isEmpty()) {
                zeroEdges.enqueue(index);
            }
        }

        while (!zeroEdges.isEmpty()) {
            int index = zeroEdges.dequeueInt();
            topologicalOrder[position++] = index; // Add to sorted list
            IntSet dependencies = outputEdges[index];

            // Remove edges next -> vertexOuputsTo[...] from graph;
            for (int dependency : dependencies) {
                inputEdges[dependency].remove(index);
                if (inputEdges[dependency].isEmpty()) {
                    zeroEdges.enqueue(dependency);
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

    public Set<Integer2IntegerKeyValue> getEdges() {
        return edges;
    }

}
