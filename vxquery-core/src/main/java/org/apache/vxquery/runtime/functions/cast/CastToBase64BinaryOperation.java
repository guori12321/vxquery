package org.apache.vxquery.runtime.functions.cast;

import java.io.DataOutput;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.vxquery.datamodel.accessors.atomic.XSBinaryPointable;
import org.apache.vxquery.datamodel.values.ValueTag;
import org.apache.vxquery.exceptions.SystemException;

import edu.uci.ics.hyracks.data.std.primitive.UTF8StringPointable;
import edu.uci.ics.hyracks.data.std.util.ByteArrayAccessibleOutputStream;

public class CastToBase64BinaryOperation extends AbstractCastToOperation {

    @Override
    public void convertBase64Binary(XSBinaryPointable binaryp, DataOutput dOut) throws SystemException, IOException {
        dOut.write(ValueTag.XS_BASE64_BINARY_TAG);
        dOut.write(binaryp.getByteArray(), binaryp.getStartOffset(), binaryp.getLength());
    }

    @Override
    public void convertHexBinary(XSBinaryPointable binaryp, DataOutput dOut) throws SystemException, IOException {
        dOut.write(ValueTag.XS_BASE64_BINARY_TAG);
        dOut.write(binaryp.getByteArray(), binaryp.getStartOffset(), binaryp.getLength());
    }

    @Override
    public void convertString(UTF8StringPointable stringp, DataOutput dOut) throws SystemException, IOException {
        ByteArrayAccessibleOutputStream baaos = new ByteArrayAccessibleOutputStream();
        Base64OutputStream b64os = new Base64OutputStream(baaos, true);
        b64os.write(stringp.getByteArray(), stringp.getStartOffset() + 2, stringp.getUTFLength());

        dOut.write(ValueTag.XS_STRING_TAG);
        dOut.write((byte) ((baaos.size() >>> 8) & 0xFF));
        dOut.write((byte) ((baaos.size() >>> 0) & 0xFF));
        dOut.write(baaos.getByteArray());
    }

    @Override
    public void convertUntypedAtomic(UTF8StringPointable stringp, DataOutput dOut) throws SystemException, IOException {
        convertString(stringp, dOut);
    }
}