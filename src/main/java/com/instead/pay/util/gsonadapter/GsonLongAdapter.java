package com.instead.pay.util.gsonadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.instead.pay.util.StringUtil;

import java.lang.reflect.Type;


public class GsonLongAdapter implements JsonDeserializer<Long> {

	@Override
	public Long deserialize(JsonElement ele, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		String str = ele.getAsString();
		if(!StringUtil.isEmpty(str)){
			return 0L;
		}else{
			return ele.getAsLong();
		}
	}
}
