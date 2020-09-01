package com.fajar.entityprops.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fajar.entityprops.inputfields.FormInputColumn;
import com.fajar.entityprops.update.EntityUpdateService;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.TYPE)  
public @interface Dto {

	FormInputColumn formInputColumn() default FormInputColumn.TWO_COLUMN;
	boolean ignoreBaseField() default true;
	boolean editable() default true;
	String value() default "";
	boolean quistionare() default false;
	Class<? extends EntityUpdateService > updateService() default EntityUpdateService.class;
	public boolean commonManagementPage() default true;
	 
}
