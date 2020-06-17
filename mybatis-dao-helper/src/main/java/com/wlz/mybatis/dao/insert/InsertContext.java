package com.wlz.mybatis.dao.insert;

import com.wlz.mybatis.dao.mapper.DaoMapper;

import com.wlz.mybatis.dao.DaoConfig;
import com.wlz.mybatis.dao.DaoContext;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月20日 下午2:04:52 
 * 
 */
public class InsertContext extends DaoContext{

	public InsertContext(Object target, DaoConfig daoConfig, DaoMapper daoMapper) {
		super(daoConfig, daoMapper);
		// TODO Auto-generated constructor stub
		this.target = target;
	}
	
	private Object target;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	
}
