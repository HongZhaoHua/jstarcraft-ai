package com.jstarcraft.ai.search.converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVPrinter;
import org.apache.lucene.index.IndexableField;

import com.jstarcraft.ai.search.annotation.RetrievalAnalyze;
import com.jstarcraft.ai.search.annotation.RetrievalIndex;
import com.jstarcraft.ai.search.annotation.RetrievalSort;
import com.jstarcraft.ai.search.annotation.RetrievalStore;
import com.jstarcraft.core.codec.csv.CsvReader;
import com.jstarcraft.core.codec.csv.CsvWriter;
import com.jstarcraft.core.codec.specification.ClassDefinition;
import com.jstarcraft.core.common.reflection.Specification;
import com.jstarcraft.core.common.reflection.TypeUtility;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 映射转换器
 * 
 * @author Birdy
 *
 */
public class MapConverter implements RetrievalConverter {

    @Override
    public Collection<IndexableField> convert(String name, Type type, Object data, RetrievalAnalyze analyze, RetrievalIndex index, RetrievalSort sort, RetrievalStore store) {
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
        Type[] types = parameterizedType.getActualTypeArguments();
        return null;
    }

}
