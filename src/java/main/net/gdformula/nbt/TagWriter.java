package net.gdformula.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 *
 */
public class TagWriter implements Closeable {
	private final DataOutputStream stream;
	
	public TagWriter(OutputStream stream) throws IOException {
		this.stream = new DataOutputStream(new GZIPOutputStream(stream));
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}
	
	public void write(Tag tag) throws IOException {
		Tag.write(tag, stream);
	}
}
