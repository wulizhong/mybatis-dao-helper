package com.wlz.mybatis.dao.selecte;

import com.wlz.mybatis.dao.mapper.DaoMapper;

import com.wlz.mybatis.dao.ICondation;
import com.wlz.mybatis.dao.DaoConfig;
import com.wlz.mybatis.dao.DaoContext;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午5:11:52 
 * 
 */
public class SelectContext extends DaoContext{

	private Class<?> type;
	
	public SelectContext(Class<?> type, DaoConfig daoConfig, DaoMapper daoMapper, ICondation condation) {
		super(daoConfig, daoMapper, condation);
		// TODO Auto-generated constructor stub
		this.type = type;
	}

//	private Page page;
//
//	public Page getPage() {
//		return page;
//	}
//
//	public void setPage(Page page) {
//		this.page = page;
//	}
	
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}
