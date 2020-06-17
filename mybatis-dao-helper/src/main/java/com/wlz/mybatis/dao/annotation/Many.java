package com.wlz.mybatis.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wlz.mybatis.dao.FetchType;
/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月18日 下午2:01:37 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Many {

	String value();
	
	FetchType fetchType() default FetchType.LAZY;
}
