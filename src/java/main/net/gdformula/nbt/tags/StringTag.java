package net.gdformula.nbt.tags;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.gdformula.nbt.Tag;
import net.gdformula.nbt.TagType;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 * 
 * @since Early Development Build 1
 */
public class StringTag extends Tag {
	private String value;
	
	public StringTag(String name) {
		super(name);
	}
	
	public StringTag(String name, String value) {
		super(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_STRING;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new StringTag(getName(), value);
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = Tag.readString(stream);
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		Tag.writeString(stream, value);
	}
}
