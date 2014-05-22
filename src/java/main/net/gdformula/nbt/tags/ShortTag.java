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
public class ShortTag extends Tag {
	private short value;
	
	public ShortTag(String name) {
		super(name);
	}
	
	public ShortTag(String name, short value) {
		super(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_SHORT;
	}

	@Override
	public Short getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new ShortTag(getName(), value);
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = stream.readShort();
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeShort(value);
	}
}
