package net.gdformula.nbt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.gdformula.nbt.Tag;
import net.gdformula.nbt.TagReader;
import net.gdformula.nbt.TagWriter;
import net.gdformula.nbt.serialization.NBTType;
import net.gdformula.nbt.tags.LongTag;
import net.gdformula.nbt.tags.MapTag;
import net.gdformula.nbt.tags.StringTag;
import net.gdformula.utils.Logger;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 * 
 * @since Early Development Build 1
 */

public abstract class NBTSettings implements NBTType {
	private String file;
	
	/**
	 * 
	 */
	public NBTSettings() {
		this.file = null;
		
		restoreDefaults();
	}
	
	/**
	 * 
	 * @param file
	 */
	public NBTSettings(String file) {
		this.file = file;
		
		if (!load(file)) {
			restoreDefaults();
			
			Logger.global.verbose("Settings file '%s' does not exist. Default settings have been restored and have%s been saved.", file, save() ? "" : " not");
		}
	}
	
	/**
	 * 
	 * @param instance
	 */
	public NBTSettings(NBTSettings instance) {
		this();
		
		if (instance == null)
			throw new IllegalArgumentException("Cannot copy from a null instance.");
		
		deserialize(instance.serialize());
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getFile() {
		return file;
	}
	
	/**
	 * 
	 * @param file
	 */
	public final void setFile(String file) {
		this.file = file;
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract String getTypeName();
	
	/**
	 * 
	 * @return
	 */
	protected abstract long getTypeSerial();
	
	/**
	 * 
	 */
	public void restoreDefaults() {
		deserialize(new MapTag(null));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.gdformula.nbt.serialization.NBTType#serialize()
	 */
	@Override
	public final MapTag serialize() {
		MapTag nbt = new MapTag("nbttype");
		{
			MapTag header = new MapTag("header");
			{
				header.addTag(new StringTag("typename", getTypeName()));
				header.addTag(new LongTag("typeserial", getTypeSerial()));
			}
			
			nbt.addTag(header);
		}
		
		return doSerialize(nbt);
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	protected abstract MapTag doSerialize(MapTag nbt);
	
	/*
	 * (non-Javadoc)
	 * @see net.gdformula.nbt.serialization.NBTType#deserialize(net.gdformula.nbt.tags.MapTag)
	 */
	@Override
	public final boolean deserialize(MapTag nbt) {
		if (nbt == null)
			throw new IllegalArgumentException("Cannot deserialize from a null MapTag.");
		
		if (!isValidNBT(nbt))
			throw new IllegalArgumentException("The provided tag is invalid.");
		
		return doDeserialize(nbt);
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	protected abstract boolean doDeserialize(MapTag nbt);
	
	/*
	 * (non-Javadoc)
	 * @see net.gdformula.nbt.serialization.NBTType#isValidNBT(net.gdformula.nbt.tags.MapTag)
	 */
	@Override
	public boolean isValidNBT(MapTag nbt) {
		if (nbt.getName() == null)
			return true;
		
		if (!nbt.getName().equals("nbttype"))
			return false;
		
		MapTag header = nbt.getMap("header");
		if (header != null && getTypeName().equals(header.getString("typename")) && getTypeSerial() == header.getLong("typeserial"))
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean save() {
		return save(file);
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean save(String file) {
		try {
			return save(new FileOutputStream(file, true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param stream
	 * @return
	 */
	public boolean save(OutputStream stream) {
		TagWriter writer = null;
		
		try {
			writer = new TagWriter(stream);
			writer.write(serialize());
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					
					if (e.getCause() != null)
						e.getCause().printStackTrace();
				}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean load() {
		return load(file);
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean load(String file) {
		try {
			return load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param stream
	 * @return
	 */
	public boolean load(InputStream stream) {
		TagReader reader = null;
		
		try {
			reader = new TagReader(stream);
			
			Tag tag = reader.read();
			if (tag instanceof MapTag) {
				deserialize((MapTag)tag);
				
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					
					if (e.getCause() != null)
						e.getCause().printStackTrace();
				}
		}
		
		return false;
	}
}
