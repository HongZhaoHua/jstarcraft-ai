package com.jstarcraft.ai.modem;

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
import com.jstarcraft.ai.modem.exception.ModemException;
import com.jstarcraft.core.codec.ContentCodec;
import com.jstarcraft.core.codec.csv.CsvContentCodec;
import com.jstarcraft.core.codec.json.JsonContentCodec;
import com.jstarcraft.core.codec.kryo.KryoContentCodec;
import com.jstarcraft.core.codec.protocolbufferx.ProtocolContentCodec;
import com.jstarcraft.core.codec.specification.CodecDefinition;
import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.reflection.ReflectionUtility;
import com.jstarcraft.core.common.reflection.Specification;
import com.jstarcraft.core.common.reflection.TypeUtility;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 调制解调编解码
 * 
 * @author Birdy
 *
 */
public enum ModemCodec {

	CSV(CsvContentCodec.class),

	JSON(JsonContentCodec.class),

	KRYO(KryoContentCodec.class),

	PROTOCOL_BUFFER_X(ProtocolContentCodec.class);

	private static final TypeFactory factory = TypeFactory.defaultInstance();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Constructor<? extends ContentCodec> codecConstructor;

	private final ContentCodec codec;

	ModemCodec(Class<? extends ContentCodec> mode) {
		try {
			this.codecConstructor = mode.getDeclaredConstructor(CodecDefinition.class);
			ReflectionUtility.makeAccessible(this.codecConstructor);
			Collection<Type> classes = new LinkedList<>();
			classes.add(ModemData.class);
			CodecDefinition definition = CodecDefinition.instanceOf(classes);
			this.codec = codecConstructor.newInstance(definition);
		} catch (Exception exception) {
			throw new ModemException(exception);
		}
	}

	private ModemData saveModel(Object model) {
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
				Specification specification = Specification.getSpecification(type);
				while (specification == Specification.ARRAY) {
					type = type.getComponentType();
					specification = Specification.getSpecification(type);
				}
				if (specification == Specification.BOOLEAN || specification == Specification.NUMBER || specification == Specification.STRING || specification == Specification.INSTANT) {
					valueData = codec.encode(clazz, model);
				} else {
					size = Array.getLength(model);
					ModemData[] datas = new ModemData[size];
					for (int index = 0; index < size; index++) {
						Object element = Array.get(model, index);
						datas[index] = saveModel(element);
					}
					valueData = codec.encode(ModemData[].class, datas);
				}
			} else if (Collection.class.isAssignableFrom(clazz)) {
				// 处理集合类型
				Collection<?> collection = Collection.class.cast(model);
				size = collection.size();
				ModemData[] datas = new ModemData[size];
				int index = 0;
				for (Object element : collection) {
					datas[index++] = saveModel(element);
				}
				valueData = codec.encode(ModemData[].class, datas);
			} else if (Map.class.isAssignableFrom(clazz)) {
				// 处理映射类型
				Map<Object, Object> map = Map.class.cast(model);
				size = map.size() * 2;
				ModemData[] datas = new ModemData[size];
				int index = 0;
				for (Entry<Object, Object> element : map.entrySet()) {
					datas[index++] = saveModel(element.getKey());
					datas[index++] = saveModel(element.getValue());
				}
				valueData = codec.encode(ModemData[].class, datas);
			} else {
				if (model instanceof ModemCycle) {
					ModemCycle.class.cast(model).beforeSave();
				}
				ModemDefinition annotation = clazz.getAnnotation(ModemDefinition.class);
				if (annotation != null) {
					size = annotation.value().length;
					ModemData[] datas = new ModemData[size];
					int index = 0;
					for (String name : annotation.value()) {
						Field field = ReflectionUtility.findField(clazz, name);
						field.setAccessible(true);
						datas[index++] = saveModel(field.get(model));
					}
					valueData = codec.encode(ModemData[].class, datas);
				} else {
					Collection<Type> classes = new LinkedList<>();
					classes.add(clazz);
					CodecDefinition definition = CodecDefinition.instanceOf(classes);
					ContentCodec codec = codecConstructor.newInstance(definition);
					valueData = codec.encode(clazz, model);
				}
			}

			ModemData content = new ModemData(keyData, valueData, size);
			return content;
		} catch (Exception exception) {
			logger.error(name(), exception);
			throw new ModemException(exception);
		}
	}

	private Object loadModel(ModemData content) {
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
				Specification specification = Specification.getSpecification(type);
				while (specification == Specification.ARRAY) {
					type = type.getComponentType();
					specification = Specification.getSpecification(type);
				}
				if (specification == Specification.BOOLEAN || specification == Specification.NUMBER || specification == Specification.STRING || specification == Specification.INSTANT) {
					Object array = codec.decode(clazz, valueData);
					return array;
				} else {
					Object array = Array.newInstance(clazz.getComponentType(), size);
					ModemData[] datas = (ModemData[]) codec.decode(ModemData[].class, valueData);
					for (int index = 0; index < size; index++) {
						Object element = loadModel(datas[index]);
						Array.set(array, index, element);
					}
					return array;
				}
			} else if (Collection.class.isAssignableFrom(clazz)) {
				// 处理集合类型
				Collection<Object> collection = Collection.class.cast(clazz.newInstance());
				ModemData[] datas = (ModemData[]) codec.decode(ModemData[].class, valueData);
				for (int index = 0; index < size; index++) {
					Object element = loadModel(datas[index]);
					collection.add(element);
				}
				return collection;
			} else if (Map.class.isAssignableFrom(clazz)) {
				// 处理映射类型
				Map<Object, Object> map = Map.class.cast(clazz.newInstance());
				ModemData[] datas = (ModemData[]) codec.decode(ModemData[].class, valueData);
				for (int index = 0; index < size;) {
					Object key = loadModel(datas[index++]);
					Object value = loadModel(datas[index++]);
					map.put(key, value);
				}
				return map;
			} else {
				ModemDefinition annotation = clazz.getAnnotation(ModemDefinition.class);
				Object model = null;
				if (annotation != null) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					ReflectionUtility.makeAccessible(constructor);
					model = constructor.newInstance();
					ModemData[] datas = (ModemData[]) codec.decode(ModemData[].class, valueData);
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
				if (model instanceof ModemCycle) {
					ModemCycle.class.cast(model).afterLoad();
				}
				return model;
			}
		} catch (Exception exception) {
			logger.error(name(), exception);
			throw new ModemException(exception);
		}
	}

	public byte[] encodeModel(Object model) {
		ModemData data = saveModel(model);
		return codec.encode(ModemData.class, data);
	}

	public Object decodeModel(byte[] content) {
		ModemData data = (ModemData) codec.decode(ModemData.class, content);
		return loadModel(data);
	}

}
