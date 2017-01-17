define(["d3"], function(d3) {

    function ChartMeta() {
        this.metas = [];
        this.main = d3.select(document.createElementNS(d3.namespaces.xhtml, "div"))
            .classed("chartMeta", true);
    };

    ChartMeta.prototype.show = function(priceData, query) {
        this.metas.forEach(function(meta) {
            meta.show(priceData, query);
        });
        return this;
    };

    ChartMeta.prototype.getNode = function() {
        return this.main.node();
    };

    ChartMeta.prototype.addMeta = function(meta) {
        this.metas.push(meta);
        this.main.append(function() { return meta.getNode() });
        return this;
    };

    ChartMeta.prototype.onMouseMove = function(coords) {
        this.metas.forEach(function(meta) {
            if (meta.onMouseMove) {
                meta.onMouseMove(coords);
            }
        });
    }

    return ChartMeta;
});