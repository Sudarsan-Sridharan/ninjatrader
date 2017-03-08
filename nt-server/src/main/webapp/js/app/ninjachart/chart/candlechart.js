define(["d3", "require", "./abstractchart", "../component/stockmeta"], function(d3, require) {

    var AbstractChart = require("./abstractchart");
    var StockMeta = require("../component/stockmeta");

    function CandleChart(config, panel) {
        AbstractChart.call(this, config, panel, "", "candles");

        this.wicks = this.main.append("g").classed("wicks", true);
        this.bars = this.main.append("g").classed("bars", true);
        this.candleMargin = config.columnWidth * 0.25;
        this.candleWidth = config.columnWidth - this.candleMargin * 2;

        this._meta = new StockMeta(config);
        panel.meta.addMeta(this._meta);

        this._init();
    }

    CandleChart.prototype = Object.create(AbstractChart.prototype);
    CandleChart.prototype.constructor = CandleChart;

    /**
     * Initialize functions.
     */
    CandleChart.prototype._init = function() {
        var self = this;

        this._getCandleStickPath = function(price) {
            var x = self.config.xByDate(price.d);
            var barX = x + self.candleMargin;
            var wickX = x + self.config.columnWidth / 2;
            var openY = self.yScale(price.o);
            var highY = self.yScale(price.h);
            var lowY = self.yScale(price.l);
            var closeY = self.yScale(price.c);

            return "M" + wickX + "," + highY + "V" + Math.min(closeY, openY) // draw top wick
                    + "M" + wickX + "," + lowY + "V" + Math.max(closeY, openY) // draw bottom wick
                    + "M" + barX + "," + closeY + "h" + self.candleWidth + "V" + openY + "h-" + self.candleWidth + "Z"; // draw bar
        };
    }

    CandleChart.prototype.query = function(query, priceData, successCallback) {
        this.data = priceData;
        successCallback(this);
    };

    CandleChart.prototype.show = function() {
        if (!this.data) return;

        this.candleMargin = this.config.columnWidth * 0.25;
        this.candleWidth = this.config.columnWidth - this.candleMargin * 2;

        this.main.style("visibility", "visible");

        var prices = this.getViewportValues();

        this._printCandleSticks(prices);
    };

    CandleChart.prototype._printCandleSticks = function(prices) {
        var bar = this.bars.selectAll("path")
            .data(prices);
        bar.enter()
            .append("path")
            .attr("class", this._getCandleClass)
            .attr("d", this._getCandleStickPath);
        bar.merge(bar)
            .attr("class", this._getCandleClass)
            .attr("d", this._getCandleStickPath)
        bar.exit()
            .remove();
    };

    /**
     * Returns class for the candle depending on whether it closed higher or lower.
     */
    CandleChart.prototype._getCandleClass = function(price) {
        return price.c > price.o ? "up" : "down"
    };

    /**
     * Override from AbstractChart
     * Returns the price's low.
     * Used as callback function when finding the lowest price in an array of prices.
     */
    CandleChart.prototype.getLowestDomainValue = function(price) {
        return price.l;
    };

    /**
     * Override from AbstractChart
     * Returns the price's high.
     * Used as callback function when finding the highest price in an array of prices.
     */
    CandleChart.prototype.getHighestDomainValue = function(price) {
        return price.h;
    };

    return CandleChart;
});