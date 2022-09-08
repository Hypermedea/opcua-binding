package org.hypermedea.opcua;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class OpcUaBindingTest {

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

        for (NodeId var : vars) {
            DataValue val = client.readValue(1000, TimestampsToReturn.Both, var).get();
            System.out.println(val.getValue());
        }
    }

    @Test
    void testReadProperty() {
        // TODO
    }

    @Test
    void testWriteProperty() {
        // TODO
    }

    private static void getAllChildren(OpcUaClient c, NodeId root, List<NodeId> visited, List<NodeId> vars) {
        if (visited.contains(root)) return;
        if (vars.size() > 5) return;

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
