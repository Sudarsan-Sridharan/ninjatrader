define(["d3"], function(d3) {

    var _removeZeroFilter = function(v) { if (v) return v; };

    function IchimokuChart(config, panel) {
        this.config = config;
        this.panel = panel;
        this.yScale = panel.getYScale();
        this.main = d3.select(document.createElementNS(d3.namespaces.svg, 'g'))
            .classed("ichimoku", true);
        this.removeEmptyFilter = function(e) {return e};
        this.x = function(ichimoku) { return config.xByDate(ichimoku.d) + config.columnWidth / 2 };

        var yScale = this.yScale;
        this.tenkanLine = d3.line()
            .x(this.x)
            .y(function(ichimoku) { return yScale(ichimoku.t); });
        this.kijunLine = d3.line()
            .x(this.x)
            .y(function(ichimoku) { return yScale(ichimoku.k); });
        this.chikouLine = d3.line()
            .x(this.x)
            .y(function(ichimoku) { return yScale(ichimoku.c); });
        this.senkouALine = d3.line()
            .x(this.x)
            .y(function(ichimoku) { return yScale(ichimoku.sa); });
        this.senkouBLine = d3.line()
            .x(this.x)
            .y(function(ichimoku) { return yScale(ichimoku.sb); });

        this.tenkan = this.main.append("path").classed("tenkan", true);
        this.kijun = this.main.append("path").classed("kijun", true);
        this.chikou = this.main.append("path").classed("chikou", true);
        this.senkouA = this.main.append("path").classed("senkouA", true);
        this.senkouB = this.main.append("path").classed("senkouB", true);
        this.kumo = this.main.append("g").classed("kumo", true);
    }

    IchimokuChart.prototype.getNode = function() {
        return this.main.node();
    };

    IchimokuChart.prototype.query = function(query, priceData, successCallback) {
        this.data = null;
        var ichimokuChart = this;
        var fullAjaxUrl = this.config.contextPath + "/ichimoku" + query.restUrl();
        d3.json(fullAjaxUrl)
            .on("error", function(error) {
                console.error("Failed to get ichimoku data for symbol: " + query.symbol)
            })
            .on("load", function(data) {
                ichimokuChart.data = data;
                if (successCallback) {
                    setTimeout(function() {successCallback(ichimokuChart)}, 4000);
                    // successCallback(ichimokuChart);
                }
            })
            .get();
    };

    IchimokuChart.prototype.hide = function() {
        this.main.style("visibility", "hidden");
        return this;
    }

    IchimokuChart.prototype.show = function() {
        if (!this.data) return;
        this.main.style("visibility", "visible");
        this.printTenkan();
        this.printKijun();
        this.printChikou();
        this.printSenkouA();
        this.printSenkouB();
        this._printKumo();
    };

    IchimokuChart.prototype.printTenkan = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.t != 0) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.tenkan.datum(data)
            .attr("d", this.tenkanLine);
    };

    IchimokuChart.prototype.printKijun = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.k) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.kijun.datum(data)
            .attr("d", this.kijunLine);
    };

    IchimokuChart.prototype.printChikou = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.c) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.chikou.datum(data)
            .attr("d", this.chikouLine);
    };

    IchimokuChart.prototype.printSenkouA = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.sa) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.senkouA.datum(data)
            .attr("d", this.senkouALine);
    };

    IchimokuChart.prototype.printSenkouB = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.sb) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.senkouB.datum(data)
            .attr("d", this.senkouBLine);
    };

    IchimokuChart.prototype._printKumo = function() {
        var yScale = this.yScale;
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.sb) return ichimoku; })
            .filter(this.removeEmptyFilter);

        var line = this.kumo.selectAll("line")
            .data(data);
        line.enter()
            .append("line")
            .classed("up", function(ichimoku) {return ichimoku.sa > ichimoku.sb})
            .attr("x1", this.x)
            .attr("x2", this.x)
            .attr("y1", function(ichimoku) { return yScale(ichimoku.sa) })
            .attr("y2", function(ichimoku) { return yScale(ichimoku.sb) });
        line.merge(line)
            .classed("up", function(ichimoku) {return ichimoku.sa > ichimoku.sb})
            .attr("x1", this.x)
            .attr("x2", this.x)
            .attr("y1", function(ichimoku) { return yScale(ichimoku.sa) })
            .attr("y2", function(ichimoku) { return yScale(ichimoku.sb) });
        line.exit().remove();
    };

    IchimokuChart.prototype.getDataDomain = function() {
        if (!this.data) return null;
        var viewportValues = this._getViewportValues();
        var viewportMin = d3.min(viewportValues, this._getLowestIchimokuValue);
        var viewportMax = d3.max(viewportValues, this._getHighestIchimokuValue);
        return [viewportMin, viewportMax];
    };

    /**
     * Returns array of values visible in the viewport.
     */
    IchimokuChart.prototype._getViewportValues = function() {
        var viewportIndexRange = this.config.viewportIndexRange;
        return this.data.values.slice(viewportIndexRange[0], viewportIndexRange[1]);
    };

    /**
     * Returns the ichimoku's lowest
     * Used as callback function when finding the lowest value in an array of ichimoku values.
     */
    IchimokuChart.prototype._getLowestIchimokuValue = function(ichimoku) {
        return d3.min([ichimoku.t, ichimoku.k, ichimoku.c, ichimoku.sa, ichimoku.sb], _removeZeroFilter);
    };

    /**
     * Returns the ichimoku's highest value.
     * Used as callback function when finding the highest value in an array of ichimoku values.
     */
    IchimokuChart.prototype._getHighestIchimokuValue = function(ichimoku) {
        return d3.max([ichimoku.t, ichimoku.k, ichimoku.c, ichimoku.sa, ichimoku.sb], _removeZeroFilter);
    };

    return IchimokuChart;
});