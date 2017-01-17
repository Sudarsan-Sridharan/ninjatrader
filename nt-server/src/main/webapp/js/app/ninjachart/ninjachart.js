define(["require",
    "d3",
    "./config",
    "./chart/candlechart",
    "./chart/emachart",
    "./chart/ichimokuchart",
    "./chart/rsichart",
    "./chart/simulationchart",
    "./chart/smachart",
    "./component/chartmeta",
    "./component/chartpanel",
    "./component/datexaxis",
    "./component/stockmeta",
    "./layout/defaultlayout",
    "./model/query",
    "./util/date"], function (require, d3) {

    var Config = require("./config");
    var CandleChart = require("./chart/candlechart");
    var IchimokuChart = require("./chart/ichimokuchart");
    var RSIChart = require("./chart/rsichart");
    var SimulationChart = require("./chart/simulationchart");
    var SMAChart = require("./chart/smachart");
    var EMAChart = require("./chart/emachart");
    var ChartMeta = require("./component/chartmeta");
    var ChartPanel = require("./component/chartpanel");
    var DateXAxis = require("./component/datexaxis");
    var StockMeta = require("./component/stockmeta");
    var DefaultLayout = require("./layout/defaultlayout");

    function NinjaChart(divId) {
        this.container = d3.select("#" + divId);
        this.ajaxUrl = "/price";
        this.config = new Config(this.container);
        this.layout = new DefaultLayout(this.config, this.container);
        this.chartContainer = this.layout.chartContainer;
        this.xAxis = new DateXAxis(this.config, this.layout.xAxis);
        this.components = [this.layout, this.xAxis];
        this.chartPanels = [];
        this.mainChartPanel = new ChartPanel(this.config, this.layout.createPanel());
        this.mainChartPanel
            .addChart(new CandleChart(this.config, this.mainChartPanel))
            // .addChart(new IchimokuChart(this.config, this.mainChartPanel))
            // .addChart(new SMAChart(this.config, this.mainChartPanel))
            .addChart(new EMAChart(this.config, this.mainChartPanel))
            .addChart(new SimulationChart(this.config, this.mainChartPanel).setReportId("SAMPLE_REPORT"))
            .addMeta(new StockMeta(this.config));
        this.addPanel(this.mainChartPanel);

        // var newPanel = new ChartPanel(this.config, this.layout.createPanel());
        // this.addPanel(newPanel
        //     .setHeight(100)
        //     .addChart(new RSIChart(this.config, newPanel))
        //     .addMeta(new ChartMeta(this.config))
        // );

        this.allComponents = this.chartPanels.concat(this.components);

        this._initMouseEvent();
        this._initScrollEvent();
        this._initDragEvent();
    }

    NinjaChart.prototype.show = function(query) {
        var self = this;
        var config = this.config;
        var ajaxUrl = config.contextPath + this.ajaxUrl + query.restUrl();
        d3.json(ajaxUrl)
            .timeout(5000)
            .on("error", function(error) { console.log(error) })
            .on("load", function(data) {
                self.onDataLoad(query, data);
            }).get();
        return this;
    };

    /**
     * Called once data is loaded from ajax request.
     */
    NinjaChart.prototype.onDataLoad = function(query, data) {
        this.query = query;
        this.data = data;

        if (data && data.values && data.values.length) {
            this.config.recalibrate(data);
            this.allComponents.forEach(function (component) {
                component.show(data, query);
            });
            this.scrollTo(this.config.chartWidth);
        }
    };

    /**
     * Scrolls the chart to the right-most.
     */
    NinjaChart.prototype.scrollTo = function(pixels) {
        this.layout.scrollable.property("scrollLeft", pixels);
    };

    /**
     * Adds new panel to the canvas.
     * All existing panels will have their height reduced to make room for the new panel.
     */
    NinjaChart.prototype.addPanel = function(chartPanel) {
        var newPanelHeight = chartPanel.getHeight();
        var pcntHeightToReduce = 1 - newPanelHeight / this.config.chartHeight;

        this.chartPanels.forEach(function(panel) {
            var adjustedPanelHeight = panel.getHeight() * pcntHeightToReduce;
            panel.setHeight(adjustedPanelHeight);
        });
        this.chartPanels.push(chartPanel);
    };

    NinjaChart.prototype._initMouseEvent = function() {
        var ninjaChart = this;

        this.layout.table.on("mousemove", function() {
            var coordinates = d3.mouse(this);
            ninjaChart.allComponents.forEach(function(component) {
                if (component.onMouseMove) {
                    component.onMouseMove(coordinates);
                }
            });
        });
    };

    NinjaChart.prototype._initScrollEvent = function() {
        var self = this;
        this.layout.scrollable.on("scroll", function() {
            self._updateViewport();
            self.chartPanels.forEach(function(panel) {
                if (panel && panel.onScroll) {
                    panel.onScroll();
                }
            });
        });
    };

    NinjaChart.prototype._initDragEvent = function() {
        var self = this;
        var config = this.config;
        var startX;
        var scrollPos;
        this.layout.scrollable.call(d3.drag()
            .on("start", function() {
                startX = d3.event.x;
                scrollPos = this.scrollLeft; })
            .on("drag", function() {
                this.scrollLeft = scrollPos + startX - d3.event.x;
            }));

        this.layout.scrollable.call(d3.zoom()
            .on("zoom", function() {
                var source = this;
                var sourceEvent = d3.event.sourceEvent;

                var dx = Math.abs(sourceEvent.deltaX);
                var dy = Math.abs(sourceEvent.deltaY);

                if (dx > dy) {
                    this.scrollLeft += sourceEvent.deltaX;
                } else if (dy > dx) {
                    var currentIndex = self.config.viewportIndexRange[1]
                    currentIndex = Math.min(self.config.dates.length, currentIndex);

                    config.recalibrateOnZoom(sourceEvent.deltaY);
                    self._updateViewport();

                    self.allComponents.forEach(function (component) {
                        if (component.onZoom) {
                            component.onZoom(source);
                        }
                    });

                    self.scrollTo(self.config.xByIndex(currentIndex)
                        - self.container.node().getBoundingClientRect().width
                        + self.config.yAxisWidth);
                }
            }));
    };

    /**
     * Update viewport by getting values currently visible on screen.
     */
    NinjaChart.prototype._updateViewport = function() {
        var scrollable = this.layout.scrollable.node();
        var displayFrom = scrollable.scrollLeft;
        var displayTo = scrollable.scrollLeft
            + this.container.node().getBoundingClientRect().right
            - this.config.yAxisWidth;
        this.config.updateViewport(displayFrom, displayTo);
    };

    return NinjaChart;
});
