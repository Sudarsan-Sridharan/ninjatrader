define(["d3"], function(d3) {

    var currentValueBoxHeight = null;

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
        this.currentValue = this.main.append("g")
            .classed("currentValue", true);
        this.currentValue.append("rect")
            .attr("width", config.yAxisWidth)
            .attr("height", 18);
        this.currentValueLabel = this.currentValue.append("text")
            .attr("x", 9)
            .attr("dy", "-200");

        this.coords = [0, -100];
        this.lastYScaleDomain = [0, 0];

        this.onMouseMove(this.coords);
    }

    YAxis.prototype.show = function() {
        this.ticks.call(this.yAxis);
    };

    YAxis.prototype.getNode = function() {
        return this.main.node();
    }

    YAxis.prototype.getTicks = function() {
        var tickData = [];
        this.ticks.selectAll(".tick").each(function(value) { tickData.push(value)});
        return tickData;
    };

    /**
     * On mouse move, update the current value box with the given coordinates.
     */
    YAxis.prototype.onMouseMove = function(coords) {
        this.coords = coords;
        this.currentValue.attr("transform", "translate(0," + (coords[1] - 9) + ")");
        this._updateCurrentValueLabel();
    };

    /**
     * Updates the label of the current value box.
     */
    YAxis.prototype._updateCurrentValueLabel = function() {
        this.currentValueLabel
            .html(this._getCurrentValueBoxLabel())
            .attr("dy", this._getCurrentValueBoxHeight);
    };

    /**
     * Calculates for the height of the current value box.
     */
    YAxis.prototype._getCurrentValueBoxHeight = function() {
        if (!currentValueBoxHeight) {
            currentValueBoxHeight = d3.select(this).node().getBBox().height;
        }
        return currentValueBoxHeight;
    };

    /**
     * Returns value depending on current mouse location.
     */
    YAxis.prototype._getCurrentValueBoxLabel = function() {
        return this.config.priceFormat(this.yScale.invert(this.coords[1]));
    };

    return YAxis;
});