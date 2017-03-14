define(["d3", "require", "./abstractchart"], function(d3, require, AbstractChart) {

    function VolumeChart(config, panel) {
        AbstractChart.call(this, config, panel, null, "volume");

        this.bars = this.getMain().append("g").classed("vBars", true);
        this.yScale = d3.scaleLinear().range([panel.getHeight() , panel.getHeight() * 0.75]); // Volume chart height is 1/4 of panel height at bottom of screen.

        var that = this;
        this._getBarPath = function(price) {
            var x = that.config.xByDate(price.d);
            var barMargin = config.columnWidth * 0.1
            var barX = x + barMargin;
            var y = that.yScale(price.v);

            return "M" + barX + "," + panel.getHeight()
                + "V" + y
                + "h" + (config.columnWidth - (barMargin * 2))
                + "V" + panel.getHeight()
                + "Z";
        }
    }

    VolumeChart.prototype = Object.create(AbstractChart.prototype);
    VolumeChart.prototype.constructor = VolumeChart;

    VolumeChart.prototype.show = function() {
        if (!this.data) return;
        this.main.style("visibility", "visible");
        var values = this.getViewportValues();
        this._printBars(values);
        return this;
    };

    VolumeChart.prototype.query = function(query, priceData, successCallback) {
        this.data = priceData;
        successCallback(this);
    };

    VolumeChart.prototype.getDataDomain = function() {
        if (!this.data) return null;
        var viewportValues = this.getViewportValues();
        var viewportMin = d3.min(viewportValues, this.getLowestDomainValue);
        var viewportMax = d3.max(viewportValues, this.getHighestDomainValue);
        this.yScale = this.yScale.domain([viewportMin, viewportMax]);
        return []; // Don't return value to chartPanel. Keep domain to internal yScale.
    };

    VolumeChart.prototype.getLowestDomainValue = function(price) {
        return price.v;
    };

    VolumeChart.prototype.getHighestDomainValue = function(price) {
        return price.v;
    };

    VolumeChart.prototype._printBars = function(prices) {
        var bars = this.bars.selectAll("path").data(prices);
        bars.enter()
            .append("path")
            .attr("d", this._getBarPath);
        bars.merge(bars)
            .attr("d", this._getBarPath)
        bars.exit()
            .remove();
    }

    return VolumeChart;
});
