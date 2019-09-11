package com.jstarcraft.ai.data.attribute;

import java.util.Arrays;

import org.redisson.Redisson;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RScript;
import org.redisson.api.RScript.Mode;
import org.redisson.api.RScript.ReturnType;

/**
 * 基于Redis的连续属性
 * 
 * @author Birdy
 *
 */
public class RedisQuantityAttribute<T extends Number> implements QuantityAttribute<T> {

    private final static String maximumLua = "local maximum = redis.call('get', KEYS[1]); if (maximum) then if (ARGV[1] > maximum) then maximum = ARGV[1]; redis.call('set', KEYS[1], maximum); end; else maximum = ARGV[1]; redis.call('set', KEYS[1], maximum); end; return maximum;";

    private final static String minimumLua = "local minimum = redis.call('get', KEYS[1]); if (minimum) then if (ARGV[1] < minimum) then minimum = ARGV[1]; redis.call('set', KEYS[1], minimum); end; else minimum = ARGV[1]; redis.call('set', KEYS[1], minimum); end; return minimum;";

    private final static String maximumSuffix = "_maximum";

    private final static String minimumSuffix = "_minimum";

    /** 属性名称 */
    private String name;

    /** 属性类型 */
    private Class<T> type;

    private String maximumKey;

    private String minimumKey;

    private float maximumCache;

    private float minimumCache;

    private RScript script;

    private String maximumSignature;

    private String minimumSignature;

    private RAtomicDouble maximumAtomic;

    private RAtomicDouble minimumAtomic;

    public RedisQuantityAttribute(String name, Class<T> type, Redisson redisson) {
        this.name = name;
        this.type = type;
        this.maximumKey = name + maximumSuffix;
        this.minimumKey = name + minimumSuffix;
        this.maximumCache = Float.NEGATIVE_INFINITY;
        this.minimumCache = Float.POSITIVE_INFINITY;
        this.script = redisson.getScript();
        this.maximumSignature = script.scriptLoad(maximumLua);
        this.minimumSignature = script.scriptLoad(minimumLua);
        this.maximumAtomic = redisson.getAtomicDouble(maximumKey);
        this.minimumAtomic = redisson.getAtomicDouble(minimumKey);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public float convertData(T data) {
        float feature = data.floatValue();
        if (feature > maximumCache) {
            Number number = script.evalSha(Mode.READ_WRITE, maximumSignature, ReturnType.INTEGER, Arrays.asList(maximumKey), feature);
            maximumCache = number.floatValue();
        }
        if (feature < minimumCache) {
            Number number = script.evalSha(Mode.READ_WRITE, minimumSignature, ReturnType.INTEGER, Arrays.asList(minimumKey), feature);
            minimumCache = number.floatValue();
        }
        return feature;
    }

    @Override
    public float getMaximum() {
        return (float) maximumAtomic.get();
    }

    @Override
    public float getMinimum() {
        return (float) minimumAtomic.get();
    }

}
