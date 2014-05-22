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
public class DoubleTag extends Tag {
	private double value;
	
	public DoubleTag(String name) {
		super(name);
	}
	
	public DoubleTag(String name, double value) {
		super(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_DOUBLE;
	}

	@Override
	public Double getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new DoubleTag(getName(), value);
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = stream.readDouble();
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeDouble(value);
	}
}
