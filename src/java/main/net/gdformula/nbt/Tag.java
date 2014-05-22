package net.gdformula.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import net.gdformula.nbt.tags.NullTag;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 * 
 * @since Early Development Build 1
 */
public abstract class Tag {
	public abstract TagType getType();
	
	public final String getTagString() {
		return getType().getTypeName();
	}
	
	private final String name;
	
	protected Tag(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * @return the value
	 */
	public abstract Object getValue();
	
	/**
	 * 
	 * @return
	 */
	public abstract Tag copy();
	
	/**
	 * 
	 * @return
	 */
	public String getValueString() {
		return String.valueOf(getValue());
	}
	
	public static final String readString(DataInputStream stream) throws IOException {
		int length = stream.readShort() & 0xFFFF;
		byte[] data = new byte[length];
		stream.readFully(data);
		
		return new String(data, TagType.TAG_CHARSET);
	}
	
	public static final void writeString(DataOutputStream stream, String string) throws IOException {
		byte[] data = string.getBytes(TagType.TAG_CHARSET);
		stream.writeShort(data.length);
		stream.write(data);
	}
	
	protected static final Tag readNext(DataInputStream stream) throws IOException {
		return readNext(stream, 0);
	}
	
	protected static final Tag readNext(DataInputStream stream, int depth) throws IOException {
		int typeCode = stream.readByte() & 0xFF;
		
		if (depth == 0 && typeCode == TagType.TYPE_NULL.getTypeCode()) {
			throw new IOException("Null tag at stream head.");
		}
		
		TagType type = TagType.getType(typeCode);
		if (type.getTypeCode() != typeCode) {
			throw new IOException(String.format("Invalid tag type code: %d.", type));
		}
		
		String tagName = null;
		Tag tag = new NullTag();
		if (typeCode != TagType.TYPE_NULL.getTypeCode()) {
			tagName = readString(stream);
			
			try {
				tag = (Tag) type.getTypeClass().getConstructors()[0].newInstance(tagName);
			} catch (InstantiationException e) {
				e.printStackTrace();
				
				if (e.getCause() != null)
					e.getCause().printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				
				if (e.getCause() != null)
					e.getCause().printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				
				if (e.getCause() != null)
					e.getCause().printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				
				if (e.getCause() != null)
					e.getCause().printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
				
				if (e.getCause() != null)
					e.getCause().printStackTrace();
			}
		}
		
		tag.read(stream, depth);
		
		return tag;
	}
	
	protected final void read(DataInputStream stream) throws IOException {
		read(stream, 0);
	}
	
	protected abstract void read(DataInputStream stream, int depth) throws IOException;
	
	protected void writeHeader(DataOutputStream stream) throws IOException {
		stream.writeByte(getType().getTypeCode());
		
		if (TagType.TYPE_NULL.getTypeCode() != getType().getTypeCode())
			writeString(stream, getName());
	}
	
	protected abstract void writeBody(DataOutputStream stream) throws IOException;
	
	protected final void write(DataOutputStream stream) throws IOException {
		writeHeader(stream);
		
		writeBody(stream);
	}
	
	protected static final void writeHeader(Tag tag, DataOutputStream stream) throws IOException {
		tag.writeHeader(stream);
	}
	
	protected static final void writeBody(Tag tag, DataOutputStream stream) throws IOException {
		tag.writeBody(stream);
	}
	
	protected static final void write(Tag tag, DataOutputStream stream) throws IOException {
		tag.write(stream);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("[%s:%s { %s } ])", getTagString(), getName(), getValueString());
	}
}
