package com.wlz.mybatis.dao.condation;

import java.util.Map;

import com.wlz.mybatis.dao.Condation;
import com.wlz.mybatis.dao.TableMap;
import com.wlz.mybatis.dao.database.Table;


/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午4:24:49 
 * 
 */
public class Where extends Condation{

	

	private RawSql rawSql;
	
	private String name;
	
	private String op;
	
	private Object value;
	
	
	protected Where(String name,String op,Object value){
		this.name = name;
		this.op = op;
		this.value = value;
	}
	
	protected Where(RawSql rawSql){
		this.rawSql = rawSql;
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		int id = IdGrenerator.grenerateId();
		StringBuilder builder = new StringBuilder();
		TableMap tableMap = TableMap.getInstance();
		builder.append(" where ");
		if(rawSql == null){
			Table table = tableMap.getTableMap(clazz);
			builder.append(table.getDataBaseField(name));
			builder.append(" ");
			builder.append(op);
			builder.append(" ");
			builder.append("#{");
			builder.append(name+id);
			builder.append("}");
			paramter.put(name+id, value);
		}else{
			builder.append(this.rawSql.toSql(clazz, paramter));
		}
		

		
		return builder.toString();
	}
	@Override
	public And and(String name,String op,Object value){
		And and = new And(this,name,op,value);
		return and;
	}
	
	
	@Override
	public And and(Segment segment){
		And and = new And(this,segment);
		return and;
	}
	@Override
	public Or or(String name,String op,Object value){
		Or or = new Or(this,name,op,value);
		return or;
	}
	@Override
	public Or or(Segment segment){
		Or or = new Or(this,segment);
		return or;
	}
	@Override
	public Or or(RawSql rawSql){
		Or or = new Or(this,rawSql);
		return or;
	}
	@Override
	public And and(RawSql rawSql){
		And and = new And(this,rawSql);
		return and;
	}
	@Override
	public OrderBy orderBy(String name,String value){
		OrderBy ob = new OrderBy(this,name,value);
		return ob;
	}
	@Override
	public OrderBy orderBy(String name){
		OrderBy ob = new OrderBy(this,name,Cnd.ASC);
		return ob;
	}
	
	@Override
	public And and(String name, Object value) {
		// TODO Auto-generated method stub
		return and(name,Cnd.EQ,value);
	}

	@Override
	public Or or(String name, Object value) {
		// TODO Auto-generated method stub
		return or(name,Cnd.EQ,value);
	}
}
