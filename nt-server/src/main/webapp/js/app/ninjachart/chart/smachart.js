define(["d3", "require", "./linechart"], function(d3, require) {

    var LineChart = require("./linechart");

    function SMAChart(config, panel) {
        LineChart.call(this, config, panel);
        this.setAjaxUrl("/sma")
            .setChartName("sma");
    }

    SMAChart.prototype = Object.create(LineChart.prototype);
    SMAChart.prototype.constructor = SMAChart;

    return SMAChart;
});