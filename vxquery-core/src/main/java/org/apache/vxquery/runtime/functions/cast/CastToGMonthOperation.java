package org.apache.vxquery.runtime.functions.cast;

import java.io.DataOutput;
import java.io.IOException;

import org.apache.vxquery.datamodel.accessors.atomic.XSDatePointable;
import org.apache.vxquery.datamodel.accessors.atomic.XSDateTimePointable;
import org.apache.vxquery.datamodel.util.DateTime;
import org.apache.vxquery.datamodel.values.ValueTag;
import org.apache.vxquery.exceptions.ErrorCode;
import org.apache.vxquery.exceptions.SystemException;
import org.apache.vxquery.runtime.functions.strings.ICharacterIterator;
import org.apache.vxquery.runtime.functions.strings.UTF8StringCharacterIterator;

import edu.uci.ics.hyracks.data.std.primitive.UTF8StringPointable;

public class CastToGMonthOperation extends AbstractCastToOperation {

    @Override
    public void convertDate(XSDatePointable datep, DataOutput dOut) throws SystemException, IOException {
        dOut.write(ValueTag.XS_G_MONTH_TAG);
        dOut.writeShort((short) 1972);
        dOut.write((byte) datep.getMonth());
        dOut.write((byte) 29);
        dOut.write((byte) DateTime.TIMEZONE_HOUR_NULL);
        dOut.write((byte) DateTime.TIMEZONE_MINUTE_NULL);
    }

    @Override
    public void convertDatetime(XSDateTimePointable datetimep, DataOutput dOut) throws SystemException, IOException {
        dOut.write(ValueTag.XS_G_MONTH_TAG);
        dOut.writeShort((short) 1972);
        dOut.write((byte) datetimep.getMonth());
        dOut.write((byte) 29);
        dOut.write((byte) 0);
        dOut.write((byte) 0);
        dOut.writeInt((int) 0);
        dOut.write((byte) DateTime.TIMEZONE_HOUR_NULL);
        dOut.write((byte) DateTime.TIMEZONE_MINUTE_NULL);
    }

    @Override
    public void convertGMonth(XSDatePointable datep, DataOutput dOut) throws SystemException, IOException {
        dOut.write(ValueTag.XS_G_MONTH_TAG);
        dOut.write(datep.getByteArray(), datep.getStartOffset(), datep.getLength());
    }

    @Override
    public void convertString(UTF8StringPointable stringp, DataOutput dOut) throws SystemException, IOException {
        ICharacterIterator charIterator = new UTF8StringCharacterIterator(stringp);
        charIterator.reset();
        int c;
        int index = 0;
        long[] date = new long[3];
        boolean positiveTimezone = false;

        // Set defaults
        date[1] = DateTime.TIMEZONE_HOUR_NULL;
        date[2] = DateTime.TIMEZONE_MINUTE_NULL;

        while ((c = charIterator.next()) != ICharacterIterator.EOS_CHAR) {
            if (Character.isDigit(c)) {
                date[index] = date[index] * 10 + Character.getNumericValue(c);
            } else if (c == Character.valueOf('-') || c == Character.valueOf(':')) {
                ++index;
            } else if (c == Character.valueOf('+')) {
                positiveTimezone = true;
                ++index;
            } else {
                // Invalid date format.
                throw new SystemException(ErrorCode.FORG0001);
            }
        }
        if (!DateTime.valid(1972, date[0], 29, 0, 0, 0, date[1], date[2])) {
            throw new SystemException(ErrorCode.FODT0001);
        }

        dOut.write(ValueTag.XS_G_MONTH_TAG);
        dOut.writeShort((short) 1972);
        dOut.write((byte) date[0]);
        dOut.write((byte) 29);
        dOut.write((byte) ((positiveTimezone ? 1 : -1) * date[1]));
        dOut.write((byte) ((positiveTimezone ? 1 : -1) * date[2]));
    }

    @Override
    public void convertUntypedAtomic(UTF8StringPointable stringp, DataOutput dOut) throws SystemException, IOException {
        convertString(stringp, dOut);
    }

}