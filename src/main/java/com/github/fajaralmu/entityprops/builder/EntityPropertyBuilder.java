package com.github.fajaralmu.entityprops.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Id;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fajaralmu.entityprops.annotation.AdditionalQuestionField;
import com.github.fajaralmu.entityprops.annotation.Dto;
import com.github.fajaralmu.entityprops.annotation.FormField;
import com.github.fajaralmu.entityprops.util.EpCollectionUtil;

public class EntityPropertyBuilder {
	static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	org.slf4j.Logger log = LoggerFactory.getLogger(EntityPropertyBuilder.class);
	private final Class<?> clazz;
	private final Map<String, List<?>> additionalObjectList;

	static String writeJSON(Object o) {
		try {
			return OBJECT_MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
			return "{}";
		}
	}

	public EntityPropertyBuilder(Class<?> clazz, Map<String, List<?>> additionalObjectList) {
		this.clazz = clazz;
		this.additionalObjectList = additionalObjectList;
	}

	public EntityPropertyBuilder(Class<?> clazz) {
		this.clazz = clazz;
		this.additionalObjectList = null;
	}

	public EntityProperty createEntityProperty() throws Exception {
		if (clazz == null || getClassAnnotation(clazz, Dto.class) == null) {
			return null;
		}

		Dto dto = (Dto) getClassAnnotation(clazz, Dto.class);
		final boolean ignoreBaseField = dto.ignoreBaseField();
		final boolean isQuestionare = dto.quistionare();

		EntityProperty entityProperty = new EntityProperty(ignoreBaseField, clazz.getSimpleName().toLowerCase(),
				isQuestionare);
		try {

			List<Field> fieldList = getDeclaredFields();

			if (isQuestionare) {
				Map<String, List<Field>> groupedFields = sortListByQuestionareSection(fieldList);
				fieldList = EpCollectionUtil.mapOfListToList(groupedFields);
				Set<String> groupKeys = groupedFields.keySet();
				String[] keyNames = EpCollectionUtil.toArrayOfString(groupKeys.toArray());

				entityProperty.setGroupNames(keyNames);
			}
			List<EntityElement> entityElements = new ArrayList<>();
			List<String> fieldNames = new ArrayList<>();
			String fieldToShowDetail = "";

			for (Field field : fieldList) {

				final EntityElement entityElement = new EntityElement(field, entityProperty, additionalObjectList);

				if (false == entityElement.build()) {
					continue;
				}
				if (entityElement.isDetailField()) {
					fieldToShowDetail = entityElement.getId();
				}

				fieldNames.add(entityElement.getId());
				entityElements.add(entityElement);
			}

			entityProperty.setAlias(dto.value().isEmpty() ? clazz.getSimpleName() : dto.value());
			entityProperty.setEditable(dto.editable());
			entityProperty.setElementJsonList();
			entityProperty.setElements(entityElements);
			entityProperty.setDetailFieldName(fieldToShowDetail);
			entityProperty.setDateElementsJson(writeJSON(entityProperty.getDateElements()));
			entityProperty.setFieldNames(writeJSON(fieldNames));
			entityProperty.setFieldNameList(fieldNames);
			entityProperty.setFormInputColumn(dto.formInputColumn().value);
			entityProperty.determineIdField();

			log.info("============ENTITY PROPERTY: {} ", entityProperty);

			return entityProperty;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	private Map<String, List<Field>> sortListByQuestionareSection(List<Field> fieldList) {
		Map<String, List<Field>> temp = new HashMap<String, List<Field>>() {
			{
				put(AdditionalQuestionField.DEFAULT_GROUP_NAME, new ArrayList<>());
			}
		};

		String key = AdditionalQuestionField.DEFAULT_GROUP_NAME;
		for (Field field : fieldList) {
			FormField formField = field.getAnnotation(FormField.class);
			boolean isIDField = field.getAnnotation(Id.class) != null
					|| field.getAnnotation(org.springframework.data.annotation.Id.class) != null;

			if (null == formField) {
				continue;
			}
			AdditionalQuestionField additionalQuestionField = field.getAnnotation(AdditionalQuestionField.class);
			if (null == additionalQuestionField || isIDField) {
				key = "OTHER";
				log.debug("{} has no additionalQuestionareField", field.getName());
			} else {
				key = additionalQuestionField.value();
			}
			if (temp.get(key) == null) {
				temp.put(key, new ArrayList<>());
			}
			temp.get(key).add(field);
			log.debug("{}: {}", key, field.getName());
		}
		log.debug("QUestionare Map: {}", temp);
		return (temp);

	}

	public static <T extends Annotation> T getClassAnnotation(Class<?> entityClass, Class<T> annotation) {
		try {
			return entityClass.getAnnotation(annotation);
		} catch (Exception e) {
			return null;
		}
	}
 
	public List<Field> getDeclaredFields() {
		Field[] baseField = clazz.getDeclaredFields();
 
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

}
