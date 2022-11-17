package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.bindings.BaseOperation;
import ch.unisg.ics.interactions.wot.td.bindings.InvalidFormException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.util.Map;

abstract public class OpcUaOperation extends BaseOperation {

    protected final Form form;

    protected final NodeId nodeId;

    protected final OpcUaClient client;

    public OpcUaOperation(Form form, OpcUaClient client) throws InvalidFormException {
        this.form = form;
        this.client = client;

        Map<String, Object> kv = form.getAdditionalProperties();

        if (kv.containsKey(OPCUA.nodeId)) {
            String v = (String) kv.get(OPCUA.nodeId);
            nodeId = NodeId.parse(v);
        } else {
            throw new InvalidFormException("The given form does not include any NodeId (mandatory)");
        }
    }

}
