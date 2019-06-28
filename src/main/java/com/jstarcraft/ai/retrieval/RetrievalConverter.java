package com.jstarcraft.ai.retrieval;

import java.util.Collection;

import org.apache.lucene.index.IndexableField;

public interface RetrievalConverter<T> {

    Collection<IndexableField> convert(T data);
    
}
