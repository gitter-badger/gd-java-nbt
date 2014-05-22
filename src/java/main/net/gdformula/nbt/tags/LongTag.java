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
public class LongTag extends Tag {
	private long value;
	
	public LongTag(String name) {
		super(name);
	}
	
	public LongTag(String name, long value) {
		super(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_LONG;
	}

	@Override
	public Long getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new LongTag(getName(), value);
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = stream.readLong();
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeLong(value);
	}
}
