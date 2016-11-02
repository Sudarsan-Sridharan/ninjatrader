define(["d3"], function(d3) {

    function LineChart(config, selection) {
        this.config = config;
        this.main = selection.append("g").classed("line", true);
        this.ajaxUrl = "/sma";
        this.indicatorName = "sma";

        this.x = function(value) { return config.xByDate(value.d) + config.columnWidth / 2 };
        this.y = function(value) { return config.yByPrice(value.v)};
        this.linePath = d3.line()
            .x(this.x)
            .y(this.y);

        var linePath = this.linePath;
        this.pathPerLine = function(periodData) { return linePath(periodData.values) }
        this.linePeriod = function(periodData) { return periodData.period };
    }

    LineChart.prototype.show = function(priceData, query) {
        var config = this.config;
        var lineChart = this;
        var fullAjaxUrl = config.contextPath + this.ajaxUrl + query.restUrl(this.indicatorName);

        d3.json(fullAjaxUrl).get(function(data) {
            var entities = [];
            var periodMap = data.values;
            for (var period in periodMap) {
                entities.push({period: period, values:periodMap[period]});
            }
            lineChart.printLine(entities);
        });
        return this;
    };

    LineChart.prototype.printLine = function(data) {
        var config = this.config;
        var line = this.main.selectAll("path")
            .data(data);
        line.enter()
            .append("path")
            .classed("line", true)
            .attr("period", this.linePeriod)
            .attr("d", this.pathPerLine)
            .style("fill", "none")
            .style("stroke", function() { return config.color.next() })
            .style("stroke-width", "1px");
        line.merge(line)
            .attr("period", this.linePeriod)
            .attr("d", this.pathPerLine)
        line.exit()
            .remove();
        return this;
    };

    LineChart.prototype.setAjaxUrl = function(ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    };

    LineChart.prototype.setIndicatorName = function(indicatorName) {
        this.indicatorName = indicatorName;
        return this;
    };

    return LineChart;
});