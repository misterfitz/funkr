package com.funkr;

import com.funkr.entities.*;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class JamConverter implements Converter {
	
	public boolean canConvert(Class clazz){
		return clazz.equals(JamBase_Data.class);
	}
		
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context){
	
	}
	
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context){
		return null;

	}
}
