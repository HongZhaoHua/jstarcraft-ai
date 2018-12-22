package com.jstarcraft.ai.text;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.text.Ngram;
import com.jstarcraft.core.utility.StringUtility;

public class NgramTestCase {

	@Test
	public void test() {
		Ngram<String> ngram = new Ngram<>(3, "to", "be", "or", "not", "to", "be");
		List<String> words = Arrays.asList("to be or", "be or not", "or not to", "not to be");
		int count = 0;
		for (List<String> terms : ngram) {
			String word = StringUtility.join(terms, " ");
			Assert.assertEquals(words.get(count), word);
			count++;
		}
		Assert.assertEquals(ngram.getSize(), count);
	}

}
