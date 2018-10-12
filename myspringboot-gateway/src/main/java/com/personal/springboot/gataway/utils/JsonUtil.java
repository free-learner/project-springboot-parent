package com.personal.springboot.gataway.utils;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
 

public class JsonUtil {
    private static final String STR_JSON = "{\"name\":\"Michael\",\"address\":{\"city\":\"Suzou\",\"street\":\" Changjiang Road \",\"postcode\":100025},\"blog\":\"http://www.ij2ee.com\"}";
    public static String xml2JSON(String xml){
    	XMLSerializer xmlSerializer=new XMLSerializer();
    
    	String result =xmlSerializer.read(xml).toString();
    	result=result.replaceAll(":\\[\\]", ":\"\"");
        return result;
    }
     
    public static String json2XML(String json,String rootName){
    	
    	if(StringUtil.isEmpty(json)){
    		return "";
    	}
    	
    	
        JSONObject jobj = JSONObject.fromObject(json);
        
        XMLSerializer xmlSerializer = new XMLSerializer();       
        xmlSerializer.setTypeHintsCompatibility(true);  
        xmlSerializer.setTypeHintsEnabled(false);  
        xmlSerializer.setRootName(rootName);
        xmlSerializer.setElementName("entity");
      
        
        String xml =  xmlSerializer.write(jobj.getJSONObject(rootName)).split("\n")[1];
      
        xml.replaceAll("json_null=\"true\"", "");
        return xml;
    }
     
    public static void main(String[] args) {
    	
    	
//    	String xml="<REQUEST_DATA><channelId>IOS</channelId><transactionUuid>1-1GIIU</transactionUuid><sysId>70000018</sysId><mobile>15048710887</mobile><birthday></birthday><buUserNo></buUserNo><carNo></carNo><cardNo></cardNo><cardType></cardType><certificateNo></certificateNo><certificateType></certificateType><education></education><email></email><gender></gender><hobbies></hobbies><homeAddress></homeAddress><industry></industry><inviterMemberId></inviterMemberId><loginPassword></loginPassword><memo></memo><name></name><nationality></nationality><payPassword></payPassword><position></position><qq></qq><registerChannel></registerChannel><registerDate></registerDate><telNo></telNo><wechat></wechat><weibo></weibo></REQUEST_DATA>";
//        
//        
//        String json = xml2JSON(xml);
//        System.out.println("json="+json);
        
//        String test="{\"RESPONSE\":{\"RETURN_CODE\":\"S0A00000\",\"RETURN_DESC\":\"操作成功！\",\"RETURN_STAMP\":\"2016-11-12 17:43:59 912\",\"RETURN_DATA\":{\"infos\":[{\"msg\":\"会员不存在!\",\"code\":\"E1B00006\",\"memberId\":\"6304435297082998845\"}]}}}";
    	 String test="{\"RESPONSE\":{\"RETURN_CODE\":\"S0A00000\",\"RETURN_DESC\":\"操作成功！\",\"RETURN_STAMP\":\"2016-11-12 17:43:59 912\",\"RETURN_DATA\":null}}";
//        System.out.println( XmlToJsonStaxon.json2xml(test));
        
        System.out.println(JsonUtil.json2XML(test, "RESPONSE"));
        
    }
}