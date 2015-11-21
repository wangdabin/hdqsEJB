package com.ceb.hdqs.query.entity;


/**
 * 存储键值对
 * @author user
 *
 */
public class SkyPair<V,T>{

	protected V key;
	protected T value;
	
	public SkyPair(V key ,T value){
		this.key =key;
		this.value = value;
	}
	
	public SkyPair(){
	}
	
	public V getKey() {
		return key;
	}
	
	public boolean setKey(V key) {
		this.key = key;
		return true;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkyPair other = (SkyPair) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
