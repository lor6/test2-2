package com.baeldung.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

public class SerializationUtils {

	public static <T extends Serializable> byte[] serialize(T obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.close();
		return baos.toByteArray();
	}

	public static <T extends Serializable> T deserialize(byte[] b, Class<T> cl)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		return cl.cast(o);
	}

	public static boolean isSerializable(Class<?> it) {
		boolean serializable = it.isPrimitive() || it.isInterface() || Serializable.class.isAssignableFrom(it);
		if(!serializable) {
			return serializable;
		}
		Field[] declaredFields = it.getDeclaredFields();
		for(Field field: declaredFields) {
			Class<?> fieldType = field.getType();
			return isSerializable(fieldType);
		}
		return serializable;
	}
}
