define(["d3", "../util/format"], function(d3, Format) {
    function SimulationMeta(config) {
        this.config = config;
        this._data = null;
        this.main = d3.select(document.createElementNS(d3.namespaces.xhtml, "div"))
            .classed("simulationMeta", true);

        this.tnxTypeLabel = this.main.append("span").classed("tnxType", true);
        this.tnxMeta = this.main.append("span").classed("tnxMeta", true);

        this.price = this.addField(this.tnxMeta, "price:", "price");
        this.shares = this.addField(this.tnxMeta, "shares:", "shares");
        this.value = this.addField(this.tnxMeta, "value:", "value");
        this.profitPcnt = this.addField(this.tnxMeta, "profit:", "profitPcnt");
    }

    SimulationMeta.prototype.addField = function(selection, label, name) {
        var field = selection.append("span").classed("metaField", true);
        field.append("span").classed("metaLabel", true).html(label);
        field.append("span").classed("metaValue", true).classed(name, true);
        field.value = function(value) {
            this.select(".metaValue").html(value);
        };
        field.show = function() {
            this.style("display", "inline");
        };
        field.hide = function() {
            this.style("display", "none");
        };
        return field;
    };

    SimulationMeta.prototype.show = function(priceData, query) {
        // Nothing to do.
    };

    SimulationMeta.prototype.getNode = function() {
        return this.main.node();
    };

    SimulationMeta.prototype.showMeta = function(tnx) {
        this.tnxTypeLabel.html(tnx.tnxType);
        this.price.value(Format.price(tnx.price));
        this.shares.value(tnx.shares);
        this.value.value(Format.price(tnx.value));

        // Sell transactions have profit, show only if it's available.
        if (tnx.profitPcnt)  {
            var profitPcnt = Math.round(tnx.profitPcnt * 10000) / 100; // convert from 1.0 to 100%
            this.profitPcnt.show();
            this.profitPcnt.value(profitPcnt + "%");
        } else {
            this.profitPcnt.hide();
        }

        this.main.style("display", "block")
            .classed("buy", tnx.tnxType == "BUY")
            .classed("sell", tnx.tnxType == "SELL");
    };

    SimulationMeta.prototype.onMouseMove = function(coords) {
        if (this._data == null) return;

        // Get bar index from mouse coordinates
        var index = Math.floor(this.config.xByIndex.invert(coords[0]));

        // Update only if index has changed.
        if (this._currentIndex != index)  {
            this._currentIndex = index;

            // Get price based on index
            var price = this.config.priceData.values[index];
            if (!price) return;

            // Get transaction data from price's date.
            if (this._dataByDateMap[price.d] != null) {
                this.showMeta(this._dataByDateMap[price.d]);
            } else {
                this.hideMeta();
            }
        }
    };

    SimulationMeta.prototype.hideMeta = function() {
        this.main.style("display", "none");
    };

    SimulationMeta.prototype.setData = function(data) {
        this._data = data;

        // Build data map by date
        this._dataByDateMap = [];
        for (var i in data.txns) {
            var tnx = data.txns[i];
            this._dataByDateMap[tnx.dt] = tnx;
        }
    }

    return SimulationMeta;
});
