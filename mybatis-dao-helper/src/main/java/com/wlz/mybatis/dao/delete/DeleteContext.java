package com.wlz.mybatis.dao.delete;

import com.wlz.mybatis.dao.mapper.DaoMapper;

import com.wlz.mybatis.dao.ICondation;
import com.wlz.mybatis.dao.DaoConfig;
import com.wlz.mybatis.dao.DaoContext;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月24日 下午3:01:30 
 * 
 */
public class DeleteContext extends DaoContext{
	
	private Object target;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	
	public DeleteContext(Object target,DaoConfig daoConfig,DaoMapper daoMapper,ICondation condation){
		super(daoConfig, daoMapper,condation);
		this.target = target;
	}
	
	public DeleteContext(Object target, DaoConfig daoConfig, DaoMapper daoMapper) {
		super(daoConfig, daoMapper);
		// TODO Auto-generated constructor stub
		this.target = target;
	}
}
