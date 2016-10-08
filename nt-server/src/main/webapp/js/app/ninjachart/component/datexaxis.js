define(["d3"], function(d3) {
    function PriceYAxis(config, selection) {
        this.config = config;
        this.yAxis = d3.axisRight().scale(config.yByPrice);
        this.main = selection.append("g").classed("priceYAxis", true);
        this.ticks = this.main.append("g").classed("ticks", true);
        this.currentPrice = this.main.append("g").classed("currentPrice", true);

        this.currentPrice.append("rect")
            .attr("width", config.yAxisWidth)
            .attr("height", 18);
        this.currentPriceLabel = this.currentPrice.append("text")
            .attr("x", 9)
            .attr("dy", function(date) { return d3.select(this).node().getBBox().height });
    }

    PriceYAxis.prototype.show = function(priceData) {
        this.ticks.call(this.yAxis);
    };

    PriceYAxis.prototype.getTicks = function() {
        return this.ticks;
    };

    PriceYAxis.prototype.mousemove = function(coords) {
        var config = this.config;
        this.currentPrice.attr("transform", "translate(0," + (coords[1] - 9) + ")")
        this.currentPriceLabel
            .html(function() { return config.priceFormat(config.yByPrice.invert(coords[1])); })
            .attr("dy", function(date) { return d3.select(this).node().getBBox().height });
    };

    return PriceYAxis;
});