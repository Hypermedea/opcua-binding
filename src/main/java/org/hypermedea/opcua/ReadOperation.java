package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ReadOperation extends OpcUaOperation {

    public static final Integer DEFAULT_MAX_AGE = 1000;

    public ReadOperation(Form form, OpcUaClient client) {
        super(form, client);
    }

    @Override
    public void sendRequest() throws IOException {
        CompletableFuture<DataValue> res = client.readValue(DEFAULT_MAX_AGE, TimestampsToReturn.Both, nodeId);

        res.thenAccept(dv -> {
            // FIXME dv may hold a null value
            onResponse(new OpcUaResponse(dv));
        });
    }

    @Override
    protected void setObjectPayload(Map<String, Object> payload) {
        // do nothing
    }

    @Override
    protected void setArrayPayload(List<Object> payload) {
        // do nothing
    }

    @Override
    protected void setStringPayload(String payload) {
        // do nothing
    }

    @Override
    protected void setBooleanPayload(Boolean payload) {
        // do nothing
    }

    @Override
    protected void setIntegerPayload(Long payload) {
        // do nothing
    }

    @Override
    protected void setNumberPayload(Double payload) {
        // do nothing
    }

}
