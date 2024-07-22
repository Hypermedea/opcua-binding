package org.hypermedea.opcua;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.Optional;

public class DataValueConverter {

    private DataValueConverter() {}

    public static Literal asLiteral(DataValue dv) {
        Variant var = dv.getValue();
        return ResourceFactory.createTypedLiteral(var.getValue().toString(), getDatatype(var));
    }

    public static DataValue asDataValue(Literal l) {
        return asDataValue(l, null);
    }

    public static DataValue asDataValue(Literal l, String datatype) {
        Variant v = new Variant(getValue(l, datatype));
        return DataValue.valueOnly(v);
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

    private static Object getValue(Literal l, String datatype) {
        if (datatype == null) return l.getValue();

        String lex = l.getLexicalForm();

        switch (datatype) {
            case OPCUA.Boolean: return Boolean.valueOf(lex);

            // FIXME SByte != Byte
            case OPCUA.SByte: return Byte.valueOf(l.getLexicalForm());
            case OPCUA.Int16: return Short.valueOf(l.getLexicalForm());
            case OPCUA.Int32: return Integer.valueOf(l.getLexicalForm());
            case OPCUA.Int64: return Long.valueOf(l.getLexicalForm());

            case OPCUA.Byte: return Byte.valueOf(lex);
            case OPCUA.UInt16: return UShort.valueOf(lex);
            case OPCUA.UInt32: return UInteger.valueOf(lex);
            case OPCUA.UInt64: return ULong.valueOf(lex);

            case OPCUA.Float: return Float.valueOf(lex);
            case OPCUA.Double: return Double.valueOf(lex);

            // TODO
            // case OPCUA.DateTime: return new DateTime(new Date(lex));

            default: return lex;
        }
    }

}
