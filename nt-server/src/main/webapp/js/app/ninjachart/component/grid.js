define(function() {
    function PriceCursor(config, selection) {
        this.config = config;
        this.main = selection.append("g").classed("priceCursor", true);
        this.line = this.main.append("line").attr("x2", config.chartWidth);
    }

    PriceCursor.prototype.mousemove = function(coords) {
        this.main.attr("transform", "translate(0," + coords[1] + ")");
    }

    return PriceCursor;
});