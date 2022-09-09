package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Link;
import ch.unisg.ics.interactions.wot.td.bindings.Response;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.TypeUtil;

import java.util.ArrayList;
import java.util.Collection;

public class OpcUaResponse implements Response {

    private final ResponseStatus status;

    private final DataValue dv;

    public OpcUaResponse(StatusCode opcUaStatus) {
        this.status = getResponseStatus(opcUaStatus);
        this.dv = null;
    }

    public OpcUaResponse(DataValue dv) {
        this.status = getResponseStatus(dv.getStatusCode());
        this.dv = dv;
    }

    @Override
    public ResponseStatus getStatus() {
        return status;
    }

    @Override
    public Object getPayload() {
        // TODO coercition to a Java class
        return dv.getValue().getValue();
    }

    @Override
    public Collection<Link> getLinks() {
        return new ArrayList<>();
    }

    private ResponseStatus getResponseStatus(StatusCode opcUaStatus) {
        // TODO is bad may mean that the request was incorrect
        if (opcUaStatus.isGood()) return Response.ResponseStatus.OK;
        else if (opcUaStatus.isBad()) return Response.ResponseStatus.THING_ERROR;
        else return Response.ResponseStatus.UNKNOWN_ERROR;
    }

}
