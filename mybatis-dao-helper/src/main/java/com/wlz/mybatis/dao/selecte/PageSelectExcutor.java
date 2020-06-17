package com.wlz.mybatis.dao.selecte;

import java.util.HashMap;
import java.util.List;

import com.wlz.mybatis.dao.condation.Limit;
import com.wlz.mybatis.dao.database.Table;

import com.wlz.mybatis.dao.ICondation;
import com.wlz.mybatis.dao.DataBase;
import com.wlz.mybatis.dao.TableMap;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月19日 下午5:01:00
 * 
 */
public class PageSelectExcutor extends SelectExcutor {
	private SelectExcutor selectExcutor;

	private Page page;
	public PageSelectExcutor(SelectExcutor select,Page page) {
		this.selectExcutor = select;
		this.page = page;
	}

	@Override
	public <T> List<T> select(SelectContext context) {
		// TODO Auto-generated method stub
		if (page != null&&context.getDaoConfig().getDataBase() == DataBase.MYSQL) {
			HashMap<String, Object> paramter = new HashMap<String, Object>();
			Table table = TableMap.getInstance().getTableMap(context.getType());
			ICondation cnd = context.getCondation();
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("select ");
			sqlBuilder.append(table.getId().getId());
			sqlBuilder.append(" from ");
			sqlBuilder.append(getTableName(context));
			if (cnd != null)
				sqlBuilder.append(cnd.toSql(context.getType(), paramter));
			page.setDaoMapper(context.getDaoMapper());
			page.setDaoConfig(context.getDaoConfig());
			page.setParamter(paramter);
			page.setSql(sqlBuilder.toString());
			
		}else if (page != null&&context.getDaoConfig().getDataBase() == DataBase.ORACLE){
			HashMap<String, Object> paramter = new HashMap<String, Object>();
			Table table = TableMap.getInstance().getTableMap(context.getType());
			ICondation cnd = context.getCondation();
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("select ");
			sqlBuilder.append(table.getId().getId());
			sqlBuilder.append(" from ");
			sqlBuilder.append(getTableName(context));
			if (cnd != null)
				sqlBuilder.append(cnd.toSql(context.getType(), paramter));
			page.setDaoMapper(context.getDaoMapper());
			page.setDaoConfig(context.getDaoConfig());
			page.setParamter(paramter);
			page.setSql(sqlBuilder.toString());
			
			
		}
		context.setCondation(new Limit(context.getCondation(), context.getDaoConfig().getDataBase(), (page.getPageNumber()*page.getPageSize()), page.getPageSize()));
		return selectExcutor.select(context);
	}
	
	@Override
	protected String getTableName(SelectContext context) {
		// TODO Auto-generated method stub
		return selectExcutor.getTableName(context);
	}
}
