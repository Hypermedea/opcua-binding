package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.bindings.Response;
import ch.unisg.ics.interactions.wot.td.schemas.DataSchema;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WriteOperation extends OpcUaOperation {

    private DataValue dv = null;

    public WriteOperation(Form form, OpcUaClient client) {
        super(form, client);
    }

    @Override
    public void setPayload(DataSchema schema, Object payload) {
        // TODO if object not of basic type, throw exception
        // TODO if datatype known (via form properties), set variant type? Type coercion done on server side?
        dv = new DataValue(new Variant(payload));
    }

    @Override
    public Response execute() throws IOException {
        if (dv == null) {
            // TODO throw exception (insufficient information)
        }

        CompletableFuture<StatusCode> res = client.writeValue(nodeId, dv);

        try {
            StatusCode opcUaCode = res.get();

            Response.ResponseStatus opCode;

            if (opcUaCode.isGood()) opCode = Response.ResponseStatus.OK;
            else if (opcUaCode.isBad()) opCode = Response.ResponseStatus.THING_ERROR;
            else opCode = Response.ResponseStatus.UNKNOWN_ERROR;

            return new OpcUaResponse(opCode);
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
    }

}
