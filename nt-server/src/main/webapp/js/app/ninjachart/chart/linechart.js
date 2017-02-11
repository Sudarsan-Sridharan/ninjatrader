define(["d3", "require", "./abstractchart"], function(d3, require) {

    var AbstractChart = require("./abstractchart");

    var _viewportNumOfBarsPadding = 20;

    function LineChart(config, panel) {
        AbstractChart.call(this, config, panel);

        this.x = function (value) {
            return config.xByDate(value.d) + config.columnWidth / 2
        };
        this.y = function (value) {
            return panel.yScale(value.v)
        };
        var linePath = d3.line()
            .x(this.x)
            .y(this.y);
        this.pathPerLine = function (periodData) {
            return linePath(periodData.values)
        };
    }

    LineChart.prototype = Object.create(AbstractChart.prototype);
    LineChart.prototype.constructor = LineChart;

    LineChart.prototype.show = function() {
        if (!this.data) return;
        this.main.style("visibility", "visible");
        var values = this.getViewportValues(_viewportNumOfBarsPadding);
        this._printLine(values);
        return this;
    };

    LineChart.prototype._printLine = function(values) {
        var config = this.getConfig();
        var line = this.main.selectAll("path")
            .data(this._transformData(values));
        line.enter()
            .append("path")
            .classed("line", true)
            .attr("period", this._getLinePeriodAttr)
            .attr("d", this.pathPerLine)
            .style("fill", "none")
            .style("stroke", function () {
                return config.color.next()
            })
            .style("stroke-width", "0.6px");
        line.merge(line)
            .attr("period", this._getLinePeriodAttr)
            .attr("d", this.pathPerLine);
        line.exit()
            .remove();
    };

    /**
     * Transform data from map of [{period : values}] to array of [{period:period, values:values}].
     * This allows D3 to take the array and print a line for each entry.
     */
    LineChart.prototype._transformData = function(values) {
        var entities = [];
        var periodMap = values;
        for (var period in periodMap) {
            entities.push({period: period, values: periodMap[period]});
        }
        return entities;
    };

    /**
     * Override from AbstractChart.
     * Returns min and max values of all periods.
     */
    LineChart.prototype.getDataDomain = function() {
        if (!this.data || !this.data.values) return null;
        var viewportValues = this.getViewportValues();
        var domainValues = [];
        for (var period in viewportValues) {
            var viewportMin = d3.min(viewportValues[period], this._getValue);
            var viewportMax = d3.max(viewportValues[period], this._getValue);
            domainValues.push(viewportMin);
            domainValues.push(viewportMax);
        }
        var lowest = d3.min(domainValues);
        var highest = d3.max(domainValues);
        return [lowest, highest];
    };

    /**
     * Override from AbstractChart.
     * Called once data is loaded from ajax request.
     */
    LineChart.prototype.onDataLoad = function(data) {
        this._createDateIndexMap(data);
    };

    /**
     * Override from AbstractChart.
     * Returns array of values visible in the viewport.
     */
    LineChart.prototype.getViewportValues = function(numOfBarsPadding) {
        numOfBarsPadding = numOfBarsPadding || 0;

        var viewportDates = this.config.viewportDates;
        var viewportValues = [];

        for (var period in this.data.values) {
            var indexFrom = this.dateIndexMap[period][viewportDates[0]] || 0;
            var indexTo = this.dateIndexMap[period][viewportDates[viewportDates.length-1]]
                || this.data.values[period].length - 1;
            indexTo *= 1 // convert to int

            indexFrom = indexFrom - numOfBarsPadding >= 0 ? indexFrom - numOfBarsPadding : 0;
            indexTo += numOfBarsPadding;
            viewportValues[period] = this.data.values[period].slice(indexFrom, indexTo);
        }
        return viewportValues;
    };

    /**
     * Returns the value of a Value object.
     * Used as callback function in finding the min or max of an array of Value objects.
     */
    LineChart.prototype._getValue = function(value) {
        return value.v;
    };

    /**
     * Returns the period for the period entity.
     */
    LineChart.prototype._getLinePeriodAttr = function(periodEntity) {
        return periodEntity.period;
    };

    /**
     * Creates a map of date to index values; (e.g. [20160101 : 0], [20160102 : 1], [20160103 : 2])
     * This map is used to quickly determine which index a date falls on.
     */
    LineChart.prototype._createDateIndexMap = function(data) {
        this.dateIndexMap = [];
        for (var period in data.values) {
            var values = data.values[period];
            var map = [];
            for (var i in values) {
                map[values[i].d] = i;
            }
            this.dateIndexMap[period] = map;
        }
    };

    return LineChart;
});