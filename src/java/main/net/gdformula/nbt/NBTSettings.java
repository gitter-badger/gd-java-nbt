package net.gdformula.nbt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.gdformula.nbt.Tag;
import net.gdformula.nbt.TagReader;
import net.gdformula.nbt.TagType;
import net.gdformula.nbt.TagWriter;
import net.gdformula.nbt.serialization.NBTType;
import net.gdformula.nbt.tags.ArrayByteTag;
import net.gdformula.nbt.tags.BoolTag;
import net.gdformula.nbt.tags.ByteTag;
import net.gdformula.nbt.tags.DoubleTag;
import net.gdformula.nbt.tags.FloatTag;
import net.gdformula.nbt.tags.IntTag;
import net.gdformula.nbt.tags.ListTag;
import net.gdformula.nbt.tags.LongTag;
import net.gdformula.nbt.tags.MapTag;
import net.gdformula.nbt.tags.ShortTag;
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
	private Map<String, Object> lookupMap;
	
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
	
	/**
	 * 
	 */
	public Map<String, Object> getLookupMap() {
		if (lookupMap == null)
			lookupMap = new HashMap<String, Object>();
		
		return Collections.unmodifiableMap(lookupMap);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.gdformula.nbt.serialization.NBTType#serialize()
	 */
	@SuppressWarnings("unchecked")
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
		
		if (lookupMap != null) {
			for (Entry<String, Object> entry : lookupMap.entrySet()) {
				if (entry.getValue() instanceof byte[]) {
					nbt.addTag(new ArrayByteTag(entry.getKey(), (byte[])entry.getValue()));
				} else if (entry.getValue() instanceof Boolean) {
					nbt.addTag(new BoolTag(entry.getKey(), (boolean)entry.getValue()));
				} else if (entry.getValue() instanceof Byte) {
					nbt.addTag(new ByteTag(entry.getKey(), (byte)entry.getValue()));
				} else if (entry.getValue() instanceof Double) {
					nbt.addTag(new DoubleTag(entry.getKey(), (double)entry.getValue()));
				} else if (entry.getValue() instanceof Float) {
					nbt.addTag(new FloatTag(entry.getKey(), (float)entry.getValue()));
				} else if (entry.getValue() instanceof Integer) {
					nbt.addTag(new IntTag(entry.getKey(), (int)entry.getValue()));
				} else if (entry.getValue() instanceof List<?>) {
					try {
						List<Tag> value = (List<Tag>)entry.getValue();
						if (!value.isEmpty()) {
							TagType type = value.get(0).getType();
							nbt.addTag(new ListTag(entry.getKey(), type.getTypeClass(), value));
						}
					} catch (Exception e) {
						e.printStackTrace();
						
						if (e.getCause() != null)
							e.getCause().printStackTrace();
					}
				} else if (entry.getValue() instanceof Long) {
					nbt.addTag(new LongTag(entry.getKey(), (long)entry.getValue()));
				} else if (entry.getValue() instanceof Map<?, ?>) {
					try {
						Map<String, Tag> value = (Map<String, Tag>)entry.getValue();
						if (!value.isEmpty()) {
							nbt.addTag(new MapTag(entry.getKey(), value));
						}
					} catch (Exception e) {
						e.printStackTrace();
						
						if (e.getCause() != null)
							e.getCause().printStackTrace();
					}
				} else if (entry.getValue() instanceof Short) {
					nbt.addTag(new ShortTag(entry.getKey(), (short)entry.getValue()));
				} else if (entry.getValue() instanceof String) {
					nbt.addTag(new StringTag(entry.getKey(), (String)entry.getValue()));
				} else {
					MapTag nonstandard = new MapTag(null);
					
					if (!serializeNonstandardType(nonstandard, entry))
						System.err.printf("Entry value is not a handled type. Dumped '%s'.", entry.getValue().toString());
					else if (!nonstandard.getValue().isEmpty())
						nbt.addTag(nonstandard.getValue().values().iterator().next());
				}
			}
		}
		
		return nbt;
	}
	
	/**
	 * 
	 * @param nbt
	 * @param entry
	 * @return
	 */
	public abstract boolean serializeNonstandardType(MapTag nbt, Entry<String, Object> entry);
	
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
		
		if (nbt.getValue().isEmpty())
			return false;
		
		lookupMap.clear();
		
		for (Entry<String, Tag> entry : nbt.getValue().entrySet()) {
			lookupMap.put(entry.getKey(), entry.getValue().getValue());
		}
		
		return checkDeserialization();
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean checkDeserialization();
	
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
	public NBTSettings copy() {
		NBTSettings settings = null;
		
		try {
			settings = getClass().newInstance();
			
			Iterator<Entry<String, Object>> itr = lookupMap.entrySet().iterator();
			
			while (itr.hasNext()) {
				Entry<String, Object> next = itr.next();
				settings.lookupMap.put(next.getKey(), next.getValue());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			
			if (e.getCause() != null)
				e.getCause().printStackTrace();
		}
		
		return settings;
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
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(String name, boolean defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof BoolTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((BoolTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public byte getByte(String name) {
		return getByte(name, (byte) 0);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public byte getByte(String name, byte defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof ByteTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((ByteTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public byte[] getBytes(String name) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof ArrayByteTag))
			return null;
		
		return ((ArrayByteTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public double getDouble(String name) {
		return getDouble(name, 0);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public double getDouble(String name, double defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof DoubleTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((DoubleTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public float getFloat(String name) {
		return getFloat(name, 0);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public float getFloat(String name, float defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof FloatTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((FloatTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getInt(String name) {
		return getInt(name, 0);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public int getInt(String name, int defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof IntTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((IntTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public ListTag getList(String name) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof ListTag))
			return null;
		
		return (ListTag) lookupMap.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getLong(String name) {
		return getLong(name, 0);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public long getLong(String name, long defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof LongTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((LongTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public MapTag getMap(String name) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof MapTag))
			return null;
		
		return (MapTag) lookupMap.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public short getShort(String name) {
		return getShort(name, (short) 0);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public short getShort(String name, short defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof ShortTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((ShortTag)lookupMap.get(name)).getValue();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public String getString(String name) {
		return getString(name, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getString(String name, String defaultValue) {
		if (!lookupMap.containsKey(name) || !(lookupMap.get(name) instanceof StringTag)) {
			lookupMap.put(name, defaultValue);
			
			return defaultValue;
		}
		
		return ((StringTag)lookupMap.get(name)).getValue();
	}
}
