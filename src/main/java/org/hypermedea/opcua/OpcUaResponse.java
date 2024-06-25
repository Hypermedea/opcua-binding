package org.hypermedea.opcua;

import jason.asSyntax.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.hypermedea.ct.RepresentationHandlers;
import org.hypermedea.op.BaseResponse;
import org.hypermedea.op.Operation;
import org.hypermedea.op.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OpcUaResponse extends BaseResponse {

    private final ResponseStatus status;

    private final Optional<DataValue> dvOpt;

    public OpcUaResponse(Operation op, StatusCode opcUaStatus) {
        super(op);

        this.status = getResponseStatus(opcUaStatus);
        this.dvOpt = Optional.empty();
    }

    public OpcUaResponse(Operation op, DataValue dv) {
        super(op);

        this.status = getResponseStatus(dv.getStatusCode());
        this.dvOpt = Optional.of(dv);
    }

    @Override
    public ResponseStatus getStatus() {
        return status;
    }

    @Override
    public Collection<Literal> getPayload() {
        Model singleton = ModelFactory.createDefaultModel();

        if (dvOpt.isEmpty()) return List.of();

        try {
            Resource res = ResourceFactory.createResource(operation.getTargetURI());
            singleton.add(res, RDF.value, DataValueConverter.asLiteral(dvOpt.get()));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            singleton.write(out, "N-TRIPLES", operation.getTargetURI());

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            return RepresentationHandlers.deserialize(in, operation.getTargetURI(), "text/turtle");
        } catch (IOException e) {
            // TODO improve log
            e.printStackTrace();
            return List.of();
        }
    }

    private ResponseStatus getResponseStatus(StatusCode opcUaStatus) {
        if (opcUaStatus.isGood()) return Response.ResponseStatus.OK;
        // TODO is bad may mean that the request was incorrect
        else if (opcUaStatus.isBad()) return ResponseStatus.SERVER_ERROR;
        else return Response.ResponseStatus.UNKNOWN_ERROR;
    }

}
