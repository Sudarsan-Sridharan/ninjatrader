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

    DateCursor.prototype.mousemove = function(coords) {
        var config = this.config;
        var index = Math.floor(config.xByIndex.invert(coords[0]));
        var x = config.xByIndex(index) + config.columnWidth / 2;
        this.main.attr("transform", "translate(" + x + ",0)");
    };

    return DateCursor;
});