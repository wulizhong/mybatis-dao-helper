package com.wlz.mybatis.dao.selecte;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wlz.mybatis.dao.condation.Cnd;
import com.wlz.mybatis.dao.database.Many;
import com.wlz.mybatis.dao.database.ManyToMany;
import com.wlz.mybatis.dao.database.One;
import com.wlz.mybatis.dao.database.Table;
import com.wlz.mybatis.dao.impl.DefaultObjectCreator;
import com.wlz.mybatis.dao.impl.FieldFilterProxyObjectCreator;
import com.wlz.mybatis.dao.util.CollectionUtils;
import com.wlz.mybatis.dao.util.ReflectionUtils;
import com.wlz.mybatis.dao.util.Toolkit;

import com.wlz.mybatis.dao.ICondation;
import com.wlz.mybatis.dao.Constant;
import com.wlz.mybatis.dao.FetchType;
import com.wlz.mybatis.dao.FieldFilter;
import com.wlz.mybatis.dao.TableMap;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月19日 下午4:50:14
 * 
 */
public class FieldFilterRelationSelectExcutor extends SelectExcutor {

	private SelectExcutor selectExcutor;

	private FieldFilter filter;

	public FieldFilterRelationSelectExcutor( FieldFilter filter,SelectExcutor select) {
		this.selectExcutor = select;
		this.filter = filter;
	}

	@Override
	public <T> List<T> select(SelectContext context) {
		// TODO Auto-generated method stub

		context.setObjectCreator(new FieldFilterProxyObjectCreator(context, filter));

		List<T> result = selectExcutor.select(context);
		if (result != null && result.size() > 0) {
			Table table = TableMap.getInstance().getTableMap(context.getType());
			for (T obj : result) {
				for (One one : table.getOneMap().values()) {
					Field field = one.getField();

					Object value = null;
					value = ReflectionUtils.getValue(obj, field);
					if (one != null && one.getFetchType() == FetchType.IMMEDIATELY && value == null&&isProxy(field.getName())) {
						Object oneId = ReflectionUtils.getValue(obj,  table.getFieldByOriginalFieldName(one.getOneIdFieldName()));
						if (oneId != null) {
							ICondation cnd = Cnd.where(table.getId().getId(), "=", oneId);
							SelectContext selectContext = new SelectContext(field.getType(), context.getDaoConfig(), context.getDaoMapper(), cnd);
							SelectExcutor select = new SelectExcutor();
							List<?> resultList = select.select(selectContext);
							if (!CollectionUtils.isEmpty(resultList)) {
								value = resultList.get(0);
							}
							ReflectionUtils.setValue(obj, field, value);
						}
					}
				}

				for (Many many : table.getManyMap().values()) {
					Field field = many.getField();
					Object value = null;
					value = ReflectionUtils.getValue(obj, field);
					if (many != null && many.getFetchType() == FetchType.IMMEDIATELY && value == null&&isProxy(field.getName())) {

						Field idField = table.getId().getField();
						Object idObj = ReflectionUtils.getValue(obj, idField);
						Field manyField = many.getField();
						Class<?> clazz = Toolkit.getGenericType(manyField);
						if (clazz == null) {
							continue;
						}
						SelectContext selectContext = new SelectContext(clazz, context.getDaoConfig(), context.getDaoMapper(), Cnd.where(
								many.getManyIdFieldName(), "=", idObj));
						SelectExcutor select = new SelectExcutor();
						Object list = select.select(selectContext);

						ReflectionUtils.setValue(obj, manyField, list);

					}

				}

				for (ManyToMany manyToMany : table.getManyToManyMap().values()) {
					Field field = manyToMany.getField();
					Object value = null;
					value = ReflectionUtils.getValue(obj, field);
					if (manyToMany != null && manyToMany.getFetchType() == FetchType.IMMEDIATELY && value == null&&isProxy(field.getName())) {

						Field manyToManyField = manyToMany.getField();
						Object fieldValue = ReflectionUtils.getValue(obj, manyToManyField);
						if (fieldValue != null) {
							continue;
						}

						Field idField = table.getId().getField();
						Object idObj = ReflectionUtils.getValue(obj, idField);

						Class<?> clazz = Toolkit.getGenericType(manyToManyField);
						if (clazz == null) {
							continue;
						}

						StringBuffer sqlBuffer = new StringBuffer();

						Table resultTable = TableMap.getInstance().getTableMap(clazz);

						sqlBuffer.append("select ");
						sqlBuffer.append(resultTable.getName());
						sqlBuffer.append(".* from ");
						sqlBuffer.append(resultTable.getName());
						sqlBuffer.append(",");
						sqlBuffer.append(manyToMany.getRelationTableName());

						sqlBuffer.append(" where ");
						sqlBuffer.append(resultTable.getName());
						sqlBuffer.append(".");
						sqlBuffer.append(resultTable.getId().getId());
						sqlBuffer.append(" = ");
						sqlBuffer.append(manyToMany.getRelationTableName());
						sqlBuffer.append(".");
						sqlBuffer.append(manyToMany.getToId());
						sqlBuffer.append(" and ");
						sqlBuffer.append(manyToMany.getRelationTableName());
						sqlBuffer.append(".");
						sqlBuffer.append(manyToMany.getFromId());
						sqlBuffer.append(" = #{");
						sqlBuffer.append(manyToMany.getFromId());
						sqlBuffer.append("}");

						HashMap<String, Object> paramter = new HashMap<>();
						paramter.put(manyToMany.getFromId(), idObj);

						paramter.put(Constant.SQL_SYMBOL, sqlBuffer.toString());
						List<Map<String, Object>> mapResultList = context.getDaoMapper().select(paramter);
						Object resultObject = Toolkit.convertMapToObjectList(context.getType(), mapResultList, new DefaultObjectCreator());
						ReflectionUtils.setValue(obj, field, resultObject);

					}
				}
			}

		}
		return result;
	}

	private boolean isProxy(String fieldName) {
		boolean isProxy = false;
		List<String> includeFields = filter.getIncludeFields();

		List<String> excludeFields = filter.getExcludeFields();
		if (!CollectionUtils.isEmpty(includeFields)) {

			for (String includeField : includeFields) {
				if (includeField.toLowerCase().equals(fieldName.toLowerCase())) {
					isProxy = true;
					break;
				}
			}

		}
		if (!CollectionUtils.isEmpty(excludeFields)) {
			isProxy = true;
			for (String excludeField : excludeFields) {
				if (excludeField.toLowerCase().equals(fieldName.toLowerCase())) {
					isProxy = false;
				}
			}
		}

		return isProxy;
	}
}
