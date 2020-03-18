package dev.morphia.mapping.primitives;


import dev.morphia.TestBase;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.eq;


public class IntegerMappingTest extends TestBase {
    @Test
    public void testMapping() {
        getMapper().map(Integers.class);
        final Integers ent = new Integers();
        ent.listWrapperArray.add(new Integer[]{1, 2});
        ent.listPrimitiveArray.add(new int[]{2, 3, 12});
        ent.listWrapper.addAll(Arrays.asList(1, 2));

        ent.singlePrimitive = 100;
        ent.singleWrapper = 47;
        ent.primitiveArray = new int[]{5, 93};
        ent.wrapperArray = new Integer[]{55, 16, 99};
        ent.nestedPrimitiveArray = new int[][]{{}, {5, 93}};
        ent.nestedWrapperArray = new Integer[][]{{55, 16, 99}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, {0}};
        getDs().save(ent);

        final Integers loaded = getDs().find(Integers.class)
                                       .filter(eq("_id", ent.id))
                                       .first();

        Assert.assertNotNull(loaded.id);

        Assert.assertArrayEquals(ent.listWrapperArray.get(0), loaded.listWrapperArray.get(0));
        Assert.assertArrayEquals(ent.listWrapper.toArray(new Integer[0]), loaded.listWrapper.toArray(new Integer[0]));
        Assert.assertArrayEquals(ent.listPrimitiveArray.get(0), loaded.listPrimitiveArray.get(0));

        Assert.assertEquals(ent.singlePrimitive, loaded.singlePrimitive, 0);
        Assert.assertEquals(ent.singleWrapper, loaded.singleWrapper, 0);

        Assert.assertArrayEquals(ent.primitiveArray, loaded.primitiveArray);
        Assert.assertArrayEquals(ent.wrapperArray, loaded.wrapperArray);
        Assert.assertArrayEquals(ent.nestedPrimitiveArray, loaded.nestedPrimitiveArray);
        Assert.assertArrayEquals(ent.nestedWrapperArray, loaded.nestedWrapperArray);
    }

    @Entity
    private static class Integers {
        private final List<Integer[]> listWrapperArray = new ArrayList<>();
        private final List<int[]> listPrimitiveArray = new ArrayList<>();
        private final List<Integer> listWrapper = new ArrayList<>();
        @Id
        private ObjectId id;
        private int singlePrimitive;
        private Integer singleWrapper;
        private int[] primitiveArray;
        private Integer[] wrapperArray;
        private int[][] nestedPrimitiveArray;
        private Integer[][] nestedWrapperArray;
    }
}
