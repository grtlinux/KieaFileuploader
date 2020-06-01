package org.tain.utils;

public class CurrentInfo {

	public static String get() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement element = Thread.currentThread().getStackTrace()[2];
		sb.append(element.getClassName()).append('.').append(element.getMethodName()).append("()");
		//sb.append(" of ");
		//sb.append(element.getFileName());
		sb.append("#").append(element.getLineNumber());
		return sb.toString();
	}
}
