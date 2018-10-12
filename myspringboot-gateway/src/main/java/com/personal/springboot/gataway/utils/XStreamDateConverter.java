package com.personal.springboot.gataway.utils;

import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.converters.basic.DateConverter;

public class XStreamDateConverter extends DateConverter {

	public XStreamDateConverter() {
		super();
	}

	public XStreamDateConverter(boolean lenient) {
		super(lenient);
	}

	public XStreamDateConverter(String defaultEraFormat, String defaultFormat,
			String[] acceptableFormats, Locale locale, TimeZone timeZone,
			boolean lenient) {
		super();
	}

	public XStreamDateConverter(String defaultFormat,
			String[] acceptableFormats, boolean lenient) {
		super(defaultFormat, acceptableFormats, lenient);
	}

	public XStreamDateConverter(String defaultFormat,
			String[] acceptableFormats, TimeZone timeZone, boolean lenient) {
		super(defaultFormat, acceptableFormats, timeZone, lenient);
	}

	public XStreamDateConverter(String defaultFormat,
			String[] acceptableFormats, TimeZone timeZone) {
		super(defaultFormat, acceptableFormats, timeZone);
	}

	public XStreamDateConverter(String defaultFormat, String[] acceptableFormats) {
		super(defaultFormat, acceptableFormats);
	}

	public XStreamDateConverter(TimeZone timeZone) {
		super(timeZone);
	}

	@Override
	public Object fromString(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return super.fromString(str);
	}

}
