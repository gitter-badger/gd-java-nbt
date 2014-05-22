package net.gdformula.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 *
 */
public class TagReader implements Closeable {
	private final DataInputStream stream;
	
	public TagReader(InputStream stream) throws IOException {
		this.stream = new DataInputStream(new GZIPInputStream(stream));
	}
	
	@Override
	public void close() throws IOException {
		stream.close();
	}
	
	public Tag read() throws IOException {
		return Tag.readNext(stream);
	}
	
	public Tag readAt(int depth) throws IOException {
		return Tag.readNext(stream, depth);
	}
}
