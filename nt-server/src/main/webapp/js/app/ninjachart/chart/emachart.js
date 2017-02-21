define(["d3", "require", "./linechart", "../util/emacalcstack"], function(d3, require) {

    var LineChart = require("./linechart");
    var EmaCalcStack = require("../util/emacalcstack");

    function EMAChart(config, panel) {
        LineChart.call(this, config, panel);
        this.setAjaxUrl("/ema")
            .setChartName("ema");
        this.calcStacks = [];
    }

    EMAChart.prototype = Object.create(LineChart.prototype);
    EMAChart.prototype.constructor = EMAChart;

    EMAChart.prototype.query = function(query, priceData, successCallback) {
        var periods = query.periodMap["ema"];
        for (var i in periods) {
            var stack = new EmaCalcStack(periods[i]);
            this.calcStacks.push(stack);
        }

        // Prepare EMA values for each period:
        // emaData[18] : [1, 2, 3]
        // emaData[50] : [1, 2, 3]
        var emaData = [];

        for (var i in this.calcStacks) {
            var calcStack = this.calcStacks[i];
            var emaValues = [];
            for (var j in priceData.values) {
                var price = priceData.values[j];
                calcStack.addPrice(price);

                // Create value object containing index and value
                var value = {};
                value.i = j;
                value.v = calcStack.getValue();
                emaValues.push(value);
            }
            emaData[calcStack.maxSize] = emaValues;
        }
        this.data = emaData;

        if (successCallback) {
            successCallback(this);
        }
    };

    return EMAChart;
});
