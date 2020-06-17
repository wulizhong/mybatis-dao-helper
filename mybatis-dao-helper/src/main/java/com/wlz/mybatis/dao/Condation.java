package com.wlz.mybatis.dao;

import com.wlz.mybatis.dao.condation.And;
import com.wlz.mybatis.dao.condation.Or;
import com.wlz.mybatis.dao.condation.OrderBy;
import com.wlz.mybatis.dao.condation.RawSql;
import com.wlz.mybatis.dao.condation.Segment;

public abstract class Condation implements ICondation{

	public abstract And and(String name,String op,Object value);
	
	public abstract And and(String name,Object value);
	
	public abstract And and(Segment segment);
	
	public abstract Or or(String name,String op,Object value);
	
	public abstract Or or(String name,Object value);
	
	public abstract Or or(Segment segment);
	
	public abstract Or or(RawSql rawSql);
	
	public abstract And and(RawSql rawSql);
	
	public abstract OrderBy orderBy(String name,String value);
	
	public abstract OrderBy orderBy(String name);
}
