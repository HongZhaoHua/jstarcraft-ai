package com.jstarcraft.ai.math.algorithm.text;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.core.utility.StringUtility;

public class NgramTestCase {

    @Test
    public void testList() {
        ListNgram<String> ngram = new ListNgram<>(3, "to", "be", "or", "not", "to", "be");
        List<String> words = Arrays.asList("to be or", "be or not", "or not to", "not to be");
        int count = 0;
        for (List<String> terms : ngram) {
            String word = StringUtility.join(terms, " ");
            Assert.assertEquals(words.get(count), word);
            count++;
        }
        Assert.assertEquals(ngram.getSize(), count);
    }

    @Test
    public void testCharacter() {
        CharacterNgram ngram = new CharacterNgram(3, "jstarcraft");
        List<String> words = Arrays.asList("jst", "sta", "tar", "arc", "rcr", "cra", "raf", "aft");
        int count = 0;
        for (CharSequence terms : ngram) {
            Assert.assertEquals(words.get(count), terms);
            count++;
        }
        Assert.assertEquals(ngram.getSize(), count);
    }

}
