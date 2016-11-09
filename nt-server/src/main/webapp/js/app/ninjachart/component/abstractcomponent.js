define(["d3"], function(d3) {
    function DateCursor(config) {
        this.config = config;
        this.main = d3.select(document.createElementNS(d3.namespaces.svg, 'g'))
            .classed("dateCursor", true);

        this.main.append("line")
            .attr("x1", 0)
            .attr("x2", 0)
            .attr("y1", 0)
            .attr("y2", config.chartHeight);
    }

    DateCursor.prototype.show = function() {
    };

    DateCursor.prototype.onMouseMove = function(coords) {
        if (!coords) return;
        var index = Math.floor(this.config.xByIndex.invert(coords[0]));
        this.moveToIndex(index);
    };

    DateCursor.prototype.onZoom = function(source) {
        console.log(source)
        var coordinates = d3.mouse(source);

        this.moveToIndex(coordinates[0]);
    };

    DateCursor.prototype.moveToIndex = function(index) {
        var x = this.config.xByIndex(index) + this.config.columnWidth / 2;
        this.main.attr("transform", "translate(" + x + ",0)");
    };

    DateCursor.prototype.getNode = function() {
        return this.main.node();
    };
    
    return DateCursor;
});