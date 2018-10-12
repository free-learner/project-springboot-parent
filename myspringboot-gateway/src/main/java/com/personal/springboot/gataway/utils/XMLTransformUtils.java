package com.personal.springboot.gataway.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * XML转换工具类
 * 
 * @author maeiei
 */
public class XMLTransformUtils {

	private static final String BODY = "Body";
	private static final String REQUEST = "REQUEST";
	private static final String HRT_ATTRS = "HRT_ATTRS";
	private static final String REQUEST_DATA = "REQUEST_DATA";

	/**
	 * 获取请求的参数
	 * 
	 * @param xml
	 * @return 转换后的Map
	 */
	public static Map<String, Object> transformRequest(String xml) {
		Document document = null;
		// 是否存在命名空间
		boolean isExistNamespace = false;
		try {
			document = DocumentHelper.parseText(xml.trim());
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
		Element root = document.getRootElement();

		Map<String, Object> map = new HashMap<>();

		Map<String, Object> requestMap = new HashMap<>();
		Map<String, String> hrtAttrsMap = new HashMap<>();
		Map<String, String> requestDataMap = new HashMap<>();

		map.put(REQUEST, requestMap);
		requestMap.put(HRT_ATTRS, hrtAttrsMap);
		requestMap.put(REQUEST_DATA, requestDataMap);

		map.put("envelope", root.getNamespacePrefix());
		map.put("envelope_uri", root.getNamespaceURI());

		Element body = root.element(BODY);
		Element request = body.element(REQUEST);

		@SuppressWarnings("unchecked")
		List<Element> attrs = request.element(HRT_ATTRS).elements();
		for (Element e : attrs) {
			if (!isExistNamespace) {
				map.put("ais", e.getNamespacePrefix());
				map.put("ais_uri", e.getNamespaceURI());
				isExistNamespace = true;
			}
			hrtAttrsMap.put(e.getName(), e.getStringValue());
		}
		@SuppressWarnings("unchecked")
		List<Element> requestData = request.element(REQUEST_DATA).elements();
		for (Element e : requestData) {
			requestDataMap.put(e.getName(), e.getStringValue());
		}
		return map;
	}

	public static String transformResponse(String json, String envelope, String envelope_uri, String ais,
			String ais_uri) {

		StringBuilder sb = new StringBuilder();
		sb.append(generateHead(envelope, envelope_uri, ais, ais_uri)).append(generateBody(json, ais))
				.append(generateEnd(envelope, envelope_uri, ais, ais_uri));

		return sb.toString();
	}

	private static String generateHead(String envelope, String envelope_uri, String ais, String ais_uri) {

		StringBuilder sb = new StringBuilder();

		sb.append("<").append(envelope).append(":Envelope ").append("xmlns:").append(envelope).append("=\"")
				.append(envelope_uri).append("\" xmlns:").append(ais).append("=\"").append(ais_uri).append("\" >")
				.append("<").append(envelope).append(":Header/>").append("<").append(envelope).append(":Body>");
		return sb.toString();
	}

	private static String generateBody(String json, String ais) {

		JSONObject responseJson = JSONObject.fromObject(json);

		XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setSkipNamespaces(true);
		xmlSerializer.setTypeHintsEnabled(false);
		String responseBody = xmlSerializer.write(responseJson);
		responseBody = responseBody.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "").replaceAll("<o>", "")
				.replaceAll("</o>", "").replaceAll("</", "##").replaceAll("<", "%%").replaceAll("##", "</" + ais + ":")
				.replaceAll("%%", "<" + ais + ":");
		return responseBody;
	}

	private static String generateEnd(String envelope, String envelope_uri, String ais, String ais_uri) {

		StringBuilder sb = new StringBuilder();
		sb.append("</").append(envelope).append(":Body>").append("</").append(envelope).append(":Envelope>");

		return sb.toString();
	}

	public static void main(String[] args) {

		String envelope = "soapenv", envelope_uri = "http://schemas.xmlsoap.org/soap/envelope/", ais = "AIS",
				ais_uri = "http://AIS.CRC.com/gbs/ais";

		System.out.println(generateHead(envelope, envelope_uri, ais, ais_uri));

		System.out.println(generateEnd(envelope, envelope_uri, ais, ais_uri));
	}
}
