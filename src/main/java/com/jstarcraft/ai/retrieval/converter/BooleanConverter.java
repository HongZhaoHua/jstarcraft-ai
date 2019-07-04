package com.jstarcraft.ai.retrieval.converter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;

import com.jstarcraft.ai.retrieval.annotation.RetrievalAnalyze;
import com.jstarcraft.ai.retrieval.annotation.RetrievalIndex;
import com.jstarcraft.ai.retrieval.annotation.RetrievalSort;
import com.jstarcraft.ai.retrieval.annotation.RetrievalStore;
import com.jstarcraft.ai.retrieval.exception.RetrievalException;
import com.jstarcraft.core.common.reflection.TypeUtility;
import com.jstarcraft.core.utility.ClassUtility;

/**
 * 布尔转换器
 * 
 * @author Birdy
 *
 */
public class BooleanConverter implements RetrievalConverter {

    @Override
    public Collection<IndexableField> convert(String name, Type type, Object data, RetrievalAnalyze analyze, RetrievalIndex index, RetrievalSort sort, RetrievalStore store) {
        Class<?> clazz = TypeUtility.getRawType(type, null);
        clazz = ClassUtility.primitiveToWrapper(clazz);
        if (AtomicBoolean.class == clazz) {

        }
        if (Boolean.class == clazz) {
            Collection<IndexableField> fields = new LinkedList<>();
            if (index != null) {
                fields.add(new IntPoint(name, (Integer) data));
            }
            if (sort != null) {
                fields.add(new NumericDocValuesField(name, (Integer) data));
            }
            if (store != null) {
                fields.add(new StoredField(name, (Integer) data));
            }
            return fields;
        }
        throw new RetrievalException();
    }

}
