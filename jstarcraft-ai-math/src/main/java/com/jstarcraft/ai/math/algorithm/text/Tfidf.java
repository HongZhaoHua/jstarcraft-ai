package com.jstarcraft.ai.math.algorithm.text;

/**
 * TF-IDF
 * 
 * @author Birdy
 *
 */
// TODO 考虑迁移到NLP项目
public class Tfidf {

	private InverseDocumentFrequency idf;

	private TermFrequency[] tfs;

	public Tfidf(InverseDocumentFrequency idf, TermFrequency... tfs) {
		this.idf = idf;
		this.tfs = tfs;
	}

	@Deprecated
	// TODO 准备整合为统一接口
	public float getCorrelation(int index, int... document) {
		float correlation = 0F;
		for (int term : document) {
			float tf = this.tfs[index].getValue(term);
			if (tf == Float.NaN) {
				continue;
			}
			float idf = this.idf.getValue(term);
			if (idf == Float.NaN) {
				continue;
			}
			correlation += (idf * tf);
		}
		return correlation;
	}

}
