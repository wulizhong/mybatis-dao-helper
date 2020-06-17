package com.wlz.mybatis.dao.update;

import com.wlz.mybatis.dao.mapper.DaoMapper;

import com.wlz.mybatis.dao.ICondation;
import com.wlz.mybatis.dao.DaoConfig;
import com.wlz.mybatis.dao.DaoContext;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月24日 上午11:33:54 
 * 
 */
public class UpdateContext extends DaoContext{

	public UpdateContext(Object target,DaoConfig daoConfig,DaoMapper daoMapper,ICondation condation){
		super(daoConfig, daoMapper,condation);
		this.target = target;
	}
	
	public UpdateContext(Object target, DaoConfig daoConfig, DaoMapper daoMapper) {
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
