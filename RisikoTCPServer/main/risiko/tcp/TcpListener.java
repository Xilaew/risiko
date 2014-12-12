/**
 * 
 */
package risiko.tcp;


/**
 * @author xilaew
 *
 */
public interface TcpListener {

	public void handleIncomming(String message);

	public void handleOutgoing(String message);

}
