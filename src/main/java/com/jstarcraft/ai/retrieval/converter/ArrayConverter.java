package com.jstarcraft.ai.retrieval.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.lucene.index.IndexableField;

import com.jstarcraft.ai.retrieval.annotation.RetrievalAnalyze;
import com.jstarcraft.ai.retrieval.annotation.RetrievalIndex;
import com.jstarcraft.ai.retrieval.annotation.RetrievalSort;
import com.jstarcraft.ai.retrieval.annotation.RetrievalStore;
import com.jstarcraft.core.common.reflection.TypeUtility;

/**
 * 数组转换器
 * 
 * @author Birdy
 *
 */
public class ArrayConverter implements RetrievalConverter {

    @Override
    public Collection<IndexableField> convert(String name, Type type, Object data, RetrievalAnalyze analyze, RetrievalIndex index, RetrievalSort sort, RetrievalStore store) {
        Class<?> componentClass = null;
        Type componentType = null;
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
            componentType = genericArrayType.getGenericComponentType();
            componentClass = TypeUtility.getRawType(componentType, null);
        } else {
            Class<?> clazz = TypeUtility.getRawType(type, null);
            componentType = clazz.getComponentType();
            componentClass = clazz.getComponentType();
        }
        return null;
    }

}
