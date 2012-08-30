package org.apache.vxquery.datamodel.builders.nodes;

import java.io.DataOutput;
import java.io.IOException;

import org.apache.vxquery.datamodel.values.ValueTag;

import edu.uci.ics.hyracks.data.std.api.IMutableValueStorage;
import edu.uci.ics.hyracks.data.std.api.IValueReference;

public class CommentNodeBuilder extends AbstractNodeBuilder {
    private DataOutput out;

    @Override
    public void reset(IMutableValueStorage mvs) throws IOException {
        out = mvs.getDataOutput();
        out.write(ValueTag.COMMENT_NODE_TAG);
    }

    @Override
    public void finish() throws IOException {
    }

    public void setLocalNodeId(int localNodeId) throws IOException {
        out.writeInt(localNodeId);
    }

    public void setValue(IValueReference value) throws IOException {
        out.write(value.getByteArray(), value.getStartOffset(), value.getLength());
    }
}