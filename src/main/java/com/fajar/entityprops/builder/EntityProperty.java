package com.fajar.entityprops.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fajar.entityprops.annotation.AdditionalQuestionField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
 
@Slf4j
public class EntityProperty implements Serializable {

	static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	/**
	* 
	*/
	private static final long serialVersionUID = 2648801606702528928L;

	private String entityName;
	private String alias;
	private String fieldNames;

	private String idField;
	private int formInputColumn; 
	private boolean editable = true; 
	private boolean withDetail = false;
	private String detailFieldName;

	private String imageElementsJson;
	private String dateElementsJson;
	private String currencyElementsJson;
 
	private List<String> dateElements = new ArrayList<String>(); 
	private List<String> imageElements = new ArrayList<String>(); 
	private List<String> currencyElements = new ArrayList<String>();
	private List<EntityElement> elements;
	private List<String> fieldNameList;

	private boolean ignoreBaseField;
	private boolean isQuestionare;
	@Setter(value = AccessLevel.NONE)
	private String groupNames;

	public EntityProperty(boolean ignoreBaseField2, String entityName, boolean isQuestionare2) {
		this.ignoreBaseField = ignoreBaseField2;
		this.entityName = entityName;
		this.isQuestionare = isQuestionare2;
		
	}

	public void setElementJsonList() {

		try {
			this.dateElementsJson = OBJECT_MAPPER.writeValueAsString(dateElements);
			this.imageElementsJson = OBJECT_MAPPER.writeValueAsString(imageElements);
			this.currencyElementsJson = OBJECT_MAPPER.writeValueAsString(currencyElements);
		} catch (JsonProcessingException e) { 
			e.printStackTrace();
		}
	}

	public void removeElements(String... fieldNames) throws  Exception {
		if (this.elements == null)
			return;
		for (int i = 0; i < fieldNames.length; i++) {
			String fieldName = fieldNames[i];
			loop: for (String fName : fieldNameList) {
				if (fieldName.equals(fName)) {
					fieldNameList.remove(fName);
					break loop;
				}
			}
			loop2: for (EntityElement entityElement : this.elements) {
				if (entityElement.getId().equals(fieldName)) {
					this.elements.remove(entityElement);
					break loop2;
				}
			}
		}
		this.fieldNames = OBJECT_MAPPER.writeValueAsString(fieldNameList);
	}

	public void setGroupNames(String[] groupNamesArray) {
		int removedIndex = 0;
		for (int i = 0; i < groupNamesArray.length; i++) {
			if (groupNamesArray[i] == AdditionalQuestionField.DEFAULT_GROUP_NAME) {
				removedIndex = i;
			}
		}
		groupNamesArray = ArrayUtils.remove(groupNamesArray, removedIndex);
		this.groupNames = String.join(",", groupNamesArray);
		groupNames += "," + AdditionalQuestionField.DEFAULT_GROUP_NAME;
	}

//	static void main(String[] args) {
//		args =new String[] {"OO", "ff", "fff22"};
//		for (int i = 0; i < args.length; i++) {
//			if(args[i] == "OO")
//		}
//	}

	public String getGridTemplateColumns() {
		if (formInputColumn == 2) {
			return "20% 70%";
		}
		return StringUtils.repeat("auto ", formInputColumn);
	}

	public void determineIdField() {
		if (null == elements) {
			log.error("Entity ELements is NULL");
			return;
		}
		for (EntityElement entityElement : elements) {
			if (entityElement.isIdField() && getIdField() == null) {
				setIdField(entityElement.getId());
			}
		}
	}
	
	public String getIdField() {
		return this.idField;
	}
	
	public void setIdField(String idField) {
		this.idField = idField;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}

	public int getFormInputColumn() {
		return formInputColumn;
	}

	public void setFormInputColumn(int formInputColumn) {
		this.formInputColumn = formInputColumn;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isWithDetail() {
		return withDetail;
	}

	public void setWithDetail(boolean withDetail) {
		this.withDetail = withDetail;
	}

	public String getDetailFieldName() {
		return detailFieldName;
	}

	public void setDetailFieldName(String detailFieldName) {
		this.detailFieldName = detailFieldName;
	}

	public String getImageElementsJson() {
		return imageElementsJson;
	}

	public void setImageElementsJson(String imageElementsJson) {
		this.imageElementsJson = imageElementsJson;
	}

	public String getDateElementsJson() {
		return dateElementsJson;
	}

	public void setDateElementsJson(String dateElementsJson) {
		this.dateElementsJson = dateElementsJson;
	}

	public String getCurrencyElementsJson() {
		return currencyElementsJson;
	}

	public void setCurrencyElementsJson(String currencyElementsJson) {
		this.currencyElementsJson = currencyElementsJson;
	}

	public List<String> getDateElements() {
		return dateElements;
	}

	public void setDateElements(List<String> dateElements) {
		this.dateElements = dateElements;
	}

	public List<String> getImageElements() {
		return imageElements;
	}

	public void setImageElements(List<String> imageElements) {
		this.imageElements = imageElements;
	}

	public List<String> getCurrencyElements() {
		return currencyElements;
	}

	public void setCurrencyElements(List<String> currencyElements) {
		this.currencyElements = currencyElements;
	}

	public List<EntityElement> getElements() {
		return elements;
	}

	public void setElements(List<EntityElement> elements) {
		this.elements = elements;
	}

	public List<String> getFieldNameList() {
		return fieldNameList;
	}

	public void setFieldNameList(List<String> fieldNameList) {
		this.fieldNameList = fieldNameList;
	}

	public boolean isIgnoreBaseField() {
		return ignoreBaseField;
	}

	public void setIgnoreBaseField(boolean ignoreBaseField) {
		this.ignoreBaseField = ignoreBaseField;
	}

	public boolean isQuestionare() {
		return isQuestionare;
	}

	public void setQuestionare(boolean isQuestionare) {
		this.isQuestionare = isQuestionare;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

}
