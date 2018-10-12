package com.personal.springboot.gataway.task;

import com.personal.springboot.gataway.core.OpenApiHandlerExecuteTemplate;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;

public class OpenApiHttpReqTask extends AbstractTask {
	private OpenApiHttpSessionBean httpSessionBean;
	private OpenApiHandlerExecuteTemplate handlerExecuteTemplate;
	
	public OpenApiHttpSessionBean getHttpSessionBean() {
		return httpSessionBean;
	}

	public void setHttpSessionBean(OpenApiHttpSessionBean httpSessionBean) {
		this.httpSessionBean = httpSessionBean;
	}

	public OpenApiHandlerExecuteTemplate getHandlerExecuteTemplate() {
		return handlerExecuteTemplate;
	}

	public void setHandlerExecuteTemplate(
			OpenApiHandlerExecuteTemplate handlerExecuteTemplate) {
		this.handlerExecuteTemplate = handlerExecuteTemplate;
	}
	
	public OpenApiHttpReqTask(OpenApiHttpSessionBean httpSessionBean,OpenApiHandlerExecuteTemplate handlerExecuteTemplate) {
		this.httpSessionBean = httpSessionBean;
		this.handlerExecuteTemplate = handlerExecuteTemplate;
	}

	@Override
	public Object doBussiness() throws Exception {
		OpenApiContext blCtx = new OpenApiContext();
		blCtx.setOpenApiHttpSessionBean(httpSessionBean);
		this.handlerExecuteTemplate.execute(blCtx);
		return blCtx.getOpenApiHttpSessionBean();
	}
}
