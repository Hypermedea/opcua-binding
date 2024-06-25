package org.hypermedea.opcua;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

public class DataValueConverter {

    private DataValueConverter() {}

    public static Literal asLiteral(DataValue dv) {
        return ResourceFactory.createTypedLiteral(dv.getValue().getValue());
    }

    public static DataValue asDataValue(Literal l) {
        Variant v = new Variant(l.asLiteral().getValue());
        return DataValue.valueOnly(v);
    }

    public static DataValue asDataValue(Literal l, String datatype) {
        return asDataValue(l);
        // TODO cast if possible
    }

}
