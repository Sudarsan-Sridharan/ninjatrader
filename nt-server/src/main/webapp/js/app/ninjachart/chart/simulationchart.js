define(["d3", "require", "./abstractchart", "../component/simulationmeta"], function(d3, require) {

    var AbstractChart = require("./abstractchart");
    var SimulationMeta = require("../component/simulationmeta");

    function SimulationChart(config, panel) {
        AbstractChart.call(this, config, panel, "/simulation/report", "simulation");

        this._getClass = function(tnx) { return tnx.type };
        this._getPath = function(tnx) {
            var x = config.xByDate(tnx.dt) + config.columnWidth / 2;
            var y = panel.yScale(tnx.price);
            var vLine = "M" + x + ",0V" + config.chartHeight;
            var hLine = "M" + (x - config.columnWidth/2) + "," + y + "h" + (config.columnWidth);
            return vLine + hLine;
        };
        this._meta = new SimulationMeta(config);
        panel.meta.addMeta(this._meta);
    }

    SimulationChart.prototype = Object.create(AbstractChart.prototype);
    SimulationChart.prototype.constructor = SimulationChart;

    SimulationChart.prototype.show = function() {
        if (!this.data) return;
        this.main.style("visibility", "visible");

        var transactions = this.getViewportValues();
        this._printBuySell(transactions);
    };

    SimulationChart.prototype.getFullAjaxUrl = function(query) {
        return this.config.contextPath + this.ajaxUrl + "/" + this.reportId;
    };

    SimulationChart.prototype._printBuySell = function(transactions) {
        if (!transactions && transactions.length <= 0) return;
        var transaction = this.getMain().selectAll("path")
            .data(transactions);

        transaction.enter()
            .append("path")
            .attr("d", this._getPath)
            .attr("class", this._getClass)

        transaction.merge(transaction)
            .attr("d", this._getPath)
            .attr("class", this._getClass)

        transaction.exit()
            .remove();
    };

    SimulationChart.prototype.getViewportValues = function() {
        var viewportIndexRange = this.config.viewportIndexRange; // array of [from, to]
        var transactions = this.data.transactions;
        var fromIndex = 0;
        var toIndex = 0;
        for (var i in transactions) {
            // Closest visible index from
            if (!fromIndex && transactions[i].index >= viewportIndexRange[0]) {
                fromIndex = i;
            }
            // Closest visible index to
            if (transactions[i].index <= viewportIndexRange[1]) {
                toIndex = i;
            } else {
                break;
            }
        }
        return transactions.slice(fromIndex, transactions.length);
    };

    SimulationChart.prototype.onDataLoad = function(data) {
        this._meta.setData(data);
    };

    SimulationChart.prototype.getDataDomain = function() {
        return [0,0];
    };

    SimulationChart.prototype.setReportId = function(reportId) {
        this.reportId = reportId;
        return this;
    };

    return SimulationChart;
});
