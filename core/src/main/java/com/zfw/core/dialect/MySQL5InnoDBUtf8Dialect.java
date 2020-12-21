package com.zfw.core.dialect;

import org.hibernate.dialect.MySQL5Dialect;

public class MySQL5InnoDBUtf8Dialect extends MySQL5Dialect{
	private final static String TABLE_TYPE="ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci";
	@Override
	public String getTableTypeString() {
		return TABLE_TYPE;
	}
}
