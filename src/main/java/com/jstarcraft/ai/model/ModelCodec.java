package com.jstarcraft.ai.model;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jstarcraft.ai.model.exception.ModelException;
import com.jstarcraft.core.codec.ContentCodec;
import com.jstarcraft.core.codec.CsvContentCodec;
import com.jstarcraft.core.codec.JsonContentCodec;
import com.jstarcraft.core.codec.KryoContentCodec;
import com.jstarcraft.core.codec.ProtocolContentCodec;
import com.jstarcraft.core.codec.specification.CodecDefinition;
import com.jstarcraft.core.codec.specification.CodecSpecification;
import com.jstarcraft.core.utility.JsonUtility;
import com.jstarcraft.core.utility.ReflectionUtility;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.core.utility.TypeUtility;

/**
 * 模式编解码
 * 
 * @author Birdy
 *
 */
public enum ModelCodec {

	CSV(CsvContentCodec.class),

	JSON(JsonContentCodec.class),

	KRYO(KryoContentCodec.class),

	PROTOCOL_BUFFER_X(ProtocolContentCodec.class);

	private static final TypeFactory factory = TypeFactory.defaultInstance();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Constructor<? extends ContentCodec> codecConstructor;

	private final ContentCodec codec;

	ModelCodec(Class<? extends ContentCodec> mode) {
		try {
			this.codecConstructor = mode.getDeclaredConstructor(CodecDefinition.class);
			this.codecConstructor.setAccessible(true);
			Collection<Type> classes = new LinkedList<>();
			classes.add(ModelData.class);
			CodecDefinition definition = CodecDefinition.instanceOf(classes);
			this.codec = codecConstructor.newInstance(definition);
		} catch (Exception exception) {
			throw new ModelException(exception);
		}
	}

	private ModelData saveModel(Object model) {
		try {
			Class<?> clazz = model.getClass();
			JavaType java = factory.constructType(clazz);
			String json = JsonUtility.object2String(java).replaceAll("<.*>", "");
			byte[] keyData = json.getBytes(StringUtility.CHARSET);
			byte[] valueData = null;
			int size = 0;

			if (clazz.isArray()) {
				// 处理数组类型
				Class<?> type = clazz.getComponentType();
				CodecSpecification specification = CodecSpecification.getSpecification(type);
				while (specification == CodecSpecification.ARRAY) {
					type = type.getComponentType();
					specification = CodecSpecification.getSpecification(type);
				}
				if (specification == CodecSpecification.BOOLEAN || specification == CodecSpecification.NUMBER || specification == CodecSpecification.STRING || specification == CodecSpecification.INSTANT) {
					valueData = codec.encode(clazz, model);
				} else {
					size = Array.getLength(model);
					ModelData[] datas = new ModelData[size];
					for (int index = 0; index < size; index++) {
						Object element = Array.get(model, index);
						datas[index] = saveModel(element);
					}
					valueData = codec.encode(ModelData[].class, datas);
				}
			} else if (Collection.class.isAssignableFrom(clazz)) {
				// 处理集合类型
				Collection<?> collection = Collection.class.cast(model);
				size = collection.size();
				ModelData[] datas = new ModelData[size];
				int index = 0;
				for (Object element : collection) {
					datas[index++] = saveModel(element);
				}
				valueData = codec.encode(ModelData[].class, datas);
			} else if (Map.class.isAssignableFrom(clazz)) {
				// 处理映射类型
				Map<Object, Object> map = Map.class.cast(model);
				size = map.size() * 2;
				ModelData[] datas = new ModelData[size];
				int index = 0;
				for (Entry<Object, Object> element : map.entrySet()) {
					datas[index++] = saveModel(element.getKey());
					datas[index++] = saveModel(element.getValue());
				}
				valueData = codec.encode(ModelData[].class, datas);
			} else {
				if (model instanceof ModelCycle) {
					ModelCycle.class.cast(model).beforeSave();
				}
				ModelDefinition annotation = clazz.getAnnotation(ModelDefinition.class);
				if (annotation != null) {
					size = annotation.value().length;
					ModelData[] datas = new ModelData[size];
					int index = 0;
					for (String name : annotation.value()) {
						Field field = ReflectionUtility.findField(clazz, name);
						field.setAccessible(true);
						datas[index++] = saveModel(field.get(model));
					}
					valueData = codec.encode(ModelData[].class, datas);
				} else {
					Collection<Type> classes = new LinkedList<>();
					classes.add(clazz);
					CodecDefinition definition = CodecDefinition.instanceOf(classes);
					ContentCodec codec = codecConstructor.newInstance(definition);
					valueData = codec.encode(clazz, model);
				}
			}

			ModelData content = new ModelData(keyData, valueData, size);
			return content;
		} catch (Exception exception) {
			logger.error(name(), exception);
			throw new ModelException(exception);
		}
	}

	private Object loadModel(ModelData content) {
		try {
			byte[] keyData = content.getKeyData();
			byte[] valueData = content.getValueData();
			int size = content.getSize();
			String json = new String(keyData, StringUtility.CHARSET);
			JavaType java = JsonUtility.string2Object(json, JavaType.class);
			Class<?> clazz = TypeUtility.getRawType(JsonUtility.java2Type(java), null);

			if (clazz.isArray()) {
				// 处理数组类型
				Class<?> type = clazz.getComponentType();
				CodecSpecification specification = CodecSpecification.getSpecification(type);
				while (specification == CodecSpecification.ARRAY) {
					type = type.getComponentType();
					specification = CodecSpecification.getSpecification(type);
				}
				if (specification == CodecSpecification.BOOLEAN || specification == CodecSpecification.NUMBER || specification == CodecSpecification.STRING || specification == CodecSpecification.INSTANT) {
					Object array = codec.decode(clazz, valueData);
					return array;
				} else {
					Object array = Array.newInstance(clazz.getComponentType(), size);
					ModelData[] datas = (ModelData[]) codec.decode(ModelData[].class, valueData);
					for (int index = 0; index < size; index++) {
						Object element = loadModel(datas[index]);
						Array.set(array, index, element);
					}
					return array;
				}
			} else if (Collection.class.isAssignableFrom(clazz)) {
				// 处理集合类型
				Collection<Object> collection = Collection.class.cast(clazz.newInstance());
				ModelData[] datas = (ModelData[]) codec.decode(ModelData[].class, valueData);
				for (int index = 0; index < size; index++) {
					Object element = loadModel(datas[index]);
					collection.add(element);
				}
				return collection;
			} else if (Map.class.isAssignableFrom(clazz)) {
				// 处理映射类型
				Map<Object, Object> map = Map.class.cast(clazz.newInstance());
				ModelData[] datas = (ModelData[]) codec.decode(ModelData[].class, valueData);
				for (int index = 0; index < size;) {
					Object key = loadModel(datas[index++]);
					Object value = loadModel(datas[index++]);
					map.put(key, value);
				}
				return map;
			} else {
				ModelDefinition annotation = clazz.getAnnotation(ModelDefinition.class);
				Object model = null;
				if (annotation != null) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					model = constructor.newInstance();
					ModelData[] datas = (ModelData[]) codec.decode(ModelData[].class, valueData);
					int index = 0;
					for (String name : annotation.value()) {
						Field field = ReflectionUtility.findField(clazz, name);
						field.setAccessible(true);
						field.set(model, loadModel(datas[index++]));
					}
				} else {
					Collection<Type> classes = new LinkedList<>();
					classes.add(clazz);
					CodecDefinition definition = CodecDefinition.instanceOf(classes);
					ContentCodec codec = codecConstructor.newInstance(definition);
					model = codec.decode(clazz, valueData);
				}
				if (model instanceof ModelCycle) {
					ModelCycle.class.cast(model).afterLoad();
				}
				return model;
			}
		} catch (Exception exception) {
			logger.error(name(), exception);
			throw new ModelException(exception);
		}
	}

	public byte[] encodeModel(Object model) {
		ModelData data = saveModel(model);
		return codec.encode(ModelData.class, data);
	}

	public Object decodeModel(byte[] content) {
		ModelData data = (ModelData) codec.decode(ModelData.class, content);
		return loadModel(data);
	}

}
