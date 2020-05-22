package com.instead.pay.util.gsonadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.instead.pay.util.DateUtil;
import com.instead.pay.util.StringUtil;

import java.lang.reflect.Type;
import java.util.Date;


public class GsonDateAdapter implements JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonElement ele, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		String date = ele.getAsString();
		Date d;
		try {
			if(!StringUtil.isEmpty(date)){
				if (date.indexOf(":")!=-1) {
					d = DateUtil.parseDateTime(date, "yyyy-MM-dd HH:mm:ss");
				}else {
					long dateLong = ele.getAsLong();
					d = new Date(dateLong);
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			 throw new RuntimeException(e); 
		}
		return d;
	}
}
