package com.personal.springboot.common.utils;

import java.util.UUID;

/**
 * UUID生成器
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public abstract class UUIDGenerator {

	private UUIDGenerator() {
	}

	public static String generate() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}