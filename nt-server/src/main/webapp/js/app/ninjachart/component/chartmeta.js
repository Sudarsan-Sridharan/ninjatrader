define(["d3"], function(d3) {
    function StockMeta(config) {
        this.config = config;
        this.main = d3.select(document.createElementNS(d3.namespaces.xhtml, "div"))
            .classed("stockMeta", true);
        this.symbol = this.main.append("span").classed("symbol", true);
        this.priceMeta = this.main.append("span").classed("priceMeta", true);

        this.open = this.addField(this.priceMeta, "O:", "open");
        this.high = this.addField(this.priceMeta, "H:", "high");
        this.low = this.addField(this.priceMeta, "L:", "low");
        this.close = this.addField(this.priceMeta, "C:", "close");
        this.volume = this.addField(this.priceMeta, "V:", "volume");
    };

    StockMeta.prototype.addField = function(selection, label, name) {
        selection.append("span").classed("metaLabel", 1).html(label);
        return selection.append("span")
            .classed("metaValue", true)
            .classed(name, true);
    };

    StockMeta.prototype.show = function(priceData, query) {
        this.symbol.html(query.symbol);
        this.priceData = priceData;
    };

    StockMeta.prototype.getNode = function() {
        return this.main.node();
    };

    StockMeta.prototype.meta = function(price) {
        this.open.html(price.o);
        this.high.html(price.h);
        this.low.html(price.l);
        this.close.html(price.c);
        this.volume.html(price.v);
        this.priceMeta.classed("up", price.c > price.o)
            .classed("down", price.o > price.c);
    };

    StockMeta.prototype.onMouseMove = function(coords) {
        var config = this.config;
        var index = Math.floor(config.xByIndex.invert(coords[0]));
        if (this.currentIndex != index) {
            this.currentIndex = index;
            if (this.priceData && this.priceData.values[index]) {
                this.meta(this.priceData.values[index]);
            }
        }
    };

    return StockMeta;
});