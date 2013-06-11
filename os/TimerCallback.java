package os;

/**{i}
 * interfaccia per callback di un timer
 *
 * @author M.Moro DEI UNIPD
 * @version 1.00 2003-10-02
 * @version 2.00 2005-10-07 package os
 */

public interface TimerCallback
{
    /**[m][a]
     * chiama la funzione di callback
     */
    void call();

    /**[m][a]
     * stringa descrittiva
     * @return stringa
     */
    String toString();

} //{i} TimerCallback
