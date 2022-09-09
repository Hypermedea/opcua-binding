package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.bindings.Operation;
import ch.unisg.ics.interactions.wot.td.bindings.ProtocolBindings;
import ch.unisg.ics.interactions.wot.td.bindings.Response;
import ch.unisg.ics.interactions.wot.td.schemas.IntegerSchema;
import ch.unisg.ics.interactions.wot.td.vocabularies.TD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class OpcUaBindingTest {

    public static final String OPC_UA_ENDPOINT = "opc.tcp://milo.digitalpetri.com:62541/milo";

    public static final String VARIABLE_TO_READ = "ns=2;s=Dynamic/RandomInt32"; // also: Int16, Int64

    public static final String VARIABLE_TO_WRITE = "ns=2;i=34"; // any integer is recognized by the server

    @BeforeAll
    static void init() {
        ProtocolBindings.registerBinding(OpcUaBinding.OPC_UA_SCHEME, OpcUaBinding.class.getName());
    }

    @Test
    void testReadProperty() throws IOException {
        Form f = new Form.Builder(OPC_UA_ENDPOINT)
                .addOperationType(TD.readProperty)
                .addProperty(OPCUA.nodeId, VARIABLE_TO_READ)
                .build();

        Operation op = ProtocolBindings.getBinding(f).bind(f, TD.readProperty);
        Response res = op.execute();

        assertEquals(Response.ResponseStatus.OK, res.getStatus());
        assertInstanceOf(Integer.class, res.getPayload());
    }

    @Test
    void testWriteProperty() throws IOException {
        Form f = new Form.Builder(OPC_UA_ENDPOINT)
                .addOperationType(TD.writeProperty)
                .addProperty(OPCUA.nodeId, VARIABLE_TO_WRITE)
                .build();

        Operation op = ProtocolBindings.getBinding(f).bind(f, TD.writeProperty);
        op.setPayload(new IntegerSchema.Builder().build(), 234);
        Response res = op.execute();

        assertEquals(Response.ResponseStatus.OK, res.getStatus());
    }

}
