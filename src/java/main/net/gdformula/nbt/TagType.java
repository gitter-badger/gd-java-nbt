package net.gdformula.nbt;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import net.gdformula.nbt.tags.*;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 *
 */
public enum TagType {
	TYPE_NULL(0, "TAG_NULL", NullTag.class),
	TYPE_BOOL(1, "TAG_BOOL", BoolTag.class),
	TYPE_BYTE(2, "TAG_BYTE", ByteTag.class),
	TYPE_SHORT(3, "TAG_SHORT", ShortTag.class),
	TYPE_INT(4, "TAG_INT", IntTag.class),
	TYPE_FLOAT(5, "TAG_FLOAT", FloatTag.class),
	TYPE_LONG(6, "TAG_LONG", LongTag.class),
	TYPE_DOUBLE(7, "TAG_DOUBLE", DoubleTag.class),
	TYPE_ARRAY_BYTE(8, "TAG_ARRBYTE", ArrayByteTag.class),
	TYPE_STRING(9, "TAG_STRING", StringTag.class),
	TYPE_LIST(10, "TAG_LIST", ListTag.class),
	TYPE_MAP(11, "TAG_MAP", MapTag.class),
	;
	
	private static final Map<Integer, TagType> MAP_CODE_TYPE = new HashMap<Integer, TagType>();
	private static final Map<String, TagType> MAP_NAME_TYPE = new HashMap<String, TagType>();
	private static final Map<Class<? extends Tag>, TagType> MAP_CLASS_TYPE = new HashMap<Class<? extends Tag>, TagType>();
	
	private static void registerTagType(TagType type) {
		MAP_CODE_TYPE.put(type.typeCode, type);
		MAP_NAME_TYPE.put(type.typeName, type);
		MAP_CLASS_TYPE.put(type.typeClass, type);
	}
	
	static {
		TagType[] values = TagType.values();
		for (TagType type : values)
			registerTagType(type);
	}
	
	private final int typeCode;
	private final String typeName;
	private final Class<? extends Tag> typeClass;
	
	private TagType(int code, String name, Class<? extends Tag> typeClass) {
		this.typeCode = code;
		this.typeName = name;
		this.typeClass = typeClass;
	}
	
	public int getTypeCode() {
		return typeCode;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public Class<? extends Tag> getTypeClass() {
		return typeClass;
	}
	
	public static final Charset TAG_CHARSET = Charset.forName("UTF-8");
	
	public static TagType getType(int typeCode) {
		TagType type = MAP_CODE_TYPE.get(typeCode);
		if (type != null)
			return type;
		
		return TYPE_NULL;
	}
	
	public static TagType getType(String typeName) {
		if (typeName != null) {
			TagType type = MAP_NAME_TYPE.get(typeName);
			
			if (type != null)
				return type;
		}
		
		return TYPE_NULL;
	}
	
	public static int getTypeCode(Class<? extends Tag> tagClass) {
		if (tagClass != null) {
			TagType type = MAP_CLASS_TYPE.get(tagClass);
			
			if (type != null)
				return type.typeCode;
		}
		
		return TYPE_NULL.typeCode;
	}
	
	public static String getTypeName(Class<? extends Tag> tagClass) {
		if (tagClass != null) {
			TagType type = MAP_CLASS_TYPE.get(tagClass);
			
			if (type != null)
				return type.typeName;
		}
		
		return TYPE_NULL.typeName;
	}
	
	public static Class<? extends Tag> getTypeClass(int typeCode) {
		return getType(typeCode).typeClass;
	}
	
	public static Class<? extends Tag> getTypeClass(String typeName) {
		return getType(typeName).typeClass;
	}
}
