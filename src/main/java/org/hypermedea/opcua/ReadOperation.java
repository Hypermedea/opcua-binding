package org.hypermedea.opcua;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ReadOperation extends OpcUaOperation {

    public static final Integer DEFAULT_MAX_AGE = 1000;

    public ReadOperation(String targetURI, Map<String, Object> formFields, OpcUaClient client) {
        super(targetURI, formFields, client);
    }

    @Override
    protected void sendSingleRequest() throws IOException {
        CompletableFuture<DataValue> res = client.readValue(DEFAULT_MAX_AGE, TimestampsToReturn.Both, nodeId);

        res.thenAccept(dv -> {
            // FIXME dv may hold a null value
            onResponse(new OpcUaResponse(ReadOperation.this, dv));
        });
    }

}
