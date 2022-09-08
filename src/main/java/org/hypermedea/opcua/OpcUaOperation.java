package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.bindings.Operation;
import ch.unisg.ics.interactions.wot.td.bindings.Response;
import ch.unisg.ics.interactions.wot.td.schemas.DataSchema;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.io.IOException;
import java.util.Map;

abstract public class OpcUaOperation implements Operation {

    protected final Form form;

    protected final NodeId nodeId;

    protected final OpcUaClient client;

    public OpcUaOperation(Form form, OpcUaClient client) {
        this.form = form;
        this.client = client;

        Map<String, Object> kv = form.getAdditionalProperties();

        if (kv.containsKey(OPCUA.nodeId)) {
            String v = (String) kv.get(OPCUA.nodeId);
            nodeId = NodeId.parse(v);
        } else {
            nodeId = null; // throw error instead?
        }
    }

    @Override
    abstract public void setPayload(DataSchema schema, Object payload);

    @Override
    abstract public Response execute() throws IOException;

}
