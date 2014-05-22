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
public class FloatTag extends Tag {
	private Float value;
	
	public FloatTag(String name) {
		super(name);
	}
	
	public FloatTag(String name, float value) {
		super(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_FLOAT;
	}

	@Override
	public Float getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new FloatTag(getName(), value);
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = stream.readFloat();
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeFloat(value);
	}
}
