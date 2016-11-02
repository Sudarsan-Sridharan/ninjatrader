define(["d3", "require", "./abstractchart"], function(d3, require) {

    var AbstractChart = require("./abstractchart");

    var _removeZeroFilter = function(v) { if (v) return v; };

    function IchimokuChart(config, panel) {
        AbstractChart.call(this, config, panel, "/ichimoku", "ichimoku");

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

        var main = this.getMain();
        this.tenkan = main.append("path").classed("tenkan", true);
        this.kijun = main.append("path").classed("kijun", true);
        this.chikou = main.append("path").classed("chikou", true);
        this.senkouA = main.append("path").classed("senkouA", true);
        this.senkouB = main.append("path").classed("senkouB", true);
        this.kumo = main.append("g").classed("kumo", true);
    }

    IchimokuChart.prototype = Object.create(AbstractChart.prototype);
    IchimokuChart.prototype.constructor = IchimokuChart;

    IchimokuChart.prototype.show = function() {
        if (!this.data) return;
        this.main.style("visibility", "visible");
        this._printTenkan();
        this._printKijun();
        this._printChikou();
        this._printSenkouA();
        this._printSenkouB();
        this._printKumo();
    };

    IchimokuChart.prototype._printTenkan = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.t != 0) return ichimoku; })
            .filter(_removeZeroFilter);
        this.tenkan.datum(data)
            .attr("d", this.tenkanLine);
    };

    IchimokuChart.prototype._printKijun = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.k) return ichimoku; })
            .filter(_removeZeroFilter);
        this.kijun.datum(data)
            .attr("d", this.kijunLine);
    };

    IchimokuChart.prototype._printChikou = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.c) return ichimoku; })
            .filter(_removeZeroFilter);
        this.chikou.datum(data)
            .attr("d", this.chikouLine);
    };

    IchimokuChart.prototype._printSenkouA = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.sa) return ichimoku; })
            .filter(_removeZeroFilter);
        this.senkouA.datum(data)
            .attr("d", this.senkouALine);
    };

    IchimokuChart.prototype._printSenkouB = function() {
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.sb) return ichimoku; })
            .filter(_removeZeroFilter);
        this.senkouB.datum(data)
            .attr("d", this.senkouBLine);
    };

    IchimokuChart.prototype._printKumo = function() {
        var yScale = this.yScale;
        var data = this.data.values.map(function(ichimoku) { if (ichimoku.sb) return ichimoku; })
            .filter(_removeZeroFilter);

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

    /**
     * Override from AbstractChart
     * Returns the ichimoku's lowest
     * Used as callback function when finding the lowest value in an array of ichimoku values.
     */
    IchimokuChart.prototype.getLowestDomainValue = function(ichimoku) {
        return d3.min([ichimoku.t, ichimoku.k, ichimoku.c, ichimoku.sa, ichimoku.sb], _removeZeroFilter);
    };

    /**
     * Override from AbstractChart
     * Returns the ichimoku's highest value.
     * Used as callback function when finding the highest value in an array of ichimoku values.
     */
    IchimokuChart.prototype.getHighestDomainValue = function(ichimoku) {
        return d3.max([ichimoku.t, ichimoku.k, ichimoku.c, ichimoku.sa, ichimoku.sb], _removeZeroFilter);
    };

    return IchimokuChart;
});