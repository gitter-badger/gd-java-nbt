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
 */
public class ArrayByteTag extends Tag {
	private byte[] value;
	
	public ArrayByteTag(String name) {
		super(name);
	}
	
	public ArrayByteTag(String name, byte[] value) {
		super(name);
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_ARRAY_BYTE;
	}

	@Override
	public byte[] getValue() {
		return value;
	}
	
	@Override
	public String getValueString() {
		StringBuilder builder = new StringBuilder("[byte]{ ");
		
		for (byte b : value) {
			if ((b & 0xF0) != 0)
				builder.append("0");
			
			builder.append(Integer.toHexString(b).toUpperCase()).append(" ");
		}
		
		builder.append("}");
		return builder.toString();
	}
	
	@Override
	public Tag copy() {
		return new ArrayByteTag(getName(), value);
	}
	
	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		int length = stream.readInt();
		byte[] data = new byte[length];
		
		stream.read(data, 0, length);
	}
	
	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		stream.writeInt(value.length);
		stream.write(value);
	}
}
