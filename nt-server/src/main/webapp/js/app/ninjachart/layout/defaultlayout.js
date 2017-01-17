define(function() {

    function DefaultLayout(config, selection) {
        this.config = config;
        this.main = selection.classed("ninjachart", true)
            .style("max-width", "100%")
            .style("border", "1px solid #000")
            .style("height", config.chartHeight + 25)
            .style("overflow", "hidden")
            .style("position", "relative");
        this.scrollable = selection.append("div")
            .style("overflow-x", "scroll")
            .style("width", "100%")
        this.table = this.scrollable.append("table")
            .attr("cellpadding", "0")
            .attr("cellspacing", "0");
        this.tbody = this.table.append("tbody");
        this.tfoot = this.table.append("tfoot");
        this.xAxis = this.tfoot.append("tr")
            .append("td")
            .attr("colspan", "3");
    }

    DefaultLayout.prototype.show = function() {
        this.tbody.selectAll(".canvasCol").style("width", this.config.chartWidth);
    };

    DefaultLayout.prototype.onZoom = function() {
        this.show();
    };

    DefaultLayout.prototype.createPanel = function() {
        var row = this.tbody.append("tr");
        row.append("td")
            .style("position", "absolute")
            .append("div")
            .classed("meta", true);
        row.append("td")
            .classed("canvasCol", true)
            .style("width", this.config.chartWidth)
            .append("div")
            .classed("canvas", true);
        row.append("td")
            .style("position", "absolute")
            .style("width", this.config.yAxisWidth)
            .style("right", "0")
            .append("div")
            .classed("yAxis", true)
            .style("background-color", "#fff");
        return row;
    };

    return DefaultLayout;
});