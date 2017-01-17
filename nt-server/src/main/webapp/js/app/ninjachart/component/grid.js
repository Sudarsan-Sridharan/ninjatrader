define(["d3", "require", "./abstractcomponent"], function(d3, require) {

    var AbstractComponent = require("./abstractcomponent");

    var _componentName = "grid";
    var _lineWidth = 99999999; // setting too high value makes lines disappear.

    function Grid(config, panel) {
        AbstractComponent.call(this, config, null, _componentName);

        this.vertical = this.main.append("g").classed("vertical", true);
        this.horizontal = this.main.append("g").classed("horizontal", true);
        this.yAxis = null;

        this._getHorizontalPath = function(price) {
            var y = panel.getYScale()(price);
            return "M0," + y + "H" + _lineWidth;
        };
        this._getVerticalPath = function(date) {
            var x = config.xByDate(date.d) + (config.columnWidth / 2);
            return "M" + x + ",0V" + config.chartHeight;
        };
    }

    Grid.prototype = Object.create(AbstractComponent.prototype);
    Grid.prototype.constructor = Grid;

    Grid.prototype.show = function() {
        this._drawVerticalLines();
        this._drawHorizontalLines();
    };

    Grid.prototype.onZoom = function() {
        var verticalLine = this.vertical.selectAll("path")
        verticalLine.merge(verticalLine)
            .attr("d", this._getVerticalPath);

        var horizontalLine = this.horizontal.selectAll("path")
        horizontalLine.merge(horizontalLine)
            .attr("d", this._getHorizontalPath);
    };

    Grid.prototype.setYAxis = function(yAxis) {
        this.yAxis = yAxis;
        return this;
    };

    /**
     * Draws vertical lines a long the x-axis.
     */
    Grid.prototype._drawVerticalLines = function() {
        if (!this._isXAxisTickDataChanged()) return;

        var verticalLine = this.vertical.selectAll("path")
            .data(this.getConfig().xAxisTickData);

        verticalLine.enter()
            .append("path")
            .attr("d", this._getVerticalPath);
        verticalLine.merge(verticalLine)
            .attr("d", this._getVerticalPath);
        verticalLine.exit()
            .remove();
    };

    /**
     * Draws horizontal lines along the y-axis.
     */
    Grid.prototype._drawHorizontalLines = function() {
        var horizontalLine = this.horizontal.selectAll("path")
            .data(this.yAxis.getTicks());
        horizontalLine.enter()
            .append("path")
            .attr("d", this._getHorizontalPath);
        horizontalLine.merge(horizontalLine)
            .attr("d", this._getHorizontalPath);
        horizontalLine.exit()
            .remove();
    };

    /**
     * Returns true if x-axis data has changed.
     */
    Grid.prototype._isXAxisTickDataChanged = function() {
        var isChanged = this.lastXAxisTickData != this.getConfig().xAxisTickData;
        this.lastXAxisTickData = this.getConfig().xAxisTickData;
        return isChanged
    };

    return Grid;
});