package com.jstarcraft.ai.modem;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.core.common.reflection.ReflectionUtility;

/**
 * 调制解调存取器
 * 
 * @author Birdy
 *
 */
public final class ModemAccessor {

	private final static Logger logger = LoggerFactory.getLogger(ModemAccessor.class);

	private final static Map<Class<?>, Map<String, Field>> properties = new HashMap<>();

	private synchronized static Field getField(Class<?> clazz, String name) {
		Map<String, Field> fields = properties.get(clazz);
		if (fields == null) {
			ModemDefinition annotation = clazz.getAnnotation(ModemDefinition.class);
			assert annotation != null;
			fields = new HashMap<>();
			for (String property : annotation.value()) {
				Field field = ReflectionUtility.findField(clazz, property);
				field.setAccessible(true);
				fields.put(property, field);
			}
		}
		return fields.get(name);
	}

	/**
	 * 根据指定的名称列表获取模型的属性
	 * 
	 * @param model
	 * @param names
	 * @return
	 */
	public static Object getProperty(Object model, String... names) {
		try {
			assert names.length > 0;
			for (int index = 0, size = names.length - 1; index < size; index++) {
				String name = names[index];
				Class<?> clazz = model.getClass();
				Field field = getField(clazz, name);
				model = field.get(model);
			}
			Class<?> clazz = model.getClass();
			Field field = getField(clazz, names[names.length - 1]);
			return field.get(model);
		} catch (Exception exception) {
			logger.error("获取模型属性异常", exception);
			throw new RuntimeException(exception);
		}
	}

	/**
	 * 根据指定的名称列表设置模型的属性
	 * 
	 * @param model
	 * @param property
	 * @param names
	 */
	public static void setProperty(Object model, Object property, String... names) {
		try {
			assert names.length > 0;
			for (int index = 0, size = names.length - 1; index < size; index++) {
				String name = names[index];
				Class<?> clazz = model.getClass();
				Field field = getField(clazz, name);
				model = field.get(model);
			}
			Class<?> clazz = model.getClass();
			Field field = getField(clazz, names[names.length - 1]);
			field.set(model, property);
		} catch (Exception exception) {
			logger.error("设置模型属性异常", exception);
			throw new RuntimeException(exception);
		}
	}

}
