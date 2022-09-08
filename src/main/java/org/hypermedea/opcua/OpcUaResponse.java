package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Link;
import ch.unisg.ics.interactions.wot.td.bindings.Response;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.ArrayList;
import java.util.Collection;

public class OpcUaResponse implements Response {

    private final ResponseStatus status;

    private final DataValue dv;

    public OpcUaResponse(ResponseStatus status) {
        this.status = status;
        this.dv = null;
    }

    public OpcUaResponse(DataValue dv) {
        this.status = ResponseStatus.OK;
        this.dv = dv;
    }

    @Override
    public ResponseStatus getStatus() {
        return status;
    }

    @Override
    public Object getPayload() {
        // TODO coercition to a Java class
        return dv;
    }

    @Override
    public Collection<Link> getLinks() {
        return new ArrayList<>();
    }

}
