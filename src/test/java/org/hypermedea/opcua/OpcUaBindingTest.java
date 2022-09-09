package org.hypermedea.opcua;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.bindings.Operation;
import ch.unisg.ics.interactions.wot.td.bindings.ProtocolBindings;
import ch.unisg.ics.interactions.wot.td.bindings.Response;
import ch.unisg.ics.interactions.wot.td.vocabularies.TD;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpcUaBindingTest {

    public static final String OPC_UA_ENDPOINT = "opc.tcp://milo.digitalpetri.com:62541/milo";

    public static final String TEST_VARIABLE = "ns=0;i=13739";

    public static void main(String[] args) throws ExecutionException, InterruptedException, UaException {
        Function<List<EndpointDescription>, Optional<EndpointDescription>> sel = eps -> eps.stream().findFirst();

        Function<OpcUaClientConfigBuilder, OpcUaClientConfig> build = b ->
                b.setApplicationName(LocalizedText.english("Hypermedea OPC UA protocol binding"))
                        .setApplicationUri("https://hypermedea.github.io/#this")
                        .build();

        OpcUaClient client = OpcUaClient.create("opc.tcp://milo.digitalpetri.com:62541/milo", sel, build);

        client.connect().get(); // blocking operation

        List<NodeId> nodes = new ArrayList<>();
        List<NodeId> vars = new ArrayList<>();
        getAllChildren(client, Identifiers.RootFolder, nodes, vars);

        for (NodeId id : vars) {
            System.out.println(id.toParseableString());
        }

//        for (NodeId var : vars) {
//            DataValue val = client.readValue(1000, TimestampsToReturn.Both, var).get();
//            System.out.println(val.getValue());
//        }
    }

    @Test
    void testReadProperty() throws IOException {
        // TODO fix loading of bindings upstream
        ProtocolBindings.registerBinding(OpcUaBinding.OPC_UA_SCHEME, new OpcUaBinding());

        Form f = new Form.Builder(OPC_UA_ENDPOINT)
                .addOperationType(TD.readProperty)
                .addProperty(OPCUA.nodeId, TEST_VARIABLE)
                .build();

        Operation op = ProtocolBindings.getBinding(f).bind(f, TD.readProperty);
        Response res = op.execute();

        assertEquals(res.getStatus(), Response.ResponseStatus.OK);
        assertNotNull(res.getPayload());
    }

    @Test
    void testWriteProperty() throws IOException {
        // TODO fix loading of bindings upstream
        ProtocolBindings.registerBinding(OpcUaBinding.OPC_UA_SCHEME, new OpcUaBinding());

        Form f = new Form.Builder(OPC_UA_ENDPOINT)
                .addOperationType(TD.writeProperty)
                .addProperty(OPCUA.nodeId, TEST_VARIABLE)
                .build();

        Operation op = ProtocolBindings.getBinding(f).bind(f, TD.writeProperty);
        op.setPayload(null, 234);
        Response res = op.execute();

        // FIXME find a writable variable
        assertEquals(Response.ResponseStatus.OK, res.getStatus());
    }

    private static void getAllChildren(OpcUaClient c, NodeId root, List<NodeId> visited, List<NodeId> vars) {
        if (visited.contains(root)) return;
        if (vars.size() > 10) return;

        try {
            List<ReferenceDescription> res = c.getAddressSpace().browse(root);

            visited.add(root);

            for (ReferenceDescription child : res) {
                NodeId id = child.getNodeId().toNodeIdOrThrow(c.getNamespaceTable());

                if (child.getNodeClass().equals(NodeClass.Variable)) {
                    vars.add(id);
                } else {
                    getAllChildren(c, id, visited, vars);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
