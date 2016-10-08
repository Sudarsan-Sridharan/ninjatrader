define(function() {
    function DefaultLayout(config, selection) {
        this.config = config;

        selection.classed("ninjachart", true)
            .style("max-width", "100%");
        this.meta = selection.append("div").classed("meta", true);
        this.table = selection.append("div")
            .style("display", "table")
            .style("width", "100%");
        this.chartOuter = this.table.append("div")
            .style("display", "table-cell")
            .style("max-width", 100)
            .append("div")
            .classed("chartOuter", true)
            .style("overflow", "hidden")
            .style("height", config.chartHeight + config.xAxisHeight - 10);
        this.chartInner = this.chartOuter.append("div")
            .classed("chartInner", true)
            .style("overflow", "auto");
        this.yAxis = this.table.append("div")
            .classed("yAxis", true)
            .style("width", "50px")
            .style("display", "table-cell")
            .style("vertical-align", "top")
            .append("svg")
            .attr("width", config.yAxisWidth)
            .attr("height", this.config.chartHeight + 10);
        this.chart = this.chartInner
            .append("svg")
            .classed("chart", true)
            .attr("width", this.config.chartWidth)
            .attr("height", this.config.chartHeight + this.config.xAxisHeight);
    }

    return DefaultLayout;
});