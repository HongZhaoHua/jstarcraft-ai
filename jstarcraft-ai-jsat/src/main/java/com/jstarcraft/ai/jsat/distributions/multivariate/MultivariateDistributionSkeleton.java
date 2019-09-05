
package com.jstarcraft.ai.jsat.distributions.multivariate;

import com.jstarcraft.ai.jsat.linear.Vec;

/**
 * Common class for implementing a multivariate distribution. A number of
 * methods are pre implemented, building off of the implementation of the
 * remaining methods. <br>
 * Note: the default implementation for the multithreaded methods calls the non
 * threaded version of the method. The exception to this is the
 * {@link #setUsingData(com.jstarcraft.ai.jsat.DataSet, java.util.concurrent.ExecutorService) }
 * method, which calls
 * {@link #setUsingData(java.util.List, java.util.concurrent.ExecutorService) }
 * 
 * @author Edward Raff
 */
public abstract class MultivariateDistributionSkeleton implements MultivariateDistribution {

    private static final long serialVersionUID = 4080753806798149915L;

    @Override
    public double logPdf(Vec x) {
        double logPDF = Math.log(pdf(x));
        if (Double.isInfinite(logPDF) && logPDF < 0)// log(0) == -Infinty
            return -Double.MAX_VALUE;
        return logPDF;
    }

    @Override
    abstract public MultivariateDistribution clone();
}
