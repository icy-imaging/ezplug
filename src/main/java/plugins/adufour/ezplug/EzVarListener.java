package plugins.adufour.ezplug;

/**
 * Interface used to fire events occurring on variables.
 * 
 * @author Alexandre Dufour
 * @param <T>
 *        Type held by the variable that changes are fired.
 */
public interface EzVarListener<T>
{
    /**
     * @param source
     *        Variable on which the value change has been done.
     * @param newValue
     *        The new value of the variable.
     */
    void variableChanged(EzVar<T> source, T newValue);
}
