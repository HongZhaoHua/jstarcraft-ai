package com.jstarcraft.ai.search;

import org.apache.lucene.document.Document;

import com.jstarcraft.core.common.identification.IdentityObject;

public class LuceneUtility {

    /**
     * 将对象转换为文档
     * 
     * @param object
     * @return
     */
    public static Document convert(IdentityObject<? extends Comparable<?>> object) {
        Document document = new Document();
        Class<? extends IdentityObject> clazz = object.getClass();

        return document;
    }

}
