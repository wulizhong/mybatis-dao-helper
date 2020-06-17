package com.wlz.mybatis.dao.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

	public static Object getValue(Object obj,Field field){
		Object result = null;
		field.setAccessible(true);
		try {
			result = field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(false);
		return result;
	}
	
	public static void setValue(Object obj,Field field,Object value){
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(false);
	}
	
	/**
	 * @param clzz
	 * @return
	 */
	public static List<Field> findAllFields(Class<?> clazz) {
		List<Field> fieldList = new ArrayList<>();
		while (clazz != null) {
			fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return fieldList;
	}
	
	/**
	 * @param clzz
	 * @return
	 */
	public static Field findField(Class<?> clazz,String fieldName) {
		Field field = null;
		while (clazz != null) {
			try {
				field = clazz.getField(fieldName);
				if(field!=null) {
					return field;
				}
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
//				if (logger.isDebugEnabled()) {
//					logger.debug("未找到对应属性", e);
//				}
			}
			
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return field;
	}

	/**
	 * 
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method findSetMethodByField(Class<?> clazz, Field field) {
		Method setMethod = null;
		while (clazz != null) {
			try {
				setMethod = clazz.getDeclaredMethod("set" + FieldNameUtils.toUpperCaseFirstOne(field.getName()),
						field.getType());
				if (setMethod != null)
					return setMethod;
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
//				if (logger.isDebugEnabled()) {
//					logger.debug("未找到对应方法", e);
//				}
			}
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return setMethod;
	}

	public static void setValueBySetMethod(Object obj,Field field,Object value) {
		Method setMethod = ReflectionUtils.findSetMethodByField(obj.getClass(), field);
		try {
			setMethod.invoke(obj, new Object[]{value});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setValueBySetMethod(Object obj,String field,Object value) {
		Method setMethod = ReflectionUtils.findSetMethodByFieldName(obj.getClass(), field);
		try {
			setMethod.invoke(obj, new Object[]{value});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Object getValueByGetMethod(Object obj,Field field) {
		Method getMethod = ReflectionUtils.findGetMethodByField(obj.getClass(), field);
		try {
			return getMethod.invoke(obj, new Object[]{});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object getValueByGetMethod(Object obj,String field) {
		Method getMethod = ReflectionUtils.findGetMethodByFieldName(obj.getClass(), field);
		try {
			return getMethod.invoke(obj, new Object[]{});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Method findSetMethodByFieldName(Class<?> clazz, String fieldName) {
		Field field = null;
		while (clazz != null) {
			try {
				field = clazz.getDeclaredField(fieldName);
				if (field != null) {
					return findSetMethodByField(clazz, field);
				}
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
//				if (logger.isDebugEnabled()) {
//					logger.debug("未找到对应字段", e);
//				}
			}
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return null;
	}
	/**
	 * 获取字段对应的无参get方法
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method findGetMethodByField(Class<?> clazz, Field field) {
		Method getMethod = null;
		while (clazz != null) {
			try {
				getMethod = clazz.getDeclaredMethod("get" + FieldNameUtils.toUpperCaseFirstOne(field.getName()),
						new Class<?>[] {});
				if (getMethod != null)
					return getMethod;
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
//				if (logger.isDebugEnabled()) {
//					logger.debug("未找到对应方法", e);
//				}
			}
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return getMethod;
	}
	
	public static Method findGetMethodByFieldName(Class<?> clazz, String fieldName) {
		Field field = null;
		while (clazz != null) {
			try {
				field = clazz.getDeclaredField(fieldName);
				if (field != null) {
					return findGetMethodByField(clazz, field);
				}
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
//				if (logger.isDebugEnabled()) {
//					logger.debug("未找到对应字段", e);
//				}
			}
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return null;
	}
	
}
