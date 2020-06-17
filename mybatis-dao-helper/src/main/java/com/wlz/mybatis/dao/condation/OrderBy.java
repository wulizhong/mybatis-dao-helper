package com.wlz.mybatis.dao.condation;

import com.wlz.mybatis.dao.Condation;
import com.wlz.mybatis.dao.ICondation;
import com.wlz.mybatis.dao.TableMap;

import java.util.Map;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午4:50:13 
 * 
 */
public class OrderBy extends Condation{

	private String name;

	private String value;
	
	private ICondation condation;
	
	public OrderBy(ICondation condation,String name, String value){
		this.condation = condation;
		this.name = name;
		this.value = value;
	}
	
	public OrderBy(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		if(condation!=null){
			builder.append(condation.toSql(clazz,paramter));
			if(builder.indexOf("ORDER BY")==-1){
				builder.append(" ORDER BY ");
			}else{
				builder.append(" , ");
			}
		}else{
			builder.append(" ORDER BY ");
		}
		
		builder.append(TableMap.getInstance().getTableMap(clazz).getDataBaseField(name));
		builder.append(" ");
		builder.append(value);
		return builder.toString();
	}

	public OrderBy orderBy(String name,String value){
		OrderBy ob = new OrderBy(this,name,value);
		return ob;
	}
	
	public OrderBy orderBy(String name){
		OrderBy ob = new OrderBy(this,name,Cnd.ASC);
		condation = ob;
		return ob;
	}

	@Override
	public And and(String name, String op, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public And and(String name, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public And and(Segment segment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Or or(String name, String op, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Or or(String name, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Or or(Segment segment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Or or(RawSql rawSql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public And and(RawSql rawSql) {
		// TODO Auto-generated method stub
		return null;
	}
}
