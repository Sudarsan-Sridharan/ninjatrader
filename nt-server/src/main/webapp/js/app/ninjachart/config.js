
define(['require',
    'd3',
    './chart/candlechart',
    './chart/ichimokuchart',
    './component/column',
    './component/datecursor',
    './component/datexaxis',
    './component/grid',
    './component/pricecursor',
    './component/priceyaxis',
    './component/stockmeta',
    './layout/defaultlayout',
    './util/date'],
    function (require, d3) {

    var CandleChart = require('./chart/candlechart');
    var IchimokuChart = require('./chart/ichimokuchart');
    var ColumnChart = require('./component/column');
    var DateCursor = require('./component/datecursor');
    var DateXAxis = require('./component/datexaxis');
    var Grid = require('./component/grid');
    var PriceCursor = require('./component/pricecursor');
    var PriceYAxis = require('./component/priceyaxis');
    var StockMeta = require('./component/stockmeta');
    var DefaultLayout = require('./layout/defaultlayout');
        

    function NinjaChart(divId) {
        this.config = new Config();
        this.container = d3.select("#" + divId);
        this.layout = new DefaultLayout(this.config, this.container);
        this.chart = this.layout.chart;
        this.charts = [];
        this.xAxis = new DateXAxis(this.config, this.layout.chart);
        this.yAxis = new PriceYAxis(this.config, this.layout.yAxis);
        this.grid = new Grid(this.config, this.layout.chart, this.xAxis, this.yAxis);
        this.stockMeta = new StockMeta(this.config, this.layout.meta);
        this.dateCursor = new DateCursor(this.config, this.chart);
        this.priceCursor = new PriceCursor(this.config, this.chart);
        this.listeners = [this.priceCursor, this.yAxis];
        this.columns = new ColumnChart(this.config, this.chart)
            .addListener(this.dateCursor)
            .addListener(this.stockMeta)
            .addListener(this.xAxis);
        this.components = [this.xAxis, this.yAxis, this.stockMeta, this.dateCursor, this.columns, this.grid];

        this.addChart(new CandleChart(this.config, this.chart));
        this.addChart(new IchimokuChart(this.config, this.chart));

        var ninjaChart = this;
        var config = this.config;
        this.chart.on("mousemove", function(e) {
            var coords = d3.mouse(this);
            if (coords[1] > config.chartHeight) return;
            for (var i in ninjaChart.listeners) {
                if (ninjaChart.listeners[i].mousemove) {
                    ninjaChart.listeners[i].mousemove(coords)
                }
            }
        });

        var query = { symbol:"MEG" };

        this.show(query);

        // priceData.prices[0].l = 2;
        // priceData.prices.splice(1, 11);

        query.symbol = "BDO";
        var that = this;
        var xx = function() {that.show(query)};

        setTimeout(xx, 1000);
    };

    NinjaChart.prototype.addChart = function(chart) {
        this.charts.push(chart);
        return this;
    };

    NinjaChart.prototype.show = function(query) {

        //TODO Sample Test Data
        var responseData = priceData;
        if (query.symbol == "BDO") {
            responseData = priceData2;
        }

        this.config.update(responseData);
        this.layout.chartInner.property("scrollLeft", this.config.chartWidth);

        for (var i in this.components) {
            if (this.components[i].show) {
                this.components[i].show(responseData, query);
            }
        }

        for (var i in this.charts) {
            if (this.charts[i].show) {
                this.charts[i].show(responseData, query);
            }
        }
        return this;
    };

    return NinjaChart;

});
