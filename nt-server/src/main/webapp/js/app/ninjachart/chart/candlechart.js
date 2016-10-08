define(function() {
    function IchimokuChart(config, selection) {
        this.config = config;
        this.main = selection.append("g").classed("ichimoku", true);
        this.removeEmptyFilter = function(e) {return e};
        this.x = function(ichimoku) { return config.xByDate(ichimoku.d) + config.columnWidth / 2 };

        var x = this.x;
        var lineX = function(ichimoku) { return x(ichimoku); };

        this.tenkanLine = d3.line()
            .x(lineX)
            .y(function(ichimoku) { return config.yByPrice(ichimoku.t); });
        this.kijunLine = d3.line()
            .x(lineX)
            .y(function(ichimoku) { return config.yByPrice(ichimoku.k); });
        this.chikouLine = d3.line()
            .x(lineX)
            .y(function(ichimoku) { return config.yByPrice(ichimoku.c); });
        this.senkouALine = d3.line()
            .x(lineX)
            .y(function(ichimoku) { return config.yByPrice(ichimoku.sa); });
        this.senkouBLine = d3.line()
            .x(lineX)
            .y(function(ichimoku) { return config.yByPrice(ichimoku.sb); });

        this.tenkan = this.main.append("path").classed("tenkan", true);
        this.kijun = this.main.append("path").classed("kijun", true);
        this.chikou = this.main.append("path").classed("chikou", true);
        this.senkouA = this.main.append("path").classed("senkouA", true);
        this.senkouB = this.main.append("path").classed("senkouB", true);
        this.kumo = this.main.append("g").classed("kumo", true);
    }

    IchimokuChart.prototype.show = function(priceData, query) {

        //TODO Sample Ichimoku Data
        var responseData = ichimokuMeg;
        if(query.symbol == "BDO") {
            responseData = ichimokuBdo;
        }

        this.printTenkan(responseData);
        this.printKijun(responseData);
        this.printChikou(responseData);
        this.printSenkouA(responseData);
        this.printSenkouB(responseData);
        this.printKumo(responseData);
    };

    IchimokuChart.prototype.printTenkan = function(ichimokuData) {
        var data = ichimokuData.map(function(ichimoku) { if (ichimoku.t != 0) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.tenkan.datum(data)
            .transition().duration(this.config.transitionDuration)
            .attr("d", this.tenkanLine);
    };

    IchimokuChart.prototype.printKijun = function(ichimokuData) {
        var data = ichimokuData.map(function(ichimoku) { if (ichimoku.k) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.kijun.datum(data)
            .transition().duration(this.config.transitionDuration)
            .attr("d", this.kijunLine);
    };

    IchimokuChart.prototype.printChikou = function(ichimokuData) {
        var data = ichimokuData.map(function(ichimoku) { if (ichimoku.c) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.chikou.datum(data)
            .transition().duration(this.config.transitionDuration)
            .attr("d", this.chikouLine);
    };

    IchimokuChart.prototype.printSenkouA = function(ichimokuData) {
        var data = ichimokuData.map(function(ichimoku) { if (ichimoku.sa) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.senkouA.datum(data)
            .transition().duration(this.config.transitionDuration)
            .attr("d", this.senkouALine);
    };

    IchimokuChart.prototype.printSenkouB = function(ichimokuData) {
        var data = ichimokuData.map(function(ichimoku) { if (ichimoku.sb) return ichimoku; })
            .filter(this.removeEmptyFilter);
        this.senkouB.datum(data)
            .transition().duration(this.config.transitionDuration)
            .attr("d", this.senkouBLine);
    };

    IchimokuChart.prototype.printKumo = function(ichimokuData) {
        var config = this.config;
        var data = ichimokuData.map(function(ichimoku) { if (ichimoku.sb) return ichimoku; })
            .filter(this.removeEmptyFilter);

        var line = this.kumo.selectAll("line")
            .data(data);
        line.enter()
            .append("line")
            .classed("up", function(ichimoku) {return ichimoku.sa > ichimoku.sb})
            .attr("x1", this.x)
            .attr("x2", this.x)
            .attr("y1", function(ichimoku) { return config.yByPrice(ichimoku.sa) })
            .attr("y2", function(ichimoku) { return config.yByPrice(ichimoku.sb) });
        line.merge(line)
            .classed("up", function(ichimoku) {return ichimoku.sa > ichimoku.sb})
            .transition().duration(this.config.transitionDuration)
            .attr("x1", this.x)
            .attr("x2", this.x)
            .attr("y1", function(ichimoku) { return config.yByPrice(ichimoku.sa) })
            .attr("y2", function(ichimoku) { return config.yByPrice(ichimoku.sb) });
        line.exit().remove();
    };


    return IchimokuChart;
});