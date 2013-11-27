package com.splicemachine.derby.impl.sql.execute.operations;

import com.splicemachine.constants.bytes.BytesUtil;
import com.splicemachine.derby.hbase.SpliceDriver;
import com.splicemachine.derby.impl.storage.BaseHashAwareScanBoundary;
import com.splicemachine.derby.utils.DerbyBytesUtil;
import com.splicemachine.derby.utils.marshall.RowDecoder;
import com.splicemachine.encoding.MultiFieldDecoder;
import com.splicemachine.encoding.MultiFieldEncoder;
import org.apache.hadoop.hbase.client.Result;

/**
 * @author Scott Fines
 *         Created on: 10/30/13
 */
public class MergeSortScanBoundary extends BaseHashAwareScanBoundary{
		private final RowDecoder rightDecoder;

    public MergeSortScanBoundary(byte[] columnFamily, RowDecoder rightDecoder) {
        super(columnFamily);
        this.rightDecoder = rightDecoder;
    }

    @Override
    public byte[] getStartKey(Result result) {
        return getKey(result);
    }

    @Override
    public byte[] getStopKey(Result result) {
        byte[] startKey = getKey(result);
        BytesUtil.unsignedIncrement(startKey,startKey.length-1);
        return startKey;
    }

    private byte[] getKey(Result result) {
        byte[] data = result.getRow();
        if(data==null) return null;

        MultiFieldDecoder decoder = MultiFieldDecoder.wrap(data, SpliceDriver.getKryoPool());

        decoder.seek(11); //skip the prefix value
				byte[] joinData;
				//copy out all the fields from the key until we reach the ordinal
				joinData = DerbyBytesUtil.slice(decoder,rightDecoder.getKeyColumns(),rightDecoder.getTemplate().getRowArray());
        decoder.reset();
        MultiFieldEncoder encoder = MultiFieldEncoder.create(SpliceDriver.getKryoPool(),2);
        encoder.setRawBytes(decoder.slice(10));
        encoder.setRawBytes(joinData);
        return encoder.build();
    }
}
