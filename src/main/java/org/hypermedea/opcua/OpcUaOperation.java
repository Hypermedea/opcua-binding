package org.hypermedea.opcua;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.hypermedea.op.InvalidFormException;
import org.hypermedea.op.SynchronousOperation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class OpcUaOperation extends SynchronousOperation {

    private final static Pattern QUERY_PATTERN = Pattern.compile("nodeId=(.+)");

    protected final NodeId nodeId;

    protected final OpcUaClient client;

    public OpcUaOperation(String targetURI, Map<String, Object> formFields, OpcUaClient client) {
        super(targetURI, formFields);
        this.client = client;

        String id = null;

        try {
            URI uri = new URI(targetURI);

            if (uri.getQuery() != null) {
                Matcher m = QUERY_PATTERN.matcher(uri.getQuery());
                if (m.matches()) id = m.group(1);
            }

            if (id != null) {
                try {
                    nodeId = NodeId.parse(id);
                } catch (UaRuntimeException e) {
                    throw new InvalidFormException("Invalid NodeId: " + id);
                }
            } else {
                String line1 = "The given target does not include any valid NodeId (mandatory).";
                String line2 = "Usage: tcp.opc://<host>?nodeId=<id>";
                throw new InvalidFormException(line1 + " " + line2);
            }
        } catch (URISyntaxException e) {
            throw new InvalidFormException("Invalid target URI: " + targetURI);
        }
    }

}
