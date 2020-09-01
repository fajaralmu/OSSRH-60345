package com.fajar.entityprops.update;

public abstract class EntityUpdateService<T, ResponseType> {

	public abstract ResponseType saveEntity(T baseEntity, boolean newRecord); 
}
