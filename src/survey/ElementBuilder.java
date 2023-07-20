package survey;

/**
 * Indicates ability to create specified object.<p>
 * 
 * The {@code ElementBuilder} interface provides methods to create object of 
 * given type and to recreate object from given one - usually for edit.
 * 
 * @author ArtiFixal
 * @param <T> Type of builded element
 */
public interface ElementBuilder<T> { 
    /**
     * @return Builded up element.
     */
    public T build();
    
    /**
     * Recreates element to edit from given one.
	 * 
     * @param element Element to edit.
     * @return Success of read.
     */
    public boolean readFrom(T element);
}
