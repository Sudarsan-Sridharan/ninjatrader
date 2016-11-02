define(function() {
    function CandleChart(config, chart) {
        this.config = config;
        this.chart = chart.append("g").classed("candles", true);
        this.wicks = this.chart.append("g").classed("wicks", true);
        this.bars = this.chart.append("g").classed("bars", true);
        this.candleHeight = function(price) {
            var height = Math.abs(config.yByPrice(price.o) - config.yByPrice(price.c));
            return height > 0 ? height : 1;
        };
    }

    CandleChart.prototype.show = function(priceData) {
        var config = this.config;
        this.candleMargin = config.columnWidth * 0.15;
        this.candleWidth = config.columnWidth - this.candleMargin * 2;
        this.printWicks(priceData);
        this.printBars(priceData);
    };

    CandleChart.prototype.printBars = function(priceData) {
        var config = this.config;
        var candleWidth = this.candleWidth;
        var candleHeight = this.candleHeight;
        var candleMargin = this.candleMargin;
        var x = function(price) { return config.xByDate(price.d) + candleMargin };
        var y = function(price) { return config.yByPrice(Math.max(price.c, price.o)) };
        var upClass = function(price) { return price.c > price.o };
        var downClass = function(price) { return price.c < price.o };

        var bar = this.bars.selectAll("rect")
            .data(priceData.prices);
        bar.enter()
            .append("rect")
            .classed("up", upClass)
            .classed("down", downClass)
            .attr("width", candleWidth)
            .attr("height", candleHeight)
            .attr("x", x)
            .attr("y", y);
        bar.merge(bar)
            .classed("up", upClass)
            .classed("down", downClass)
            .transition().duration(config.transitionDuration)
            .attr("width", candleWidth)
            .attr("height", candleHeight)
            .attr("x", x)
            .attr("y", y)
        bar.exit()
            .remove();
    };

    CandleChart.prototype.printWicks = function(priceData) {
        var config = this.config;
        var candleWidth = this.candleWidth;
        var wickX = function(price, index) {
            return config.xByDate(price.d) + (config.columnWidth / 2);
        };
        var wick = this.wicks.selectAll("line")
            .data(priceData.prices);
        wick.enter()
            .append("line")
            .attr("x1", wickX)
            .attr("x2", wickX)
            .attr("y1", function(price) { return config.yByPrice(price.l) })
            .attr("y2", function(price) { return config.yByPrice(price.h) });
        wick.merge(wick)
            .transition().duration(config.transitionDuration)
            .attr("x1", wickX)
            .attr("x2", wickX)
            .attr("y1", function(price) { return config.yByPrice(price.l) })
            .attr("y2", function(price) { return config.yByPrice(price.h) });
        wick.exit()
            .remove();
    };

    return CandleChart;
});