package com.jstarcraft.ai.retrieval;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.NumericUtils;

import com.jstarcraft.ai.retrieval.annotation.RetrievalAnalyze;
import com.jstarcraft.ai.retrieval.annotation.RetrievalIndex;
import com.jstarcraft.ai.retrieval.annotation.RetrievalSort;
import com.jstarcraft.ai.retrieval.annotation.RetrievalStore;
import com.jstarcraft.ai.retrieval.annotation.RetrievalTerm;
import com.jstarcraft.core.codec.specification.CodecSpecification;
import com.jstarcraft.core.common.reflection.ReflectionUtility;
import com.jstarcraft.core.utility.ClassUtility;

public class RetrievalInformation<T> {

    private Map<Field, RetrievalConverter<Object>> converters;

    private RetrievalConverter<Object> numberConverter(String name, Class<?> clazz, RetrievalAnalyze analyze, RetrievalIndex index, RetrievalSort sort, RetrievalStore store) {
        clazz = ClassUtility.primitiveToWrapper(clazz);
        RetrievalConverter<Object> converter = null;
        if (Byte.class == clazz) {

        }
        if (Short.class == clazz) {

        }
        if (Integer.class == clazz) {
            converter = (data) -> {
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
            };
        }
        if (Long.class == clazz) {
            converter = (data) -> {
                Collection<IndexableField> fields = new LinkedList<>();
                if (index != null) {
                    fields.add(new LongPoint(name, (Long) data));
                }
                if (sort != null) {
                    fields.add(new NumericDocValuesField(name, (Long) data));
                }
                if (store != null) {
                    fields.add(new StoredField(name, (Long) data));
                }
                return fields;
            };
        }
        if (Float.class == clazz) {
            converter = (data) -> {
                Collection<IndexableField> fields = new LinkedList<>();
                if (index != null) {
                    fields.add(new FloatPoint(name, (Float) data));
                }
                if (sort != null) {
                    fields.add(new NumericDocValuesField(name, NumericUtils.floatToSortableInt((Float) data)));
                }
                if (store != null) {
                    fields.add(new StoredField(name, (Float) data));
                }
                return fields;
            };
        }
        if (Double.class == clazz) {
            converter = (data) -> {
                Collection<IndexableField> fields = new LinkedList<>();
                if (index != null) {
                    fields.add(new DoublePoint(name, (Double) data));
                }
                if (sort != null) {
                    fields.add(new NumericDocValuesField(name, NumericUtils.doubleToSortableLong((Double) data)));
                }
                if (store != null) {
                    fields.add(new StoredField(name, (Double) data));
                }
                return fields;
            };
        }
        return converter;
    }

    private RetrievalConverter<Object> stringConverter(String name, Class<?> clazz, RetrievalAnalyze analyze, RetrievalIndex index, RetrievalSort sort, RetrievalStore store) {
        FieldType type = new FieldType();
        if (index != null) {
            type.setIndexOptions(IndexOptions.DOCS);
        }

        if (analyze != null) {
            type.setTokenized(true);

            if (index != null) {
                RetrievalTerm negative = analyze.negative();
                if (negative.offset()) {
                    type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
                } else if (negative.position()) {
                    type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                } else if (negative.frequency()) {
                    type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
                }
            }

            RetrievalTerm positive = analyze.positive();
            if (positive.offset()) {
                type.setStoreTermVectorOffsets(true);
            }
            if (positive.position()) {
                type.setStoreTermVectorPositions(true);
            }
            if (positive.frequency()) {
                type.setStoreTermVectors(true);
            }
        } else if (sort != null) {
            // 注意:分词字段存储docValue没有意义
            type.setDocValuesType(DocValuesType.SORTED);
        }

        if (store != null) {
            type.setStored(true);
        }

        RetrievalConverter<Object> converter = (data) -> {
            Collection<IndexableField> fields = new LinkedList<>();
            fields.add(new org.apache.lucene.document.Field(name, (String) data, type));
            return fields;
        };
        return converter;
    }

    public RetrievalInformation(Class<T> clazz) {
        ReflectionUtility.doWithFields(clazz, (field) -> {
            Class<?> type = field.getType();

            RetrievalAnalyze analyze = field.getAnnotation(RetrievalAnalyze.class);
            RetrievalIndex index = field.getAnnotation(RetrievalIndex.class);
            RetrievalSort sort = field.getAnnotation(RetrievalSort.class);
            RetrievalStore store = field.getAnnotation(RetrievalStore.class);

            CodecSpecification specification = CodecSpecification.getSpecification(type);
            switch (specification) {
            case ARRAY:
                // TODO 思考数组与检索的关系?
                break;
            case COLLECTION:
                // TODO 思考集合与检索的关系?
                break;
            case INSTANT:
                break;
            case NUMBER:
                break;
            case OBJECT:
                break;
            case STRING:
                break;
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
