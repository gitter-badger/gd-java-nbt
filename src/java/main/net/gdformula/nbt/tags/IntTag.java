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
public class IntTag extends Tag {
	private int value;
	
	public IntTag(String name) {
		super(name);
	}
	
	public IntTag(String name, int value) {
		super(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_INT;
	}

	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new IntTag(getName(), value);
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = stream.readInt();
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeInt(value);
	}
}
