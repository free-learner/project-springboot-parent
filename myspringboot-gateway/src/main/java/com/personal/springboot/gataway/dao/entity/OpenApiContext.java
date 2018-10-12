package com.personal.springboot.gataway.dao.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.chain.Context;

@SuppressWarnings("rawtypes")
public class OpenApiContext extends HashMap implements Context {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String SESSION_BEAN_CONTEXT_KEY="BL_OPENAPI_SESSION_BEAN_CONTEXT_KEY";

	@Override
	public int size() {
		return super.size();
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return super.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return (super.get(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return (super.put(key, value));
	}

	@Override
	public Object remove(Object key) {
		return super.remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putAll(Map m) {
		super.putAll(m);
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public Set keySet() {
		return super.keySet();
	}

	@Override
	public Collection values() {
		return super.values();
	}

	@Override
	public Set entrySet() {
		return super.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public AbstractOpenApiSessionBean getOpenApiHttpSessionBean(){
		return (AbstractOpenApiSessionBean)this.get(this.SESSION_BEAN_CONTEXT_KEY);
	}
	
	public void setOpenApiHttpSessionBean(AbstractOpenApiSessionBean sessionBean){
		this.put(this.SESSION_BEAN_CONTEXT_KEY, sessionBean);
	}
	
	public static void main(String[] args){
		OpenApiContext blCtx = new OpenApiContext();
		OpenApiHttpSessionBean sb = new OpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = new OpenApiHttpRequestBean();

		sb.setRequest(request);
		blCtx.setOpenApiHttpSessionBean(sb);
		Context ctx = (Context)blCtx;
		System.out.println(ctx.toString());
	}
}
