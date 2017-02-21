define(["d3", "require", "./abstractchart", "../util/ichimokumeanstack"], function(d3, require) {

    var AbstractChart = require("./abstractchart");
    var IchimokuMeanStack = require("../util/ichimokumeanstack");

    var _getTenkanFilter = function(ichimoku) { if (ichimoku.t) return ichimoku };
    var _getKijunFilter = function(ichimoku) { if (ichimoku.k) return ichimoku };
    var _getChikouFilter = function(ichimoku) { if (ichimoku.c) return ichimoku };
    var _getSenkouAFilter = function(ichimoku) { if (ichimoku.sa) return ichimoku };
    var _getSenkouBFilter = function(ichimoku) { if (ichimoku.sb) return ichimoku };
    var _isSenkouAAboveB = function(ichimoku) { return ichimoku.sa > ichimoku.sb };
    var _removeEmptyFilter = function(v) { if (v) return v };

    function IchimokuChart(config, panel) {
        AbstractChart.call(this, config, panel, "/ichimoku", "ichimoku");

        var futureSenkouPeriods = 26;
        var chikouPastPeriods = 26;

        this._getXByIndex = function(ichimoku) { return config.xByIndex(ichimoku.i) + config.columnWidth / 2 };
        this._getChikouXByIndex = function(ichimoku) { return config.xByIndex(parseInt(ichimoku.i) - chikouPastPeriods) + config.columnWidth / 2 };
        this._getSenkouXByIndex = function(ichimoku) { return config.xByIndex(parseInt(ichimoku.i) + futureSenkouPeriods) + config.columnWidth / 2 };

        this._getYOfTenkan = function(ichimoku) { return yScale(ichimoku.t) };
        this._getYOfKijun = function(ichimoku) { return yScale(ichimoku.k); };
        this._getYOfChikou = function(ichimoku) { return yScale(ichimoku.c); };
        this._getYOfSenkouA = function(ichimoku) { return yScale(ichimoku.sa) };
        this._getYOfSenkouB = function(ichimoku) { return yScale(ichimoku.sb) };
        this._getKumoPath = function(ichimoku) {
            var x = config.xByIndex(parseInt(ichimoku.i) + futureSenkouPeriods) + config.columnWidth / 2;
            var y1 = yScale(ichimoku.sa);
            var y2 = yScale(ichimoku.sb);
            return "M" + x + "," + y1 + "V" + y2;
        };

        this._ichimokuTenkanStack = new IchimokuMeanStack(9);
        this._ichimokuKijunStack = new IchimokuMeanStack(26);
        this._ichimokuSenkouStack = new IchimokuMeanStack(52);

        var yScale = this.yScale;
        this.tenkanLine = d3.line()
            .x(this._getXByIndex)
            .y(this._getYOfTenkan);
        this.kijunLine = d3.line()
            .x(this._getXByIndex)
            .y(this._getYOfKijun);
        this.chikouLine = d3.line()
            .x(this._getChikouXByIndex)
            .y(this._getYOfChikou);
        this.senkouALine = d3.line()
            .x(this._getSenkouXByIndex)
            .y(this._getYOfSenkouA);
        this.senkouBLine = d3.line()
            .x(this._getSenkouXByIndex)
            .y(this._getYOfSenkouB);

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

        var values = this.getViewportValues();
        this._printTenkan(values);
        this._printKijun(values);
        this._printChikou(values);
        this._printSenkouA(values);
        this._printSenkouB(values);
        this._printKumo(values);
    };

    IchimokuChart.prototype._printTenkan = function(values) {
        var data = values.map(_getTenkanFilter).filter(_removeEmptyFilter);
        this.tenkan.datum(data)
            .attr("d", this.tenkanLine);
    };

    IchimokuChart.prototype._printKijun = function(values) {
        var data = values.map(_getKijunFilter).filter(_removeEmptyFilter);
        this.kijun.datum(data)
            .attr("d", this.kijunLine);
    };

    IchimokuChart.prototype._printChikou = function(values) {
        var data = values.map(_getChikouFilter).filter(_removeEmptyFilter);
        this.chikou.datum(data)
            .attr("d", this.chikouLine);
    };

    IchimokuChart.prototype._printSenkouA = function(values) {
        var data = values.map(_getSenkouAFilter).filter(_removeEmptyFilter);
        this.senkouA.datum(data)
            .attr("d", this.senkouALine);
    };

    IchimokuChart.prototype._printSenkouB = function(values) {
        var data = values.map(_getSenkouBFilter).filter(_removeEmptyFilter);
        this.senkouB.datum(data)
            .attr("d", this.senkouBLine);
    };

    IchimokuChart.prototype._printKumo = function(values) {
        var data = values.map(_getSenkouBFilter)
            .filter(_removeEmptyFilter);
        var line = this.kumo.selectAll("path")
            .data(data);
        line.enter()
            .append("path")
            .classed("up", _isSenkouAAboveB)
            .attr("d", this._getKumoPath);
        line.merge(line)
            .classed("up", _isSenkouAAboveB)
            .attr("d", this._getKumoPath);
        line.exit()
            .remove();
    };

    IchimokuChart.prototype.query = function(query, priceData, successCallback) {
        var ichimokuData = [];

        // Reset the stacks and prepare to calculate new values
        this._ichimokuTenkanStack.clear();
        this._ichimokuKijunStack.clear();
        this._ichimokuSenkouStack.clear();

        for (var i in priceData.values) {
            var price = priceData.values[i];

            this._ichimokuTenkanStack.addPrice(price);
            this._ichimokuKijunStack.addPrice(price);
            this._ichimokuSenkouStack.addPrice(price);

            // Create value object containing index and value
            var ichimoku = {};
            ichimoku.i = i;
            ichimoku.c = price.c; // Chikou is closing price
            ichimoku.t = this._ichimokuTenkanStack.getValue(); // Tenkan
            ichimoku.k = this._ichimokuKijunStack.getValue(); // Kijun
            ichimoku.sa = (ichimoku.t + ichimoku.k) / 2; // Senkou Span A
            ichimoku.sb = this._ichimokuSenkouStack.getValue(); // Senkou Span B

            ichimokuData.push(ichimoku);
        }

        this.data = ichimokuData;

        if (successCallback) {
            successCallback(this);
        }
    };

    /**
     * Override from AbstractChart
     * Returns the ichimoku's lowest
     * Used as callback function when finding the lowest value in an array of ichimoku values.
     */
    IchimokuChart.prototype.getLowestDomainValue = function(ichimoku) {
        return d3.min([ichimoku.t, ichimoku.k, ichimoku.c, ichimoku.sa, ichimoku.sb], _removeEmptyFilter);
    };

    /**
     * Override from AbstractChart
     * Returns the ichimoku's highest value.
     * Used as callback function when finding the highest value in an array of ichimoku values.
     */
    IchimokuChart.prototype.getHighestDomainValue = function(ichimoku) {
        return d3.max([ichimoku.t, ichimoku.k, ichimoku.c, ichimoku.sa, ichimoku.sb], _removeEmptyFilter);
    };

    IchimokuChart.prototype.getViewportValues = function(numOfBarsPadding) {
        var viewportIndexRange = this.config.viewportIndexRange;
        numOfBarsPadding = numOfBarsPadding || 26;
        var fromIndex = viewportIndexRange[0] - numOfBarsPadding;
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        var toIndex = viewportIndexRange[1] + numOfBarsPadding;
        return this.data.slice(fromIndex, toIndex);
    };

    return IchimokuChart;
});
