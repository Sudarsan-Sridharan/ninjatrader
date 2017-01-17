define(["d3", "require", "../util/date"], function(d3, require) {

    var _lineSeparatorWidth = 99999999;

    function DateXAxis(config, selection) {
        this.config = config;
        this.main = selection.append("svg")
            .attr("class", "xAxis")
            .attr("height", config.xAxisHeight);
        this.lineSeparator = this.main.append("path")
            .attr("d", "M0,1V0H" + _lineSeparatorWidth +"V1");
        this.ticks = this.main.append("g").classed("ticks", true);
        this.targetDate = this.main.append("g").classed("targetDate", true);

        this.targetDate.append("rect")
            .attr("width", 60)
            .attr("height", config.xAxisHeight)
            .attr("x", -30);

        this.targetDateLabel = this.targetDate.append("text")
            .attr("y", 6)
            .html("x")
            .attr("dy", function() { return d3.select(this).node().getBBox().height })
            .attr("dx", function() { return -d3.select(this).node().getBBox().width/2 });

        this._tickTransform = function(date) { return "translate(" + (config.xByDate(date.d) + config.columnWidth / 2) + ")" };
        this._tickLabel = function(date) { return date.month == 1 ? date.year : Date.monthNames[date.month - 1] };
        this._tickClass = function(date) { return date.month == 1 };
        this._tickLabelDX = function() { return -d3.select(this).node().getBBox().width / 2 };
        this._tickLabelDY = function() { return d3.select(this).node().getBBox().height };
    }

    DateXAxis.prototype.show = function() {
        this.setWidth(this.config.chartWidth);
        this._drawTicks();
    };

    DateXAxis.prototype._drawTicks = function() {
        var tick = this.ticks.selectAll("g")
            .data(this.config.xAxisTickData);
        var tickEnter = tick.enter()
            .append("g")
            .classed("tick", true)
            .attr("transform", this._tickTransform);
        tickEnter.append("line")
            .attr("y2", 6);
        tickEnter.append("text")
            .text(this._tickLabel)
            .attr("y", 6)
            .attr("dx", this._tickLabelDX)
            .attr("dy", this._tickLabelDY)
            .classed("year", this._tickClass);

        var tickMerge = tick.merge(tick);
        tickMerge.attr("transform", this._tickTransform);
        tickMerge.select("text")
            .classed("year", this._tickClass)
            .text(this._tickLabel)
            .attr("dx", this._tickLabelDX)
            .attr("dy", this._tickLabelDY);

        tick.exit()
            .remove();
    };

    DateXAxis.prototype.onZoom = function() {
        this.setWidth(this.config.chartWidth);
        var tick = this.ticks.selectAll("g")
        tick.merge(tick)
            .attr("transform", this._tickTransform);
    };

    DateXAxis.prototype.onMouseMove = function(coordinates) {
        this._updateTargetDate(coordinates);
    };

    /**
     * Updates the x coordinate of the target date box and its label.
     */
    DateXAxis.prototype._updateTargetDate = function(coordinates) {
        if (!this.config.dates) return;

        var config = this.config;
        var index = Math.floor(config.xByIndex.invert(coordinates[0]));
        var x = config.xByIndex(index) + config.columnWidth / 2;
        var date = config.dates[index];
        var dateLabel = date ? Date.parseDbFormat(date).toHumanFormat() : "";

        this.targetDate.attr("transform", function() {
            return "translate(" + x + ")"
        });
        this.targetDateLabel
            .html(function() { return dateLabel })
            .attr("dx", function() { return -d3.select(this).node().getBBox().width/2 });
    }

    DateXAxis.prototype.setWidth = function(width) {
        this.main.attr("width", width);
        return this;
    };

    DateXAxis.prototype.getTicks = function() {
        return this.ticks;
    };

    return DateXAxis;
});