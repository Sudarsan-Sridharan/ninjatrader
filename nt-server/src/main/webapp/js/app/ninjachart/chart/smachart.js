define(["d3", "require", "./linechart", "../util/smacalcstack"], function(d3, require) {

    var LineChart = require("./linechart");
    var SmaCalcStack = require("../util/smacalcstack");

    function SMAChart(config, panel) {
        LineChart.call(this, config, panel);
        this.setChartName("sma");
        this.calcStacks = [];
    }

    SMAChart.prototype = Object.create(LineChart.prototype);
    SMAChart.prototype.constructor = SMAChart;

    SMAChart.prototype.query = function(query, priceData, successCallback) {
        var periods = query.periodMap["sma"];
        for (var i in periods) {
            var stack = new SmaCalcStack(periods[i]);
            this.calcStacks.push(stack);
        }

        // Prepare SMA values for each period:
        // smaData[18] : [1, 2, 3]
        // smaData[50] : [1, 2, 3]
        var smaData = [];

        for (var i in this.calcStacks) {
            var calcStack = this.calcStacks[i];
            var smaValues = [];
            for (var j in priceData.values) {
                var price = priceData.values[j];
                calcStack.addPrice(price);

                // Create value object containing index and value
                var value = {};
                value.i = j;
                value.v = calcStack.getValue();
                smaValues.push(value);
            }
            smaData[calcStack.maxSize] = smaValues;
        }
        this.data = smaData;

        if (successCallback) {
            successCallback(this);
        }
    }

    return SMAChart;
});
