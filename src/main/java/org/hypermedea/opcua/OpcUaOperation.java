package org.hypermedea.opcua;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.hypermedea.op.InvalidFormException;
import org.hypermedea.op.SynchronousOperation;

import java.util.Map;

public abstract class OpcUaOperation extends SynchronousOperation {

    protected final NodeId nodeId;

    protected final OpcUaClient client;

    public OpcUaOperation(String targetURI, Map<String, Object> formFields, OpcUaClient client) {
        super(targetURI, formFields);
        this.client = client;

        if (formFields.containsKey(OPCUA.nodeId)) {
            String v = (String) formFields.get(OPCUA.nodeId);
            nodeId = NodeId.parse(v);
        } else {
            throw new InvalidFormException("The given form does not include any NodeId (mandatory)");
        }
    }

}
