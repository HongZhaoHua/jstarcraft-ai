package com.jstarcraft.ai.search;

import java.util.Arrays;

import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;

public class Rank {

	/**
	 * This is the percentage of time that a random surfer follows the structure of
	 * the web.
	 */
	private float alpha = 0.8F;

	/** This is the error tolerance for convergence */
	private float epsilon = 0.001F;

	float[] pR;

	private MathMatrix hMatrix;

	public Rank(MathMatrix hMatrix) {
		this.hMatrix = hMatrix;
	}

	public float[] build() throws Exception {

		// check the results
		// getH().print();

		findPageRank(alpha, epsilon);
		return Arrays.copyOf(pR, pR.length);
	}

	public void findPageRank(float alpha, float epsilon) {

		// A counter for our iterations
		int k = 0;

		// auxiliary variable
		MathMatrix matrixH = hMatrix;

		// The H matrix has size nxn and the PageRank vector has size n
		int n = matrixH.getColumnSize();

		// auxiliary variable
		float inv_n = 1F / n;

		// A dummy variable that holds our error --
		// arbitrarily set to an initial value of 1
		float error = 1F;

		// This holds the values of the PageRank vector
		pR = new float[n];

		// This is a copy of the PageRank vector from the previous iteration
		// The only reason that we need this is for evaluating the error
		float[] tmpPR = new float[n];

		// Set the initial values (ad hoc)
		for (int i = 0; i < n; i++) {
			pR[i] = inv_n;
		}

		/*
		 * Book Section 2.3 -- Altering the H matrix: Dangling nodes
		 */
		float[][] dNodes = getDanglingNodeMatrix();

		/**
		 * TODO: 2.5 -- Altering the G matrix: Teleportation (Book Section 2.3)
		 * 
		 * The following code defines the contribution of the dangling nodes, i.e.
		 * jumping randomly on a page that is not connected with the one that our surfer
		 * is currently viewing
		 * 
		 * Notice that it is the same for all pages. An interesting variation of the
		 * algorithm would introduce a "teleportation" contribution that relates the
		 * probability of an arbitrary transition to the degree of interest that a user
		 * has for the content of a page.
		 * 
		 * Exercise: Could that be done? If so, how? What problems can you see with that
		 * variation?
		 */
		float tNodes = (1 - alpha) * inv_n;

		// Replace the H matrix with the G matrix
		matrixH.iterateElement(MathCalculator.SERIAL, (scalar) -> {
			scalar.setValue(alpha * scalar.getValue() + dNodes[scalar.getRow()][scalar.getColumn()] + tNodes);
		});

		// Iterate until convergence.
		// We have found the PageRank values if our error is smaller than
		// epsilon
		while (error >= epsilon) {

			// Make a copy of the PageRank vector before we update it
			for (int i = 0; i < n; i++) {
				tmpPR[i] = pR[i];
			}

			float dummy = 0F;
			// Now we get the next point in the iteration
			for (int i = 0; i < n; i++) {

				dummy = 0;

				for (int j = 0; j < n; j++) {

					dummy += pR[j] * hMatrix.getValue(j, i);
				}

				pR[i] = dummy;
			}

			// Get the error, so that we can check convergence
			error = norm(pR, tmpPR);

			// Display the progress
			System.out.println("\n Iteration: " + k + ",   PageRank convergence error: " + error);
			for (int i = 0; i < n; i++) {
				System.out.println("Index: " + i + " -->  PageRank: " + pR[i]);
			}
			// increase the value of the counter by one
			k++;
		}
	}

	private float norm(float[] a, float[] b) {
		float norm = 0F;

		int n = a.length;

		for (int i = 0; i < n; i++) {
			norm += Math.abs(a[i] - b[i]);
		}
		return norm;
	}

	/**
	 * TODO: 2.4 -- Altering the G matrix: Dangling nodes (Book Section 2.3)
	 * 
	 * The following code defines the contribution of the dangling nodes, i.e. nodes
	 * that do not link to any other node.
	 * 
	 * Notice that the 1/n contribution is arbitrary. Given that we have no other
	 * information about the random surfer's habits or preferences, the 1/n value is
	 * fair. However, an interesting variation would take into account some
	 * statistics related to the number of visits a page gets.
	 * 
	 * Exercise: Change the algorithm, so that a dangling node's contribution
	 * depends on some page visit statistic. You can practice with a small set of
	 * pages and examine the effect on the ranking of the pages.
	 */
	private float[][] getDanglingNodeMatrix() {

		MathMatrix matrixH = hMatrix;

		int n = matrixH.getColumnSize();

		float inv_n = 1F / n;

		// The dangling node vector
		int[] dangling = new int[n];
		for (int index = 0; index < n; index++) {
			if (matrixH.getRowVector(index).getSum(false) == 0D) {
				dangling[index] = 1;
			}
		}

		float[][] dNodes = new float[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				if (dangling[i] == 0) {
					dNodes[i][j] = 0;
				} else {
					dNodes[i][j] = alpha * inv_n;
				}
			}
		}

		return dNodes;
	}

	/**
	 * @return the alpha
	 */
	public float getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return the epsilon
	 */
	public float getEpsilon() {
		return epsilon;
	}

	/**
	 * @param epsilon
	 *            the epsilon to set
	 */
	public void setEpsilon(float epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * @return the pR
	 */
	public double getPageRank(int i) {

		return pR[i];
	}
}
