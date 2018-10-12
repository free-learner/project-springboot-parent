package com.personal.springboot.gataway.utils;

import java.util.regex.Pattern;

import org.apache.commons.collections.Predicate;

public class RedisPredicate  implements Predicate {	
	
	public RedisPredicate(String patternString) {
		super();
		this.pattern = Pattern.compile(patternString);
	}
	
	private Pattern pattern ;
	
	
	@Override
	public boolean evaluate(Object object) {		
		String obj=object.toString();		
		return pattern.matcher(obj).find();
	}


	public Pattern getPattern() {
		return pattern;
	}


	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	
	
	
	
}
