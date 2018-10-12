package com.personal.springboot.gataway.dao.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;


//{
//    "RESPONSE": {
//        "RETURN_CODE": "S000A000",
//        "RETURN_DATA": { 
//           业务数据
//        },
//        "RETURN_DESC": "描述信息"
//    }
//}

@XStreamAlias("RESPONSE")
public class OpenApiRespDto  implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	  @JSONField(name="RETURN_CODE")
	  @XStreamAlias("RETURN_CODE")
	  private String  return_code;
	  
	  @JSONField(name="RETURN_DESC")
	  @XStreamAlias("RETURN_DESC")
	  private String  return_desc;
	  
	  @JSONField(name="RETURN_STAMP")
	  @XStreamAlias("RETURN_STAMP")
	  private String  return_stamp;
	  
	  @JSONField(name="RETURN_DATA")
	  @XStreamAlias("RETURN_DATA")
	  private Object returnData;
	  
	  
	  
	 public OpenApiRespDto(String return_code, String return_desc, String return_stamp) {
		this(return_code, return_desc);	
		this.return_stamp = return_stamp;
	}
	 
	 public OpenApiRespDto(String return_code, String return_desc) {
			super();
			this.return_code = return_code;
			this.return_desc = return_desc;
			this.returnData=new Object();
		} 
	 
	 
	 public OpenApiRespDto(){
		 
	 }

	public String getReturn_stamp() {
		return return_stamp;
	}




	public void setReturn_stamp(String return_stamp) {
		this.return_stamp = return_stamp;
	}




	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_desc() {
		return return_desc;
	}

	public void setReturn_desc(String return_desc) {
		this.return_desc = return_desc;
	}

	public Object getReturnData() {
		return returnData;
	}

	public void setReturnData(Object returnData) {
		this.returnData = returnData;
	}

	 
	 
	 
	
}
