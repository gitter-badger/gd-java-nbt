package net.gdformula.nbt.tags;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.gdformula.nbt.Tag;
import net.gdformula.nbt.TagType;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 * 
 * @since Early Development Build 1
 */
public class MapTag extends Tag {
	private Map<String, Tag> value;
	
	public MapTag(String name) {
		super(name);
		this.value = new HashMap<String, Tag>();
	}
	
	public MapTag(String name, Map<String, Tag> value) {
		super(name);
		this.value = Collections.unmodifiableMap(value);
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_MAP;
	}

	@Override
	public Map<String, Tag> getValue() {
		return value;
	}
	
	public Tag getTag(String name) {
		if (value.containsKey(name))
			return value.get(name);
		
		return null;
	}
	
	public void addTag(Tag tag) {
		value.put(tag.getName(), tag);
	}
	
	public void setTag(String name, Tag tag) {
		value.put(name, tag);
	}
	
	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}
	
	public boolean getBoolean(String name, boolean defaultValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof BoolTag))
			return defaultValue;
		
		return ((BoolTag)value.get(name)).getValue();
	}
	
	public byte getByte(String name) {
		return getByte(name, (byte) 0);
	}
	
	public byte getByte(String name, byte defaultValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof ByteTag))
			return defaultValue;
		
		return ((ByteTag)value.get(name)).getValue();
	}
	
	public byte[] getBytes(String name) {
		if (!value.containsKey(name) || !(value.get(name) instanceof ArrayByteTag))
			return null;
		
		return ((ArrayByteTag)value.get(name)).getValue();
	}
	
	public double getDouble(String name) {
		return getDouble(name, 0);
	}
	
	public double getDouble(String name, double defaultValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof DoubleTag))
			return defaultValue;
		
		return ((DoubleTag)value.get(name)).getValue();
	}
	
	public float getFloat(String name) {
		return getFloat(name, 0);
	}
	
	public float getFloat(String name, float defaultValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof FloatTag))
			return defaultValue;
		
		return ((FloatTag)value.get(name)).getValue();
	}
	
	public int getInt(String name) {
		return getInt(name, 0);
	}
	
	public int getInt(String name, int defaultValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof IntTag))
			return defaultValue;
		
		return ((IntTag)value.get(name)).getValue();
	}
	
	public ListTag getList(String name) {
		if (!value.containsKey(name) || !(value.get(name) instanceof ListTag))
			return null;
		
		return (ListTag) value.get(name);
	}
	
	public long getLong(String name) {
		return getLong(name, 0);
	}
	
	public long getLong(String name, long defaultValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof LongTag))
			return defaultValue;
		
		return ((LongTag)value.get(name)).getValue();
	}
	
	public MapTag getMap(String name) {
		if (!value.containsKey(name) || !(value.get(name) instanceof MapTag))
			return null;
		
		return (MapTag) value.get(name);
	}
	
	public short getShort(String name) {
		return getShort(name, (short) 0);
	}
	
	public short getShort(String name, short defaultValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof ShortTag))
			return defaultValue;
		
		return ((ShortTag)value.get(name)).getValue();
	}
	
	public String getString(String name) {
		return getString(name, null);
	}
	
	public String getString(String name, String defaulValue) {
		if (!value.containsKey(name) || !(value.get(name) instanceof StringTag))
			return defaulValue;
		
		return ((StringTag)value.get(name)).getValue();
	}
	
	@Override
	public Tag copy() {
		MapTag tag = new MapTag(getName());
		
		Iterator<Tag> itr = value.values().iterator();
		
		while (itr.hasNext())
			tag.addTag(itr.next().copy());
		
		return tag;
	}

	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = new HashMap<String, Tag>();
		
		while (true) {
			Tag tag = Tag.readNext(stream, depth + 1);
			if (tag instanceof NullTag)
				break;
			
			value.put(tag.getName(), tag);
		}
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		Iterator<Tag> itr = value.values().iterator();
		
		while (itr.hasNext())
			Tag.write(itr.next(), stream);
		
		Tag.write(new NullTag(), stream);
	}
}
