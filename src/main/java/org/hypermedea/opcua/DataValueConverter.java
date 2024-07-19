package org.hypermedea.opcua;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.Optional;

public class DataValueConverter {

    private DataValueConverter() {}

    public static Literal asLiteral(DataValue dv) {
        Variant var = dv.getValue();
        return ResourceFactory.createTypedLiteral(var.getValue().toString(), getDatatype(var));
    }

    public static DataValue asDataValue(Literal l) {
        Variant v = new Variant(l.asLiteral().getValue());
        // TODO inverse function to getDatatype()
        return DataValue.valueOnly(v);
    }

    public static DataValue asDataValue(Literal l, String datatype) {
        return asDataValue(l);
        // TODO cast if possible
    }

    private static RDFDatatype getDatatype(Variant var) {
        Optional<ExpandedNodeId> dtOpt = var.getDataType();
        if (dtOpt.isEmpty()) return XSDDatatype.XSDstring;

        ExpandedNodeId dt = dtOpt.get();

        if (dt.equalTo(BuiltinDataType.Boolean.getNodeId())) return XSDDatatype.XSDboolean;

        if (dt.equalTo(BuiltinDataType.SByte.getNodeId())) return XSDDatatype.XSDbyte;
        if (dt.equalTo(BuiltinDataType.Int16.getNodeId())) return XSDDatatype.XSDshort;
        if (dt.equalTo(BuiltinDataType.Int32.getNodeId())) return XSDDatatype.XSDint;
        if (dt.equalTo(BuiltinDataType.Int64.getNodeId())) return XSDDatatype.XSDlong;

        if (dt.equalTo(BuiltinDataType.Byte.getNodeId())) return XSDDatatype.XSDunsignedByte;
        if (dt.equalTo(BuiltinDataType.UInt16.getNodeId())) return XSDDatatype.XSDunsignedShort;
        if (dt.equalTo(BuiltinDataType.UInt32.getNodeId())) return XSDDatatype.XSDunsignedInt;
        if (dt.equalTo(BuiltinDataType.UInt64.getNodeId())) return XSDDatatype.XSDunsignedLong;

        if (dt.equalTo(BuiltinDataType.Float.getNodeId())) return XSDDatatype.XSDfloat;
        if (dt.equalTo(BuiltinDataType.Double.getNodeId())) return XSDDatatype.XSDdouble;

        if (dt.equalTo(BuiltinDataType.DateTime.getNodeId())) return XSDDatatype.XSDdateTime;

        return XSDDatatype.XSDstring;
    }

}
