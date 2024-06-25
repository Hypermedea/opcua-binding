package org.hypermedea.opcua;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.hypermedea.ct.RepresentationHandlers;
import org.hypermedea.op.InvalidFormException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WriteOperation extends OpcUaOperation {

    public WriteOperation(String targetURI, Map<String, Object> formFields, OpcUaClient client) {
        super(targetURI, formFields, client);
    }

    @Override
    protected void sendSingleRequest() throws IOException {
        if (payload.isEmpty()) {
            throw new IOException("writeProperty operation cannot be executed: no value to send to OPC UA server");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RepresentationHandlers.serialize(payload, out, getTargetURI());

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        Model m = ModelFactory.createDefaultModel();
        m.read(in, getTargetURI(), "text/turtle");

        DataValue dv = getDataValue(m);
        CompletableFuture<StatusCode> res = client.writeValue(nodeId, dv);

        res.thenAccept(opcUaCode -> {
            onResponse(new OpcUaResponse(WriteOperation.this, opcUaCode));
        });

        // TODO catch error?
    }

    private DataValue getDataValue(Model m) {
        NodeIterator it = m.listObjectsOfProperty(RDF.value);

        if (!it.hasNext())
            throw new InvalidFormException("The provided payload should include at least one rdf:value statement");

        RDFNode l = it.next();

        if (!l.isLiteral())
            throw new InvalidFormException("The rdf:value provided in the payload should be a literal");

        return DataValueConverter.asDataValue(l.asLiteral());
    }

}
