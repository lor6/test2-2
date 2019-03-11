package com.baeldung.primitives;

import cern.colt.map.AbstractIntDoubleMap;
import cern.colt.map.OpenIntDoubleHashMap;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.map.hash.TDoubleIntHashMap;
import it.unimi.dsi.fastutil.ints.*;
import org.eclipse.collections.api.map.primitive.*;
import org.eclipse.collections.impl.factory.primitive.*;

public class PrimitiveMaps {

    public static void main(String[] args) {

        eclipseCollectionsMap();
        troveMap();
        coltMap();
        fastutilMap();
    }

    private static void fastutilMap() {
        Int2BooleanMap int2BooleanMap = new Int2BooleanOpenHashMap();
        int2BooleanMap.put(1, true);
        int2BooleanMap.put(7, false);
        int2BooleanMap.put(4, true);

        boolean value = int2BooleanMap.get(1);

        Int2BooleanSortedMap int2BooleanSorted = Int2BooleanSortedMaps.EMPTY_MAP;
    }

    private static void coltMap() {
        AbstractIntDoubleMap map = new OpenIntDoubleHashMap();
        map.put(1, 4.5);
        double value = map.get(1);
    }

    private static void eclipseCollectionsMap() {
        MutableIntIntMap mutableIntIntMap = IntIntMaps.mutable.empty();
        mutableIntIntMap.addToValue(1, 1);

        ImmutableIntIntMap immutableIntIntMap = IntIntMaps.immutable.empty();

        MutableObjectDoubleMap<String> dObject = ObjectDoubleMaps.mutable.empty();
        dObject.addToValue("price", 150.5);
        dObject.addToValue("quality", 4.4);
        dObject.addToValue("stability", 0.8);
    }

    private static void troveMap() {
        double[] doubles = new double[] {1.2, 4.5, 0.3};
        int[] ints = new int[] {1, 4, 0};

        TDoubleIntMap doubleIntMap = new TDoubleIntHashMap(doubles, ints);

        doubleIntMap.adjustValue(1.2, 1);
        doubleIntMap.adjustValue(4.5, 4);
        doubleIntMap.adjustValue(0.3, 0);
    }
}
