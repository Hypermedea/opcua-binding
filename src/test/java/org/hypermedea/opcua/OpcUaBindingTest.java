package org.hypermedea.opcua;

import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import org.apache.jena.vocabulary.RDF;
import org.hypermedea.ct.RepresentationHandlers;
import org.hypermedea.op.Operation;
import org.hypermedea.op.ProtocolBindings;
import org.hypermedea.op.Response;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class OpcUaBindingTest {

    public static final String OPC_UA_ENDPOINT = "opc.tcp://milo.digitalpetri.com:62541/milo";

    public static final String VARIABLE_TO_READ = "ns=2;s=Dynamic/RandomInt32"; // also: Int16, Int64

    public static final String VARIABLE_TO_WRITE = "ns=2;i=34"; // any integer is recognized by the server

    @Test
    void testReadProperty() throws IOException {
        Map<String, Object> f = new HashMap<>();
        f.put(Operation.METHOD_NAME_FIELD, Operation.GET);

        Operation op = ProtocolBindings.bind(getTarget(OPC_UA_ENDPOINT, VARIABLE_TO_READ), f);
        op.sendRequest();
        Response res = op.getResponse();

        assertEquals(Response.ResponseStatus.OK, res.getStatus());

        assertEquals(1, res.getPayload().size());

        Literal dv = res.getPayload().stream().findFirst().get();

        assertEquals("rdf", dv.getFunctor());
        assertInstanceOf(NumberTerm.class, dv.getTerm(2));
    }

    @Test
    void testWriteProperty() throws IOException {
        Map<String, Object> f = new HashMap<>();
        f.put(Operation.METHOD_NAME_FIELD, Operation.PUT);
        f.put(OPCUA.nodeId, VARIABLE_TO_WRITE);

        String target = getTarget(OPC_UA_ENDPOINT, VARIABLE_TO_WRITE);
        Operation op = ProtocolBindings.bind(target, f);

        String t = String.format("<%s> <%s> 234 .", target, RDF.value.getURI());
        InputStream in = new ByteArrayInputStream(t.getBytes(StandardCharsets.UTF_8));
        Collection<Literal> payload = RepresentationHandlers.deserialize(in, OPC_UA_ENDPOINT, "text/turtle");
        op.setPayload(payload);
        
        op.sendRequest();
        Response res = op.getResponse();

        assertEquals(Response.ResponseStatus.OK, res.getStatus());
    }

    private String getTarget(String endpoint, String nodeId) {
        return String.format("%s?nodeId=%s", endpoint, nodeId);
    }

}
