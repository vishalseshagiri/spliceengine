/*
 * Copyright 2012 - 2016 Splice Machine, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.splicemachine.db.iapi.stats;

import com.splicemachine.db.agg.Aggregator;
import com.splicemachine.db.iapi.error.StandardException;
import com.splicemachine.db.iapi.types.DataValueDescriptor;
import com.yahoo.sketches.quantiles.ItemsUnion;
import com.yahoo.sketches.theta.Sketches;
import com.yahoo.sketches.theta.Union;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * Aggregator for merging together column statistics.
 *
 */
public class ColumnStatisticsMerge implements Aggregator<ColumnStatisticsImpl, ColumnStatisticsImpl, ColumnStatisticsMerge>, Externalizable {
    protected boolean initialized;
    protected Union thetaSketchUnion;
    protected ItemsUnion quantilesSketchUnion;
    protected com.yahoo.sketches.frequencies.ItemsSketch<DataValueDescriptor> frequenciesSketch;
    protected long nullCount = 0l;
    protected DataValueDescriptor dvd;

    public ColumnStatisticsMerge() {

    }

    public static ColumnStatisticsMerge instance() {
        return new ColumnStatisticsMerge();
    }

    @Override
    public void init() {

    }

    @Override
    public void accumulate(ColumnStatisticsImpl value) throws StandardException {
        ColumnStatisticsImpl columnStatistics = (ColumnStatisticsImpl) value;
        if (!initialized) {
            dvd = columnStatistics.getColumnDescriptor();
            thetaSketchUnion = Sketches.setOperationBuilder().buildUnion();
            quantilesSketchUnion = ItemsUnion.getInstance( ((ColumnStatisticsImpl)value).getColumnDescriptor());
            frequenciesSketch =  dvd.getFrequenciesSketch();
            initialized = true;
        }
        quantilesSketchUnion.update(columnStatistics.quantilesSketch);
        frequenciesSketch.merge(columnStatistics.frequenciesSketch);
        thetaSketchUnion.update(columnStatistics.thetaSketch);
        nullCount =+ columnStatistics.nullCount();
    }

    @Override
    public void merge(ColumnStatisticsMerge otherAggregator) {
        ColumnStatisticsMerge columnStatisticsMerge = (ColumnStatisticsMerge) otherAggregator;
        quantilesSketchUnion.update(columnStatisticsMerge.quantilesSketchUnion.getResult());
        frequenciesSketch.merge(columnStatisticsMerge.frequenciesSketch);
        thetaSketchUnion.update(columnStatisticsMerge.thetaSketchUnion.getResult());
        nullCount =+ columnStatisticsMerge.nullCount;
    }

    @Override
    public ColumnStatisticsImpl terminate() {
        return new ColumnStatisticsImpl(dvd,quantilesSketchUnion.getResult(),frequenciesSketch,thetaSketchUnion.getResult(),nullCount);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
