package com.wlz.mybatis.dao.insert;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import com.wlz.mybatis.dao.database.Id;
import com.wlz.mybatis.dao.database.Table;
import com.wlz.mybatis.dao.exception.SequenceIsNullException;
import com.wlz.mybatis.dao.util.ReflectionUtils;
import com.wlz.mybatis.dao.util.StringUtils;
import com.wlz.mybatis.dao.util.Toolkit;

import com.wlz.mybatis.dao.Constant;
import com.wlz.mybatis.dao.DataBase;
import com.wlz.mybatis.dao.TableMap;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月20日 下午2:13:21
 * 
 */
public class InsertExcutor {

	public int insert(InsertContext context) {
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		SQL sql = new SQL();
		sql = sql.INSERT_INTO(getTableName(context));
		HashMap<String, Object> paramter = new HashMap<>();
		for (String fieldStr : table.getDataBaseFieldMap().values()) {
			Field field = table.getField(fieldStr);
			if (field == table.getId().getField())
				continue;
			Object value = ReflectionUtils.getValue(target, field);
			if (value == null)
				continue;
			sql = sql.VALUES(fieldStr, "#{" + field.getName() + "}");
			paramter.put(field.getName(), value);
		}
		
		
		Id id = table.getId();
		int count = -1;
		DataBase dataBase = context.getDaoConfig().getDataBase();
		if(id.isAutoGenerateId() && (dataBase == DataBase.MYSQL||dataBase == DataBase.SQL_SERVER)){
			paramter.put(Constant.SQL_SYMBOL, sql.toString());
			Map<String, Object> result = context.getDaoMapper().insertUseReturnGeneratedKeys(paramter);
			if(result!=null) {
				Field idField = id.getField();
				Object resultId =  result.get("id");
				if (resultId != null) {
					Class<?> type = (Class<?>) idField.getGenericType();
					if (type == int.class || type == Integer.class) {
						ReflectionUtils.setValue(target, idField, Integer.parseInt(resultId.toString()));
					} else if (type == long.class || type == Long.class) {
						ReflectionUtils.setValue(target, idField, Long.parseLong(resultId.toString()));
					} else if(type == BigDecimal.class){
						ReflectionUtils.setValue(target, idField, new BigDecimal(resultId.toString()));
					} else if(type == BigInteger.class){
						ReflectionUtils.setValue(target, idField, new BigInteger(resultId.toString()));
					}
				}
				
				if (result.get("count") != null) {
					count = (int) result.get("count");
				}
			}
		}else if(id.isAutoGenerateId() &&dataBase == DataBase.ORACLE){
			
			if(StringUtils.isEmpty(id.getSequence())){
				throw new SequenceIsNullException("oracle Sequence is null");
			}
			
			StringBuilder sqlBuilder = new StringBuilder();
			
			sqlBuilder.append("select ");
			sqlBuilder.append(id.getSequence());
			sqlBuilder.append(".");
			sqlBuilder.append("nextval as id");
			sqlBuilder.append(" from dual");
			Map<String, Object> param = new HashMap<>();
			param.put(Constant.SQL_SYMBOL, sqlBuilder.toString());
			List<Map<String, Object>> resultList = context.getDaoMapper().select(param);
			Object idValue = resultList.get(0).values().toArray()[0];
			Field idField = id.getField();
			Class<?> idType = idField.getType();
			if(idType == BigDecimal.class){
				if(idValue instanceof BigDecimal){
					ReflectionUtils.setValue(target, idField, idValue);
				}else{
					ReflectionUtils.setValue(target, idField, new BigDecimal(idValue.toString()));
				}
			}else if(idType == Long.class || idType == long.class){
				ReflectionUtils.setValue(target, idField, new Long(idValue.toString()));
			}else if(idType == Integer.class || idType == int.class){
				ReflectionUtils.setValue(target, idField, new Integer(idValue.toString()));
			}
//			ReflectionUtils.setValue(target, idField, idValue);
			sql = sql.VALUES(id.getId(), "#{" + idField.getName() + "}");
			paramter.put(idField.getName(), idValue);
			paramter.put(Constant.SQL_SYMBOL, sql.toString());
			count = context.getDaoMapper().insert(paramter);
		}else{
			count = context.getDaoMapper().insert(paramter);
		}
		
		return count;
	}
	
	protected String getTableName(InsertContext context) {
		Object target = context.getTarget();
		Class<?> targetType = Toolkit.isCglibProxy(target) ? target.getClass().getSuperclass() : target.getClass();
		Table table = TableMap.getInstance().getTableMap(targetType);
		return table.getName();
	}
}
