package jstarcraft.ai.math.algorithm.lsh;

import com.jstarcraft.ai.math.structure.vector.MathVector;
import com.jstarcraft.core.common.hash.HashFunction;

/**
 * A hash function can hash a vector of arbitrary dimensions to an integer
 * representation. The hash function needs to be locality sensitive to work in
 * the locality sensitive hash scheme. Meaning that vectors that are 'close'
 * according to some metric have a high probability to end up with the same
 * hash.
 * 
 * @author Joren Six
 */
public interface VectorHashFunction extends HashFunction<MathVector> {

}
