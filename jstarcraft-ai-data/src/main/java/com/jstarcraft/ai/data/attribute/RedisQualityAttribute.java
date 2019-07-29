package com.jstarcraft.ai.data.attribute;

import java.util.Arrays;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RScript;
import org.redisson.api.RScript.Mode;
import org.redisson.api.RScript.ReturnType;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;

/**
 * 基于Redis的离散属性
 * 
 * @author Birdy
 *
 */
public class RedisQualityAttribute<T extends Comparable<T>> implements QualityAttribute<T> {

	private final static String indexLua = "local index = redis.call('hget', KEYS[1], ARGV[1]); if (not index) then index = redis.call('incr', KEYS[2]) - 1; redis.call('hset', KEYS[1], ARGV[1], index); end; return index;";

	private final static String indexSuffix = "_indexes";

	private final static String sizeSuffix = "_size";

	/** 属性名称 */
	private String name;

	/** 属性类型 */
	private Class<T> type;

	private String indexKey;

	private String sizeKey;

	private ConcurrentLinkedHashMap<T, Integer> indexCache;

	private RScript script;

	private String indexSignature;

	private RAtomicLong sizeAtomic;

	public RedisQualityAttribute(String name, Class<T> type, int minimunSize, int maximunSize, Redisson redisson) {
		this.name = name;
		this.type = type;
		this.indexKey = name + indexSuffix;
		this.sizeKey = name + sizeSuffix;
		Builder<T, Integer> builder = new Builder<>();
		builder.initialCapacity(minimunSize);
		builder.maximumWeightedCapacity(maximunSize);
		this.indexCache = builder.build();
		this.script = redisson.getScript();
		this.indexSignature = script.scriptLoad(indexLua);
		this.sizeAtomic = redisson.getAtomicLong(sizeKey);
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
	public int convertData(T data) {
		Integer index = indexCache.get(data);
		if (index == null) {
			Number number = script.evalSha(Mode.READ_WRITE, indexSignature, ReturnType.INTEGER, Arrays.asList(indexKey, sizeKey), data);
			index = number.intValue();
			indexCache.put(data, index);
		}
		return index;
	}

	@Override
	public int getSize() {
		return (int) sizeAtomic.get();
	}

}
