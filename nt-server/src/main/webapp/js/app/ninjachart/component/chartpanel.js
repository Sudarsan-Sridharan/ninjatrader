define([
    "require",
    "d3",
    "./yaxis",
    "./grid",
    "./chartmeta",
    "./datecursor",
    "./pricecursor"], function(require, d3) {

    var YAxis = require('./yaxis');
    var Grid = require('./grid');
    var DateCursor = require('./datecursor');
    var PriceCursor = require('./pricecursor');
    var ChartMeta = require('./chartmeta');

    function ChartPanel(config, panel) {
        this.config = config;
        this.main = panel;
        this.panelHeight = config.chartHeight;
        this.panelWidth = config.chartWidth;
        this.charts = [];
        this.loadedCharts = [];
        this.yScale = d3.scaleLinear();

        this._initLayout();
        this._initComponents();
        this._initEvents();
        this._initFunctions();

        this.setWidth(config.chartWidth);
        this.setHeight(config.chartHeight);
    }

    ChartPanel.prototype._initLayout = function() {
        var config = this.config;
        this.canvas = this.main.select(".canvas");
        this.chartArea = this.main.select(".canvas").append("svg");
        this.metaPanel = this.main.select(".meta");
        this.yAxisPanel = this.main.select(".yAxis").append("svg")
            .attr("width", config.yAxisWidth);
        this.panelSeparator = this.chartArea.append("path").attr("d", "M0,0H" + this.panelWidth)
            .style("stroke", "#000")
            .style("stroke-width", 2);
        this.yAxisSeparator = this.yAxisPanel.append("path").attr("d", "M0,0H" + config.yAxisWidth)
            .style("stroke", "#000")
            .style("stroke-width", 2);
    };

    ChartPanel.prototype._initComponents = function() {
        var config = this.config;

        this.xCursor = new DateCursor(config);
        this.yCursor = new PriceCursor(config);
        this.yAxis = new YAxis(config, this);
        this.meta = new ChartMeta();
        this.grid = new Grid(config, this).setYAxis(this.yAxis);
        this.components = [this.yAxis, this.grid, this.xCursor, this.yCursor, this.meta];
        this.yAxisPanel.append(this._getComponentNode(this.yAxis));
        this.metaPanel.append(this._getComponentNode(this.meta));
        this.chartArea.append(this._getComponentNode(this.grid));
        this.chartArea.append(this._getComponentNode(this.xCursor));
        this.chartArea.append(this._getComponentNode(this.yCursor));
    };

    ChartPanel.prototype._initEvents = function() {
        var listeners = [this.yAxis, this.yCursor];

        this.canvas.on("mousemove", function() {
            var coordinates = d3.mouse(this);
            listeners.forEach(function(listener) {
                listener.onMouseMove(coordinates);
            });
        });
        this.canvas.on("mouseout", function() {
            var coordinates = [-100, -100];
            listeners.forEach(function(listener) {
                listener.onMouseMove(coordinates);
            });
        });
    };

    ChartPanel.prototype._initFunctions = function() {
        var self = this;
        this._redrawLoadedCharts = function() {
            self.loadedCharts.forEach(function(chart) {
                chart.show();
            });
        };

        this._redrawGridAndYAxis = function() {
            self.yAxis.show();
            self.grid.show();
        };
    };

    ChartPanel.prototype.getYScale = function() {
        return this.yScale;
    };

    ChartPanel.prototype.show = function(priceData, query) {
        this.priceData = priceData;
        this.query = query;

        var self = this;
        var config = this.config;

        this.yScale.domain([]);
        this.loadedCharts = [];
        this.setWidth(config.chartWidth);
        this.components.forEach(function(component) { component.show(priceData, query); });
        this.charts.forEach(function(chart) {
            chart.hide().query(query, priceData, self._getChartQuerySuccessCallback());
        });
        return this;
    };

    ChartPanel.prototype.addChart = function(chart) {
        this.charts.push(chart);
        this.chartArea.append(function() { return chart.getNode() });
        return this;
    };

    ChartPanel.prototype.addMeta = function(meta) {
        this.meta.addMeta(meta);
        return this;
    };

    ChartPanel.prototype.setScaleDomain = function(min, max, onScaleChangeCallback) {
        var currMin = this.yScale.domain()[0];
        var currMax = this.yScale.domain()[1];
        this.yScale.domain([min, max]);
        if (onScaleChangeCallback && (currMin != min || currMax != max)) {
            onScaleChangeCallback();
        }
    };

    ChartPanel.prototype.getHeight = function() {
        return this.panelHeight;
    };

    ChartPanel.prototype.setHeight = function(height) {
        this.panelHeight = height;
        this.yScale.range([this.panelHeight, 0]);
        this.chartArea.attr("height", this.panelHeight);
        this.yAxisPanel.attr("height", this.panelHeight);
        return this;
    };

    ChartPanel.prototype.setWidth = function(width) {
        this.panelWidth = width;
        this.chartArea.attr("width", width);
        this.panelSeparator.attr("d", "M0,0H" + width);
        return this;
    };

    ChartPanel.prototype.onMouseMove = function(coordinates) {
        this.xCursor.onMouseMove(coordinates);
        if (this.meta && this.meta.onMouseMove) {
            this.meta.onMouseMove(coordinates);
        }
    };

    ChartPanel.prototype.onScroll = function() {
        this._refreshYScaleDomain();
        this._redrawLoadedCharts();
    };

    ChartPanel.prototype.onZoom = function(source) {
        this.setWidth(this.config.chartWidth);
        this.grid.onZoom();
        this.yCursor.onZoom();
        this.xCursor.onZoom(source);
        // onScroll() is also called.
    };

    /**
     * Returns component's node.
     * Used as callback function to append to a D3 selection.
     */
    ChartPanel.prototype._getComponentNode = function(component) {
        return function() {
            return component.getNode();
        };
    };

    /**
     * Updates the yScale domain values by getting the lowest and highest values from all charts.
     * If scale has changed, it calls function to redraw the grid and yAxis.
     */
    ChartPanel.prototype._refreshYScaleDomain = function() {
        var self = this;
        var onScaleChangeCallback = this._redrawGridAndYAxis;
        var yScaleDomain = [0, 0];

        this.charts.forEach(function(chart) {
            var chartDomain = chart.getDataDomain();
            if (chartDomain) {
                var padding = (chartDomain[1] - chartDomain[0]) * 0.04;
                var lowest = d3.min([chartDomain[0] - padding, yScaleDomain[0]], self._removeZeroArrayFilter);
                var highest = d3.max([chartDomain[1] + padding, yScaleDomain[1]], self._removeZeroArrayFilter);
                yScaleDomain = [lowest, highest];
            }
        });
        this.setScaleDomain(yScaleDomain[0], yScaleDomain[1], onScaleChangeCallback);
    };

    /**
     * This callback function is called when a chart successfully finishes loading
     * and has data retrieved. This panel's yScale domain will then be updated with
     * lowest and highest value of chart's data.
     *
     * If yScale is changed, all charts that have finished rendering will be
     * re-rendered, using the latest yScale.
     */
    ChartPanel.prototype._getChartQuerySuccessCallback = function() {
        var self = this;
        var onScaleChangeCallback = this._redrawLoadedCharts;

        return function(chart) {
            var range = self._getChartLowHighRange(chart);
            if (range) {
                self.setScaleDomain(range[0], range[1], onScaleChangeCallback);
            }
            chart.show();
            self.components.forEach(function(component) { component.show(self.priceData, self.query); });
            self.loadedCharts.push(chart);
        };
    };

    /**
     * Get lowest and highest value of a chart.
     * This will be used to adjust the yScale so everything fits on the canvas.
     */
    ChartPanel.prototype._getChartLowHighRange = function(chart) {
        if (!chart.getDataDomain) return;
        var chartDomain = chart.getDataDomain();
        var padding = (chartDomain[1] - chartDomain[0]) * 0.04;
        var lowest = d3.min([chartDomain[0] - padding, this.yScale.domain()[0]], this._removeZeroArrayFilter);
        var highest = d3.max([chartDomain[1] + padding, this.yScale.domain()[1]], this._removeZeroArrayFilter);
        return [lowest, highest];
    };

    /**
     * This filter can be applied to array via [].filter() to remove all zeros
     * or empty values.
     */
    ChartPanel.prototype._removeZeroArrayFilter = function(val) {
        if (val) return val;
    };

    return ChartPanel;
});