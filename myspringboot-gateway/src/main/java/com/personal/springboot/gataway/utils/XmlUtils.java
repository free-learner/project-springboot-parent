package com.personal.springboot.gataway.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.QName;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 
 * @author carlee
 * 
 */
public class XmlUtils {

	
	@SuppressWarnings("unchecked")
	public static <T> T convertXml2JavaBean(Class<T> claz, String xmlMessage) {
		XStream xStream = new XStream();
		xStream.processAnnotations(claz);
		xStream.registerConverter(new XStreamDateConverter("yyyy-MM-dd HH:mm:ss",
				null, TimeZone.getTimeZone("GMT+8")));
		xStream.registerConverter(new XStreamDateConverter("yyyy-MM-ddTHH:mm:ssZ",
						null, TimeZone.getTimeZone("GMT+8")));
		xStream.registerConverter(new StringConverter() {

			@Override
			public String fromString(String str) {
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				return super.fromString(str).toString().trim();
			}
		});
		// 下面是转换常用的数字类型
		xStream.registerConverter(new DoubleConverter() {

			@Override
			public Object fromString(String str) {
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				return super.fromString(str);
			}
		});
		xStream.registerConverter(new LongConverter() {

			@Override
			public Object fromString(String str) {
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				return super.fromString(str);
			}
		});
		xStream.registerConverter(new IntConverter() {

			@Override
			public Object fromString(String str) {
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				if("FALSE".equals(str)){
					return 0;
				}
				if("TRUE".equals(str)){
					return 1;
				}
				return super.fromString(str);
			}
		});
		xStream.registerConverter(new BigDecimalConverter() {

			@Override
			public Object fromString(String str) {
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				return super.fromString(str);
			}
		});
		T t = (T) xStream.fromXML(xmlMessage);
		return t;
	}

	/**
	 * 
	 * @param claz
	 * @param xmlMessage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertXml2JavaBean(Class<T> claz, String xmlMessage,
			String dataFormat) {
		XStream xStream = new XStream();
		xStream.processAnnotations(claz);
		xStream.registerConverter(new DateConverter(dataFormat, null));
		T t = (T) xStream.fromXML(xmlMessage);
		return t;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String bean2xml(Map<String, Class> clazzMap, Object bean) {
		XStream xStream = new XStream();
		for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
			xStream.alias(m.getKey(), m.getValue());
		}
		String xml = xStream.toXML(bean);
		return xml;
	}

	public static String bean2xml(Object bean) {
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
		xStream.autodetectAnnotations(true);
		
		String xml = xStream.toXML(bean);
		return xml;
	}

	
	
	public static void removeNamespace(Element element){
		if(element==null){
			return;
		}		
		
		element.setQName(new QName(element.getName()));
		@SuppressWarnings("unchecked")
		List<Element> elements = element.elements();
		if (elements != null && elements.size() > 0) {
			for (Element e : elements) {
				removeNamespace(e);
			}			
		} 
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		String s="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ais=\"http://AIS.CRC.com/gbs/ais\">"
		+"<soapenv:Header/><soapenv:Body><ais:REQUEST><ais:HRT_ATTRS><ais:App_Sub_ID>2002</ais:App_Sub_ID>"+
			"<ais:App_Token>5b8fe283-9f43-4baf-a099-15409e37be3c</ais:App_Token>"
			+"<ais:Api_ID>hrt.order.getOrder.testliuliu10jsonnew</ais:Api_ID>"
			+"<ais:Api_Version>1.0</ais:Api_Version>"
            +"<ais:Time_Stamp>20160825165613</ais:Time_Stamp>"
           +" <ais:Sign_Method>md5</ais:Sign_Method>"
            +"<ais:Sign>e54045105da689bb24e88a5ef680d12c</ais:Sign>"
           +"<ais:Format>json</ais:Format>"
            +"<ais:Partner_id>S001</ais:Partner_id>"
           +" <ais:App_Pub_ID>MOA</ais:App_Pub_ID>"
        +" </ais:HRT_ATTRS>"
         +"<ais:REQUEST_DATA>"
         		+"<customer>"
         			+"<address>shanghai</address>"
         			+"<id>2</id>"
         			+"<name>liuliu</name> "
         		+"</customer>"
         +"</ais:REQUEST_DATA>"
      +"</ais:REQUEST>"
  +" </soapenv:Body>"
+"</soapenv:Envelope>";
		
		
		
		
//		Document document = DocumentHelper.parseText(s);
//		Element root = document.getRootElement();
////		removeNamespace(root);
//		
//		Element body = root.element(BODY);
//		Element request = body.element(REQUEST);
//		removeNamespace(request);
//		request.element(HRT_ATTRS).remove(request.element(HRT_ATTRS).element(APP_TOKEN));
//		System.out.println(request.element(REQUEST_DATA).asXML());
		

		
//		OpenApiRespDto a=new OpenApiRespDto("S000A000", "chenggong");
//		System.out.println(bean2xml(a));
		
		
	}
}
