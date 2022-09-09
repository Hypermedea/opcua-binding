package org.hypermedea.opcua;

/**
 * Vocabulary to declare OPC UA form attributes in W3C Thing Description documents, as described in the
 * <a href="https://w3c.github.io/wot-binding-templates/">W3C Web of Things (WoT) Binding Templates</a>
 * specification.
 */
public class OPCUA {

    public static final String NS = "http://opcfoundation.org/UA/";

    /**
     * <p>
     *     NodeId of the Variable to read or write. This attribute is mandatory.
     * </p>
     * <p>
     *     Note that the NodeId cannot be part of the OPC UA endpoint URI (the form's {@code target}).
     *     This URI uniquely identify the server, not individual nodes the server holds. Endpoint URIs that
     *     include a path and/or a fragment are e.g. used to distinguish the target server from other OPC UA
     *     servers running on the same host. See
     *     <a href="https://reference.opcfoundation.org/v105/Core/docs/Part6/7.1.2/#7.1.2.3">7.1.2.3 Hello Message (OPC 10000-6)</a>
     *     for a specification of the OPC UA connection protocol.
     * </p>
     */
    public static final String nodeId = NS + "nodeId";

    /**
     * Maximum time interval between last known acquisition for the Variable's Value and the operation
     * (only relevant for read operations).
     */
    public static final String maxAge = NS + "maxAge";

    /**
     * DataType of the Value held by the referenced Variable.
     * See <a href="https://reference.opcfoundation.org/v105/Core/docs/Part3/#8">8 Standard DataTypes (OPC 10000-3)</a>
     * for the full list. Not all DataTypes are recognized in the current implementation,
     * see below for the list of supported DataTypes.
     */
    public static final String datatype = NS + "datatype";

    public static final String Boolean = NS + "Boolean";

    public static final String Byte = NS + "Byte";

    public static final String Double = NS + "Double";

    public static final String Float = NS + "Float";

    public static final String Int16 = NS + "Int16";

    public static final String Int32 = NS + "Int32";

    public static final String LocalizedText = NS + "LocalizedString";

    public static final String SByte = NS + "SByte";

    public static final String String = NS + "String";

    public static final String UInt16 = NS + "UInt16";

    public static final String UInt32 = NS + "UInt32";

    public static final String UInt64 = NS + "UInt64";

}
