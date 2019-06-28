package com.jstarcraft.ai.retrieval;

import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import com.jstarcraft.ai.retrieval.annotation.RetrievalAnalyze;
import com.jstarcraft.ai.retrieval.annotation.RetrievalIndex;
import com.jstarcraft.ai.retrieval.annotation.RetrievalSort;
import com.jstarcraft.ai.retrieval.annotation.RetrievalStore;
import com.jstarcraft.core.common.reflection.ReflectionUtility;

public class RetrievalInformation<T> {

    private Map<Field, RetrievalConverter<Object>> converters;

    public RetrievalInformation(Class<T> clazz) {
        ReflectionUtility.doWithFields(clazz, (field) -> {
            Class<?> type = field.getType();

            RetrievalAnalyze analyze = field.getAnnotation(RetrievalAnalyze.class);
            RetrievalIndex index = field.getAnnotation(RetrievalIndex.class);
            RetrievalSort sort = field.getAnnotation(RetrievalSort.class);
            RetrievalStore store = field.getAnnotation(RetrievalStore.class);

            // RetrievalAnalyze仅支持Reader,String,TokenStream类型
            if (analyze != null) {
                if (Reader.class.isAssignableFrom(type)) {

                }
                if (String.class.isAssignableFrom(type)) {

                }
                if (TokenStream.class.isAssignableFrom(type)) {

                }
            }
            
            if (sort != null) {
                if (Reader.class.isAssignableFrom(type)) {

                }
                if (String.class.isAssignableFrom(type)) {

                }
                if (TokenStream.class.isAssignableFrom(type)) {

                }
            }

            if (store != null) {
                if (Reader.class.isAssignableFrom(type)) {

                }
                if (String.class.isAssignableFrom(type)) {

                }
                if (TokenStream.class.isAssignableFrom(type)) {

                }
            }
        });
    }

    public Document convert(T object) {
        Document document = new Document();

        try {
            for (Entry<Field, RetrievalConverter<Object>> term : converters.entrySet()) {
                Field field = term.getKey();
                Object data = field.get(object);
                RetrievalConverter<Object> converter = term.getValue();
                for (IndexableField indexable : converter.convert(data)) {
                    document.add(indexable);
                }
            }
        } catch (Exception exception) {
            // TODO
        }

        return document;
    }

}
