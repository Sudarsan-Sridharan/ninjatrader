define(["d3", "require", "./linechart"], function(d3, require) {

    var LineChart = require("./linechart");

    function RSIChart(config, panel) {
        LineChart.call(this, config, panel);
        this.setAjaxUrl("/rsi")
            .setChartName("rsi");
        panel.yAxis.yAxis.tickArguments([5]);
    }

    RSIChart.prototype = Object.create(LineChart.prototype);
    RSIChart.prototype.constructor = RSIChart;

    RSIChart.prototype.getDataDomain = function() {
        return [0, 100];
    };

    return RSIChart;
});