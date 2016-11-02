define(["d3"], function(d3) {
    function YAxis(config, panel) {
        this.config = config;
        this.panel = panel;
        this.yScale = panel.getYScale();
        this.yAxis = d3.axisRight().scale(this.yScale);
        this.main = d3.select(document.createElementNS(d3.namespaces.svg, 'g'))
            .classed("yAxis", true);
        this.ticks = this.main.append("g")
            .classed("ticks", true)
            .call(this.yAxis);
        this.currentValue = this.main.append("g").classed("currentValue", true);
        this.coords = [0,0];
        this.currentValue.append("rect")
            .attr("width", config.yAxisWidth)
            .attr("height", 18);
        this.currentValueLabel = this.currentValue.append("text")
            .attr("x", 9)
            .attr("dy", function(date) { return d3.select(this).node().getBBox().height });
    }

    YAxis.prototype.show = function() {
        this.ticks.call(this.yAxis);
        this.updateCurrentValueLabel();
    };

    YAxis.prototype.getNode = function() {
        return this.main.node();
    }

    YAxis.prototype.getTicks = function() {
        var tickData = [];
        this.ticks.selectAll(".tick").each(function(value) { tickData.push(value)});
        return tickData;
    };

    YAxis.prototype.onMouseMove = function(coords) {
        this.coords = coords;
        this.currentValue.attr("transform", "translate(0," + (coords[1] - 9) + ")")
        this.updateCurrentValueLabel();
    };

    YAxis.prototype.updateCurrentValueLabel = function() {
        var config = this.config;
        var coords = this.coords;
        var yScale = this.yScale;
        this.currentValueLabel
            .html(function() { return config.priceFormat(yScale.invert(coords[1])); })
            .attr("dy", function(date) { return d3.select(this).node().getBBox().height });
    };

    return PriceYAxis;
});