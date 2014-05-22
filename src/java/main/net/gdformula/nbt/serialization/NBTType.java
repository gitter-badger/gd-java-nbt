package net.gdformula.nbt.serialization;

import net.gdformula.nbt.tags.MapTag;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 * 
 * @since Early Development Build 1
 */
public interface NBTType {
	/**
	 * 
	 * @return
	 */
	MapTag serialize();
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	boolean deserialize(MapTag nbt);
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	boolean isValidNBT(MapTag nbt);
}
