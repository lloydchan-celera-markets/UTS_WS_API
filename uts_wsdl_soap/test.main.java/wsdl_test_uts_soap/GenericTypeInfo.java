package wsdl_test_uts_soap;

import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import com.vectails.xml.IXmlParser;
import com.vectails.xml.data.Legs;

public class GenericTypeInfo
{
	java.util.List<? extends Foo> fooList = new ArrayList<Bar>();

	public static void main(String[] args) throws Exception
	{
		TestGenericToString();
	}

	static class Foo
	{
	}

	static class Bar extends Foo
	{
	}

	public static void TestGenericTypeInfo() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field field = GenericTypeInfo.class.getDeclaredField("fooList");

		Type type = field.getGenericType();
		System.out.println("type: " + type);
		if (type instanceof ParameterizedType)
		{
			ParameterizedType pt = (ParameterizedType) type;
			System.out.println("raw type: " + pt.getRawType());
			System.out.println("owner type: " + pt.getOwnerType());
			System.out.println("actual type args:");
			for (Type t : pt.getActualTypeArguments())
			{
				System.out.println("    " + t);
			}
		}

		System.out.println();

		Object obj = field.get(new GenericTypeInfo());
		System.out.println("obj: " + obj);
		System.out.println("obj class: " + obj.getClass());
	}
	
	public static void TestGenericToString()
	{
		Legs legs = new Legs();
		legs.toString();
	}
	

}