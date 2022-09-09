package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.bindings.Response;
import ch.unisg.ics.interactions.wot.td.schemas.DataSchema;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ReadOperation extends OpcUaOperation {

    public static final Integer DEFAULT_MAX_AGE = 1000;

    public ReadOperation(Form form, OpcUaClient client) {
        super(form, client);
    }

    @Override
    public void setPayload(DataSchema schema, Object payload) {
        // does nothing
    }

    @Override
    public Response execute() throws IOException {
        CompletableFuture<DataValue> res = client.readValue(DEFAULT_MAX_AGE, TimestampsToReturn.Both, nodeId);

        try {
            DataValue dv = res.get();

            // FIXME dv may hold a null value
            return new OpcUaResponse(dv);
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
    }

}
