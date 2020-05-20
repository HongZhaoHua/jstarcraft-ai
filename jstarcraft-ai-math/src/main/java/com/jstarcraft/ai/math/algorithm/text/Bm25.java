package com.jstarcraft.ai.math.algorithm.text;

/**
 * BM25
 * 
 * @author Birdy
 *
 */
public class Bm25 {

	private InverseDocumentFrequency idf;

	private TermFrequency[] tfs;

	/** k控制着词频饱和度,值越小饱和度变化越快,值越大饱和度变化越慢 */
	private float k = 1.2F;

	/** b控制着词频归一化所起的作用,0.0会完全禁用归一化,1.0会完全启用归一化 */
	private float b = 0.75F;

	private float l;

	public Bm25(float k, float b, InverseDocumentFrequency idf, TermFrequency... tfs) {
		this.idf = idf;
		this.tfs = tfs;
		this.k = k;
		this.b = b;
		float l = 0F;
		for (TermFrequency tf : tfs) {
			l += tf.getLength();
		}
		l /= tfs.length;
		this.l = l;
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
			correlation += (idf * (k + 1F) * tf) / (k * (1F - b + b * l) + tf);
		}
		return correlation;
	}

}
