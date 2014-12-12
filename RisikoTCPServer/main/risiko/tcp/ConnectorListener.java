package risiko.tcp;

import org.eclipse.emf.ecore.EObject;

public interface ConnectorListener {

	public void handleIncomming(EObject object);

	public void handleOutgoing(EObject object);

}
