package com.jstarcraft.ai.jsat.clustering;

/**
 * A base foundation that provides an implementation of the methods that return
 * a list of lists for the clusterings using their int array counterparts.
 * 
 * @author Edward Raff
 */
public abstract class KClustererBase extends ClustererBase implements KClusterer {

    private static final long serialVersionUID = 2542432122353325407L;

    @Override
    abstract public KClusterer clone();
}
