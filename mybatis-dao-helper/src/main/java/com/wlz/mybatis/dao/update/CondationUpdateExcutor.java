package com.wlz.mybatis.dao.update;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.ibatis.jdbc.SQL;
import com.wlz.mybatis.dao.database.Table;
import com.wlz.mybatis.dao.util.ReflectionUtils;
import com.wlz.mybatis.dao.util.Toolkit;

import com.wlz.mybatis.dao.Constant;
import com.wlz.mybatis.dao.TableMap;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2017年11月09日 下午03:33:23 
 * 
 */
public class CondationUpdateExcutor extends UpdateExcutor{

	private Class<?> clazz;
	
	private String[] fields;
	
	private Object[] values;
	
	public CondationUpdateExcutor(Class<?> clazz,String[] fields,Object[] values){
		this.clazz = clazz;
		this.fields = fields;
		this.values = values;
	}
	
	public int update(UpdateContext context){
		int count = -1;
		SQL sql = new SQL();
		sql.UPDATE(getTableName(context));
		HashMap<String, Object> paramter = new HashMap<>();
		Table table = TableMap.getInstance().getTableMap(clazz);
		for (int i = 0;i<fields.length;i++) {
			String fieldName = table.getDataBaseField(fields[i]);
			sql = sql.SET(fieldName+ " = #{" + fields[i] + i + "}");
			paramter.put(fields[i] + i, values[i]);
		}
		paramter.put(Constant.SQL_SYMBOL, sql.toString()+context.getCondation().toSql(clazz, paramter));
		
		count = context.getDaoMapper().update(paramter);
		
		return count;
	}
	
	protected String getTableName(UpdateContext context) {
		Table table = TableMap.getInstance().getTableMap(clazz);
		return table.getName();
	}
}
