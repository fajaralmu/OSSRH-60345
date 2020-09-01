package com.fajar.entityprops.inputfields;

public enum FieldType {

	 FIELD_TYPE_TEXT ( "text"),
	 FIELD_TYPE_IMAGE ( "img"),
	 FIELD_TYPE_CURRENCY ( "currency"),
	 FIELD_TYPE_NUMBER ( "number"),
	 FIELD_TYPE_HIDDEN ( "hidden"),
	 FIELD_TYPE_COLOR ( "color"),
	 FIELD_TYPE_FIXED_LIST ("fixedlist"),
	 FIELD_TYPE_DYNAMIC_LIST ( "dynamiclist"),
	 FIELD_TYPE_TEXTAREA ( "textarea"),
	 FIELD_TYPE_PLAIN_LIST ( "plainlist"),
	 FIELD_TYPE_DATE ( "date");
	public final String value;
	private FieldType(String val) {
		this.value = val;
	}
	
	public static FieldType getByValue(String value) {
		for(FieldType fieldType : FieldType.values()) {
			if(fieldType.value.equals(value)) {
				return fieldType;
			}
		}
		
		return FIELD_TYPE_TEXT;
	}
}
