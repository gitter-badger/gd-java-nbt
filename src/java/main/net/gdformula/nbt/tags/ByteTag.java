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
public class ByteTag extends Tag {
	private byte value;
	
	public ByteTag(String name) {
		super(name);
	}
	
	public ByteTag(String name, byte value) {
		super(name);
		this.value = value;
	}

	@Override
	public final TagType getType() {
		return TagType.TYPE_BYTE;
	}

	@Override
	public final Byte getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new ByteTag(getName(), value);
	}

	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = stream.readByte();
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeByte(value);
	}
}
