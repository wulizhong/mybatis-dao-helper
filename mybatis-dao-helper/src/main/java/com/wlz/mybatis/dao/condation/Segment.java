package com.wlz.mybatis.dao.condation;

import com.wlz.mybatis.dao.ICondation;

import java.util.Map;

public class Segment implements ICondation{

	private ICondation condation;
	
	protected Segment(ICondation condation) {
		this.condation = condation;
	}
	
	@Override
	public String toSql(Class<?> clazz, Map<String, Object> paramter) {
		// TODO Auto-generated method stub
		return condation.toSql(clazz, paramter);
	}

}
