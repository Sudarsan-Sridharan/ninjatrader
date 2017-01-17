define(["d3", "require", "./linechart"], function(d3, require) {

    var LineChart = require("./linechart");

    function EMAChart(config, panel) {
        LineChart.call(this, config, panel);
        this.setAjaxUrl("/ema")
            .setChartName("ema");
    }

    EMAChart.prototype = Object.create(LineChart.prototype);
    EMAChart.prototype.constructor = EMAChart;

    return EMAChart;
});