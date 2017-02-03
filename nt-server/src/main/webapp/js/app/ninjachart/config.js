
define(["d3", "require", "./util/date", "./util/color"], function(d3, require) {

    var Color = require("./util/color");

    var _minColumnWidth = 2;
    var _maxColumnWidth = 40;

    function Config(container) {
        this.container = container;
        this.contextPath = "http://localhost:8080";
        this.chartWidth = 2000;
        this.chartHeight = 500;
        this.columnWidth = 8;
        this.readOnly = false;
        this.numOfFutureBars = 40;
        this.futureBars = [];
        this.xAxisHeight = 35;
        this.yAxisWidth = 50;
        this.priceFormat = d3.format(".4");
        this.xByIndex = d3.scaleLinear();
        this.onChartWidthChange();
        this.color = new Color();
    }

    Config.prototype.recalibrate = function(priceData) {
        this.priceData = priceData;
        this.recalculateChartWidth();

        this.dates = priceData.values.map(this._getPriceDate);
        this.futureBars = [];
        var lastDate = Date.parseDbFormat(this.dates[this.dates.length-1]);

        for (var i = 0; i < this.numOfFutureBars; i++) {
            lastDate = Date.nextWeekDay(lastDate);
            this.dates.push(lastDate.toDbFormat());
            this.futureBars.push({d:lastDate.toDbFormat()});
        }

        this.xAxisTickData = this.createXAxisTickData(this.dates);
        this.xByIndex.domain([0, priceData.values.length + this.numOfFutureBars]);

        var pixelsFrom = this.chartWidth - this.container.node().getBoundingClientRect().width - this.yAxisWidth;
        var pixelsTo = this.chartWidth;

        this.updateViewport(pixelsFrom, pixelsTo);

        this.dateIndexMap = [];
        for (var i in this.dates) {
            this.dateIndexMap[this.dates[i]] = i;
        }
    };

    Config.prototype.recalibrateOnZoom = function(delta) {
        this.columnWidth += delta * 0.01;
        if (this.columnWidth < _minColumnWidth) {
            this.columnWidth = _minColumnWidth;
        }
        if (this.columnWidth > _maxColumnWidth) {
            this.columnWidth = _maxColumnWidth;
        }
        this.recalculateChartWidth();
        this.onChartWidthChange();
    }

    Config.prototype.xByDate = function(date) {
        var index = this.dateIndexMap[date];
        return this.xByIndex(index);
    };

    Config.prototype.indexByDate = function(date) {
        return this.dateIndexMap[date];
    };

    Config.prototype.updateViewport = function(pixelsFrom, pixelsTo) {
        var indexFrom = Math.floor(this.xByIndex.invert(pixelsFrom));
        var indexTo = Math.floor(this.xByIndex.invert(pixelsTo));
        this.viewportIndexRange = [indexFrom, indexTo];
        this.viewportDates = this.dates.slice(indexFrom, indexTo);
    }

    Config.prototype.onChartWidthChange = function() {
        this.xByIndex.range([0, this.chartWidth]);
    };

    Config.prototype.createXAxisTickData = function(dates) {
        var ticks = [];
        var lastMonth = 0;
        var lastYear = 0;

        for (var i in dates) {
            var date = Date.parseDbFormat(dates[i]);
            var month = date.getMonth()+1;
            var year = date.getFullYear();

            if (lastMonth != month || lastYear != year) {
                if (lastMonth) { // Ignore first date. Say it's Sept 30, we'll wait to print Oct 1 instead.
                    ticks.push({d:dates[i], month:month, year:year});
                }
                lastMonth = month;
                lastYear = year;
            }
        }
        return ticks;
    };

    Config.prototype.recalculateChartWidth = function() {
        this.chartWidth = this.columnWidth * (this.priceData.values.length + this.numOfFutureBars);
        this.onChartWidthChange();
    };

    Config.prototype._getPriceDate = function(price) {
        return price.d;
    };

    return Config;
});
