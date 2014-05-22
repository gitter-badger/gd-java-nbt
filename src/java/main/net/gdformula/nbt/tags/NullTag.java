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
public class NullTag extends Tag {
	public NullTag() {
		super(null);
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_NULL;
	}
	
	@Override
	public Object getValue() {
		return null;
	}
	
	@Override
	public Tag copy() {
		return new NullTag();
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		/* This tag does not write anything to the output stream */
	}

	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		/* This tag does not write anything to the output stream */
	}
}
