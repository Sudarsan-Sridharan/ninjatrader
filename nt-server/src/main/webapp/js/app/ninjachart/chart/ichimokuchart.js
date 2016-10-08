define(function() {
    function Grid(config, selection, xAxis, yAxis) {
        this.config = config;
        this.main = selection.append("g").classed("grid", true);
        this.vertical = this.main.append("g").classed("vertical", true);
        this.horizontal = this.main.append("g").classed("horizontal", true);
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.verticalX = function(date) { return config.xByDate(date.d) + config.columnWidth / 2 };
        this.horizontalY = function(price) { return config.yByPrice(price) };
    }

    Grid.prototype.show = function(priceData, query) {
        this.printVerticalLines();
        this.printHorizontalLines();
    };

    Grid.prototype.printVerticalLines = function() {
        var config = this.config;
        var xTicks = [];
        this.xAxis.getTicks().selectAll(".tick").each(function(date) { xTicks.push(date) });

        var verticalLine = this.vertical.selectAll("line")
            .data(xTicks);

        verticalLine.enter()
            .append("line")
            .attr("x1", this.verticalX)
            .attr("x2", this.verticalX)
            .attr("y2", config.chartHeight);
        verticalLine.merge(verticalLine)
            .attr("y2", config.chartHeight)
            .transition().duration(this.config.transitionDuration)
            .attr("x1", this.verticalX)
            .attr("x2", this.verticalX);
        verticalLine.exit().remove();
    };

    Grid.prototype.printHorizontalLines = function() {
        var config = this.config;
        var yTicks = [];
        this.yAxis.getTicks().selectAll(".tick").each(function(price) { yTicks.push(price) });

        var horizontalLine = this.horizontal.selectAll("line")
            .data(yTicks);
        horizontalLine.enter()
            .append("line")
            .attr("x2", config.chartWidth)
            .attr("y1", this.horizontalY)
            .attr("y2", this.horizontalY);
        horizontalLine.merge(horizontalLine)
            .attr("x2", config.chartWidth)
            .transition().duration(this.config.transitionDuration)
            .attr("y1", this.horizontalY)
            .attr("y2", this.horizontalY);
        horizontalLine.exit()
            .transition().duration(this.config.transitionDuration)
            .attr("y1", -10)
            .attr("y2", -10)
            .remove();
    };

    return Grid;
});