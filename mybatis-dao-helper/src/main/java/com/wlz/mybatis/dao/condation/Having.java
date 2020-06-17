package com.wlz.mybatis.dao.condation;

import com.wlz.mybatis.dao.ICondation;

import java.util.Map;


/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午4:24:49 
 * 
 */
public class Having implements ICondation{

	
	public Having(String name,String op,Object value){
		
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
