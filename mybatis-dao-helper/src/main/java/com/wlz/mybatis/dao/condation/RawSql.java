package com.wlz.mybatis.dao.condation;

import com.wlz.mybatis.dao.ICondation;

import java.util.Map;

public class RawSql implements ICondation{

	private String sql;
	
	public RawSql(String sql){
		this.sql = sql;
	}
	
	@Override
	public String toSql(Class<?> clazz, Map<String, Object> paramter) {
		// TODO Auto-generated method stub
		return this.sql;
	}

}
