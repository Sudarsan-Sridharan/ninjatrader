define(function() {
    function DateCursor(config, selection) {
        this.config = config;
        this.main = selection.append("g").classed("dateCursor", true);

        this.main.append("line")
            .attr("x1", 0)
            .attr("x2", 0)
            .attr("y1", 0)
            .attr("y2", config.chartHeight);
    }

    DateCursor.prototype.onColumnMouseOver = function(price) {
        var x = this.config.xByDate(price.d) + this.config.columnWidth / 2;
        this.main.attr("transform", "translate(" + x + ",0)");
    };

    return DateCursor;
});