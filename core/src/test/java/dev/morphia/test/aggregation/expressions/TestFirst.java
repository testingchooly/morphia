package dev.morphia.test.aggregation.expressions;

import dev.morphia.aggregation.stages.SetWindowFields.Output;
import dev.morphia.query.Sort;
import dev.morphia.test.ServerVersion;
import dev.morphia.test.aggregation.AggregationTest;

import org.testng.annotations.Test;

import static dev.morphia.aggregation.expressions.AccumulatorExpressions.first;
import static dev.morphia.aggregation.expressions.Expressions.field;
import static dev.morphia.aggregation.stages.Group.group;
import static dev.morphia.aggregation.stages.Group.id;
import static dev.morphia.aggregation.stages.SetWindowFields.setWindowFields;
import static dev.morphia.aggregation.stages.Sort.sort;
import static dev.morphia.test.ServerVersion.v50;

public class TestFirst extends AggregationTest {

    @Test
    public void testExample1() {
        testPipeline(v50, false, false, (aggregation) -> aggregation.pipeline(
                sort()
                        .ascending("item", "date"),
                group(id(field("item")))
                        .field("firstSale", first(field("date")))));
    }

    @Test
    public void testExample2() {
        testPipeline(v50, false, false, (aggregation) -> aggregation.pipeline(
                sort()
                        .ascending("item", "price"),
                group(id(field("item")))
                        .field("inStock", first(field("quantity")))

        ));
    }

    @Test
    public void testExample3() {
        testPipeline(ServerVersion.ANY, false, true, (aggregation) -> aggregation.pipeline(
                setWindowFields()
                        .partitionBy(field("state"))
                        .sortBy(Sort.ascending("orderDate"))
                        .output(Output.output("firstOrderTypeForState")
                                .operator(first(field("type")))
                                .window()
                                .documents("unbounded", "current"))));
    }

}
