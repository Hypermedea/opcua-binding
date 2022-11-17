package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.schemas.DataSchema;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WriteOperation extends OpcUaOperation {

    private DataValue dv = null;

    public WriteOperation(Form form, OpcUaClient client) {
        super(form, client);
    }

    @Override
    public void setPayload(DataSchema schema, Object payload) {
        // TODO if datatype known (via form properties), set variant type? Type coercion done on server side?
        super.setPayload(schema, payload);
    }

    @Override
    public void sendRequest() throws IOException {
        if (dv == null) {
            throw new IOException("writeProperty operation cannot be executed: no value to send to OPC UA server");
        }

        CompletableFuture<StatusCode> res = client.writeValue(nodeId, dv);

        res.thenAccept(opcUaCode -> {
            onResponse(new OpcUaResponse(opcUaCode));
        });

        // TODO catch error?
    }

    @Override
    protected void setObjectPayload(Map<String, Object> payload) {
        dv = new DataValue(new Variant(payload));
    }

    @Override
    protected void setArrayPayload(List<Object> payload) {
        dv = new DataValue(new Variant(payload));
    }

    @Override
    protected void setStringPayload(String payload) {
        dv = new DataValue(new Variant(payload));
    }

    @Override
    protected void setBooleanPayload(Boolean payload) {
        dv = new DataValue(new Variant(payload));
    }

    @Override
    protected void setIntegerPayload(Long payload) {
        dv = new DataValue(new Variant(payload));
    }

    @Override
    protected void setNumberPayload(Double payload) {
        dv = new DataValue(new Variant(payload));
    }

}
