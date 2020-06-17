package com.wlz.mybatis.dao.selecte;

import java.util.List;
import java.util.Map;

import com.wlz.mybatis.dao.mapper.DaoMapper;
import com.wlz.mybatis.dao.util.CollectionUtils;

import com.wlz.mybatis.dao.Constant;
import com.wlz.mybatis.dao.DaoConfig;
import com.wlz.mybatis.dao.DataBase;


/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月17日 上午9:58:11
 * 
 */
public class Page {

	public static int DEFAULT_PAGE_SIZE = 20;
	
	private int pageNumber;
	private int pageSize;
	private long totalCount = -1;
	
	private DaoMapper daoMapper;
	
	private DaoConfig daoConfig;
	
	private String sql;
	private Map<String, Object> paramter;

	public Page(int pageNumber,int pageSize){
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
	
	public Page(){
		this(1);
	}
	
	public Page(int pageNumber){
		this.pageNumber = pageNumber;
		this.pageSize = DEFAULT_PAGE_SIZE;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Page setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public long getTotalCount() {
		if(totalCount == -1){
			String sql;
			if(daoConfig.getDataBase() == DataBase.MYSQL) {
				sql = "select count(*) from ("+this.sql+") as temp_table";
			}else if(daoConfig.getDataBase() == DataBase.ORACLE) {
				sql = "select count(*) from ("+this.sql+") ";
			}else {
				throw new RuntimeException("此数据库未支持分页");
			}

			paramter.put(Constant.SQL_SYMBOL, sql);
			List<Map<String, Object>> result = daoMapper.select(paramter);
			if(!CollectionUtils.isEmpty(result)){
//				Map<String, Object> countMap = result.get(0);
				
				totalCount =  Long.parseLong(result.get(0).values().toArray()[0].toString());
			}
		}
		return totalCount;
	}

//	protected void setTotalCount(int totalCount) {
//		this.totalCount = totalCount;
//	}

	


	protected void setSql(String sql) {
		this.sql = sql;
	}

	protected void setDaoMapper(DaoMapper daoMapper) {
		this.daoMapper = daoMapper;
	}

	protected void setParamter(Map<String, Object> paramter) {
		this.paramter = paramter;
	}

	protected void setDaoConfig(DaoConfig daoConfig) {
		this.daoConfig = daoConfig;
	}

	
}
