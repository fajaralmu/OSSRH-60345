package com.fajar.entityprops.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.SerializationUtils;

import com.fajar.entityprops.annotation.FormField;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityPropsUtil {

	
	public static void main(String[] args) {
//		List<Field> fields = getDeclaredFields(StudentParent.class);
//		fields = sortListByQuestionareSection(fields);
//		
//		for (Field field : fields) {
//			log.debug("{}", field.getName());
//		}
//		
//		Object[] arrayOfFields = fields.toArray();
	}

	static boolean isIdField(Field field) {
		return field.getAnnotation(Id.class) != null;
	}

	/**
	 * 
	 * @param _class
	 * @return String type field & non empty able
	 */
	public static List<Field> getNotEmptyAbleField(Class<?  > _class) {

		List<Field> result = new ArrayList<>();
		List<Field> formFieldAnnotatedField = getFormFieldAnnotatedField(_class);
		for (int i = 0; i < formFieldAnnotatedField.size(); i++) {
			Field field = formFieldAnnotatedField.get(i);
			FormField formField = getFieldAnnotation(field, FormField.class);
			
			if(field.getType().equals(String.class) && !formField.emptyAble()) {
				result.add(field);
			}
			
		}
		
		return result;
	}

	public static List<Field> getFormFieldAnnotatedField(Class<? > _class) {

		List<Field> result = new ArrayList<>();

		List<Field> declaredField = getDeclaredFields(_class);
		for (int i = 0; i < declaredField.size(); i++) {
			Field field = declaredField.get(i);

			if (getFieldAnnotation(field, FormField.class) != null) {
				result.add(field);
			}

		}

		return result;
	}

		public static <T extends Annotation> T getClassAnnotation(Class<?> entityClass, Class<T> annotation) {
		try {
			return entityClass.getAnnotation(annotation);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T extends Annotation> T getFieldAnnotation(Field field, Class<T> annotation) {
		try {
			return field.getAnnotation(annotation);
		} catch (Exception e) {
			return null;
		}
	}

	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if (field == null) {

			}
			field.setAccessible(true);
			return field;

		} catch (Exception e) {
			log.error("Error get declared field in the class, and try access super class");
		}
		if (clazz.getSuperclass() != null) {

			try {
				log.info("TRY ACCESS SUPERCLASS");

				Field superClassField = clazz.getSuperclass().getDeclaredField(fieldName);
				superClassField.setAccessible(true);
				return superClassField;
			} catch (Exception e) {

				log.error("FAILED Getting FIELD: " + fieldName);
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * get fields of a class, accessible true
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<Field> getDeclaredFields(Class<?> clazz) {
		Field[] baseField = clazz.getDeclaredFields();
//
//		List<EntityElement> entityElements = new ArrayList<EntityElement>();
		List<Field> fieldList = new ArrayList<>();

		for (Field field : baseField) {
			field.setAccessible(true);
			fieldList.add(field);
		}
		if (clazz.getSuperclass() != null) {

			Field[] parentFields = clazz.getSuperclass().getDeclaredFields();

			for (Field field : parentFields) {
				field.setAccessible(true);
				fieldList.add(field);
			}

		}
		return fieldList;
	}

	public static Field getIdFieldOfAnObject(Class<?> clazz) {
		log.info("Get ID FIELD FROM :" + clazz.getCanonicalName());

		if (getClassAnnotation(clazz, Entity.class) == null) {
			return null;
		}
		List<Field> fields = getDeclaredFields(clazz);

		for (Field field : fields) {

			if (field.getAnnotation(Id.class) != null) {

				return field;
			}
		}

		return null;
	}

	public static boolean isNumericField(Field field) {
		return field.getType().equals(Integer.class) || field.getType().equals(Double.class)
				|| field.getType().equals(Long.class) || field.getType().equals(BigDecimal.class)
				|| field.getType().equals(BigInteger.class);
	}

	 
 
	public static boolean isStaticField(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}
 
	public static <T> T getObjectFromListByFieldName(final String fieldName, final Object value, final List<T> list) {

		for (T object : list) {
			Field field = EntityPropsUtil.getDeclaredField(object.getClass(), fieldName);
			field.setAccessible(true);
			try {
				Object fieldValue = field.get(object);

				if (fieldValue != null && fieldValue.equals(value)) {
					return (T) object;
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		return null;
	}

	public static <T> boolean existInList(T o, List<T> list) {
		if (null == list) {
			log.error("LIST IS NULL");
			return false;
		}
		for (T object : list) {
			if (object.equals(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Clone Serializable Object
	 * 
	 * @param <T>
	 * @param serializable
	 * @return
	 */
	public static <T extends Serializable> T cloneSerializable(T serializable) {
		try {
			return SerializationUtils.clone(serializable);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 

}
