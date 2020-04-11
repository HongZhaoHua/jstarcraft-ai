package com.jstarcraft.ai.math.algorithm.text;

import java.util.Iterator;

public class CharacterNgram implements Ngram<CharSequence> {

    private int grams;

    private CharSequence terms;

    public CharacterNgram(int grams, CharSequence terms) {
        assert grams <= terms.length();
        this.grams = grams;
        this.terms = terms;
    }

    @Override
    public int getSize() {
        return terms.length() - grams + 1;
    }

    @Override
    public Iterator<CharSequence> iterator() {
        return new NgramIterator();
    }

    private class NgramIterator implements Iterator<CharSequence> {

        private int size = terms.length();

        private int cursor;

        @Override
        public boolean hasNext() {
            return cursor + grams <= size;
        }

        @Override
        public CharSequence next() {
            CharSequence ngram = terms.subSequence(cursor, cursor + grams);
            cursor++;
            return ngram;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
