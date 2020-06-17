package com.wlz.mybatis.dao.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wlz.mybatis.dao.database.Table;
import com.wlz.mybatis.dao.impl.DefaultObjectCreator;

import com.wlz.mybatis.dao.ObjectCreator;
import com.wlz.mybatis.dao.TableMap;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月17日 下午4:48:57
 * 
 */
public class Toolkit {
	
	/** The CGLIB class separator: "$$" */
	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	public static boolean isBasicType(Class<?> t) {

		return t == String.class || t == Long.class || t == long.class || t == Integer.class || t == int.class || t == Short.class || t == short.class
				|| t == Byte.class || t == byte.class || t == char.class || t == StringBuilder.class || t == StringBuffer.class || t == BigInteger.class
				|| t == BigDecimal.class || t == java.sql.Date.class|| t == java.util.Date.class || t == Double.class || t == double.class || t == Float.class || t == float.class;

	}

	public static boolean isListType(Class<?> t) {
		if (t == List.class) {
			return true;
		}
		Class<?>[] types = t.getInterfaces();
		if (types != null) {
			for (Class<?> type : types) {
				boolean isList = isListType(type);
				if (isList) {
					return true;
				}
			}
		}
		return false;
	}

	public static Class<?> getGenericType(Field f) {
		Type genericType = f.getGenericType();
		if (genericType == null)
			return null;
		if (genericType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericType;
			Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
			return genericClazz;
		}
		return null;
	}
	
	public static <T> T convertMapToObject(Class<T> clazz, Map<String, Object> result) {
		return convertMapToObject(clazz,result,new DefaultObjectCreator());
	}

	public static <T> T convertMapToObject(Class<T> clazz, Map<String, Object> result, ObjectCreator creator) {
		T obj = creator.create(clazz);
		Table table = TableMap.getInstance().getTableMap(clazz);
		if (result != null) {
			for (Map.Entry<String, Object> me : result.entrySet()) {
				
				String key = me.getKey();
				Method setMethod = null;
				if(table!=null) {
					Field field = table.getField(key);
					if(field == null) {
						field = table.getField(key.toLowerCase());
					}
					if(field == null) {
						field = table.getFieldByOriginalFieldName(key);
						if(field == null) {
							field = table.getFieldByOriginalFieldName(key.toLowerCase());
						}
					}
					if(field!=null) {
						setMethod = ReflectionUtils.findSetMethodByField(clazz, field);
					}
				}
				if(setMethod == null) {
					String fieldName = FieldNameUtils.underlineToHump(me.getKey(), true);
					setMethod = ReflectionUtils.findSetMethodByFieldName(clazz, fieldName);
				}
				
				
				if (setMethod == null || setMethod.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				Class<?> paramterType = setMethod.getParameterTypes()[0];
				Object value = me.getValue();
				try {
					if (value.getClass()!=paramterType) {
						if (paramterType == Long.class || paramterType == long.class) {
							setMethod.invoke(obj, Long.parseLong(value.toString()));
						} else if (paramterType == Integer.class || paramterType == int.class) {
							setMethod.invoke(obj, Integer.parseInt(value.toString()));
						} else if (paramterType == Short.class || paramterType == short.class) {
							setMethod.invoke(obj, Short.parseShort(value.toString()));
						} else if (paramterType == Byte.class || paramterType == byte.class) {
							setMethod.invoke(obj, Byte.parseByte(value.toString()));
						} else if (paramterType == Double.class || paramterType == double.class) {
							setMethod.invoke(obj, Double.parseDouble(value.toString()));
						} else if (paramterType == Float.class || paramterType == float.class) {
							setMethod.invoke(obj, Float.parseFloat(value.toString()));
						}else if (paramterType == BigDecimal.class) {
							setMethod.invoke(obj, new BigDecimal(value.toString()));
						}else if (paramterType == BigInteger.class) {
							setMethod.invoke(obj, new BigInteger(value.toString()));
						}else {
							setMethod.invoke(obj, value);
						}

					} else {
						setMethod.invoke(obj, value);
					}
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	
	public static Map<String, Object> convertObjectToMap(Object obj) {
		if (obj == null)
			return null;
		HashMap<String, Object> map = new HashMap<>();
		Table table;
		if (isCglibProxy(obj)) {
			table = TableMap.getInstance().getTableMap(obj.getClass().getSuperclass());
		} else {
			table = TableMap.getInstance().getTableMap(obj.getClass());
		}
		try {
			for (Map.Entry<String, Field> entry : table.getOriginalFieldMap().entrySet()) {
				Field field = entry.getValue();
				Method getMethod = ReflectionUtils.findGetMethodByField(obj.getClass(), field);
				if (getMethod == null || getMethod.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				map.put(entry.getKey(), getMethod.invoke(obj, new Object[] {}));
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static <T> List<T> convertMapToObjectList(Class<T> clazz, List<Map<String, Object>> mapList, ObjectCreator creator) {
		ArrayList<T> objList = new ArrayList<T>();

		for (Map<String, Object> map : mapList) {
			objList.add(convertMapToObject(clazz, map, creator));
		}

		return objList;
	}
	
	public static <T> List<T> convertMapToObjectList(Class<T> clazz, List<Map<String, Object>> mapList) {
		return convertMapToObjectList(clazz,mapList,new DefaultObjectCreator());
	}

	/**
	 * Check whether the given object is a CGLIB proxy.
	 * 
	 * @param object
	 *            the object to check
	 * @see org.springframework.aop.support.AopUtils#isCglibProxy(Object)
	 */
	public static boolean isCglibProxy(Object object) {
		return isCglibProxyClass(object.getClass());
	}

	/**
	 * Check whether the specified class is a CGLIB-generated class.
	 * 
	 * @param clazz
	 *            the class to check
	 */
	public static boolean isCglibProxyClass(Class<?> clazz) {
		return (clazz != null && isCglibProxyClassName(clazz.getName()));
	}

	/**
	 * Check whether the specified class name is a CGLIB-generated class.
	 * 
	 * @param className
	 *            the class name to check
	 */
	public static boolean isCglibProxyClassName(String className) {
		return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
	}
}
