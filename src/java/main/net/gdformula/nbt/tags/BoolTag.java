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
public class BoolTag extends Tag {
	private boolean value;
	
	public BoolTag(String name) {
		super(name);
	}
	
	public BoolTag(String name, boolean value) {
		this(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_BOOL;
	}

	@Override
	public Boolean getValue() {
		return value;
	}
	
	@Override
	public Tag copy() {
		return new BoolTag(getName(), value);
	}

	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		value = stream.readBoolean();
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeBoolean(value);
	}
}
