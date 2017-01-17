define(["d3"], function(d3) {

    function AbstractChart(config, panel, ajaxUrl, chartName) {
        this.config = config;
        this.panel = panel;
        this.yScale = panel.getYScale();
        this.ajaxUrl = ajaxUrl;
        this.chartName = chartName;
        this.main = d3.select(document.createElementNS(d3.namespaces.svg, 'g'))
            .classed(this.chartName, function() {return chartName});
    }

    AbstractChart.prototype.getNode = function() {
        return this.main.node();
    };

    AbstractChart.prototype.getConfig = function() {
        return this.config;
    };

    AbstractChart.prototype.getMain = function() {
        return this.main;
    };

    AbstractChart.prototype.query = function(query, priceData, successCallback) {
        this.data = null;
        var chart = this;
        var fullAjaxUrl = this.getFullAjaxUrl(query);
        d3.json(fullAjaxUrl)
            .on("error", function() {
                console.error("Failed to get data for symbol: " + query.symbol)
            })
            .on("load", function(data) {
                chart.data = data;
                chart.onDataLoad(data);
                if (successCallback) {
                    successCallback(chart);
                }
            })
            .get();
    };

    AbstractChart.prototype.getFullAjaxUrl = function(query) {
      return this.config.contextPath + this.ajaxUrl + query.restUrl(this.chartName);
    };

    /**
     * Called once data is loaded from ajax request.
     */
    AbstractChart.prototype.onDataLoad = function(data) {
    };

    AbstractChart.prototype.hide = function() {
        this.main.style("visibility", "hidden");
        return this;
    };

    AbstractChart.prototype.show = function() {
        if (!this.data) return;
        this.main.style("visibility", "visible");
    };

    AbstractChart.prototype.setAjaxUrl = function(ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    }

    AbstractChart.prototype.getChartName = function() {
        return this.chartName;
    }

    AbstractChart.prototype.setChartName = function(chartName) {
        this.chartName = chartName;
        this.main.classed(chartName, function() { return chartName});
        return this;
    }

    AbstractChart.prototype.getDataDomain = function() {
        if (!this.data) return null;
        var viewportValues = this.getViewportValues();
        var viewportMin = d3.min(viewportValues, this.getLowestDomainValue);
        var viewportMax = d3.max(viewportValues, this.getHighestDomainValue);
        return [viewportMin, viewportMax];
    };

    AbstractChart.prototype.getLowestDomainValue = function() {

    };

    AbstractChart.prototype.getHighestDomainValue = function() {

    };

    /**
     * Returns array of values visible in the viewport.
     */
    AbstractChart.prototype.getViewportValues = function(numOfBarsPadding) {
        // numOfBarsPadding = 0; // TODO REMOVE
        var viewportIndexRange = this.config.viewportIndexRange;
        numOfBarsPadding = numOfBarsPadding || 0;
        var fromIndex = viewportIndexRange[0] - numOfBarsPadding;
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        var toIndex = viewportIndexRange[1] + numOfBarsPadding;
        return this.data.values.slice(fromIndex, toIndex);
    };

    return AbstractChart;
});