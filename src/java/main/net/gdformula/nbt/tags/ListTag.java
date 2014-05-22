package net.gdformula.nbt.tags;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.gdformula.nbt.Tag;
import net.gdformula.nbt.TagType;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 * 
 * @since Early Development Build 1
 */
public class ListTag extends Tag {
	private List<Tag> value;
	private Class<? extends Tag> type;
	
	public ListTag(String name) {
		super(name);
		value = new ArrayList<Tag>();
	}
	
	private ListTag(String name, Class<? extends Tag> type) {
		this(name);
		this.type = type;
	}
	
	public ListTag(String name, Class<? extends Tag> type, List<Tag> value) {
		super(name);
		this.type = type;
		this.value = value;
	}

	@Override
	public TagType getType() {
		return TagType.TYPE_LIST;
	}

	@Override
	public List<Tag> getValue() {
		return value;
	}

	/**
	 * @return the type
	 */
	public Class<? extends Tag> getListType() {
		return type;
	}
	
	@Override
	public Tag copy() {
		ListTag tag = new ListTag(getName(), type);
		
		Iterator<Tag> itr = value.iterator();
		
		while (itr.hasNext())
			tag.value.add(itr.next().copy());
		
		return tag;
	}

	@Override
	protected void read(DataInputStream stream, int depth) throws IOException {
		int listType = stream.readByte();
		
		if (listType == TagType.TYPE_NULL.getTypeCode())
			throw new IOException("Null tags are illegal in List tags.");
		
		int len = stream.readInt();
		value = new ArrayList<Tag>(len);
		
		for (int i = 0; i < len; i++) {
			value.add(Tag.readNext(stream, depth + 1));
		}
		
		type = TagType.getTypeClass(listType);
	}
	
	@Override
	protected void writeBody(DataOutputStream stream) throws IOException {
		Class<? extends Tag> tagClass = type;
		stream.writeByte(TagType.getTypeCode(tagClass));
		
		int size = value.size();
		stream.writeInt(size);
		for (int i = 0; i < size; i++) {
			Tag.write(value.get(i), stream);
		}
	}
}
