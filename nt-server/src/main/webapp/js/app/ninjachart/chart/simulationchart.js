define(["d3", "require", "./abstractchart", "../component/simulationmeta"], function(d3, require) {

    var AbstractChart = require("./abstractchart");
    var SimulationMeta = require("../component/simulationmeta");

    function SimulationChart(config, panel) {
        AbstractChart.call(this, config, panel, "/simulation/report", "simulation");

        this.transactionGroup = this.getMain().append("g").classed("transactions", true);
        this.markGroup = this.getMain().append("g").classed("marks", true);

        this._getClass = function(tnx) { return tnx.type };
        this._getTransactionPath = function(tnx) {
            var x = config.xByDate(tnx.dt) + config.columnWidth / 2;
            var y = panel.yScale(tnx.price);
            var vLine = "M" + x + ",0V" + config.chartHeight;
            var hLine = "M" + (x - config.columnWidth/2) + "," + y + "h" + (config.columnWidth);
            return vLine + hLine;
        };
        this._getMarkPath = function(mark) {
            var x = config.xByDate(mark.date) + config.columnWidth / 2;
            return "M" + x + ",0V" + config.chartHeight;
        }
        this._getMarkColor = function(mark) {
            return mark.color;
        }

        this._meta = new SimulationMeta(config);
        panel.meta.addMeta(this._meta);
    }

    SimulationChart.prototype = Object.create(AbstractChart.prototype);
    SimulationChart.prototype.constructor = SimulationChart;

    SimulationChart.prototype.show = function() {
        if (!this.data) return;
        this.main.style("visibility", "visible");

        var transactions = this.getViewportTransactionValues();
        var marks = this.getViewportMarkValues();

        this._printBuySell(transactions);
        this._printMarks(marks);
    };

    SimulationChart.prototype.getFullAjaxUrl = function(query) {
        return this.config.contextPath + this.ajaxUrl + "/" + this.reportId;
    };

    SimulationChart.prototype._printBuySell = function(transactions) {
        if (!transactions || transactions.length <= 0) return;
        var transaction = this.transactionGroup.selectAll("path")
            .data(transactions);

        transaction.enter()
            .append("path")
            .attr("d", this._getTransactionPath)
            .attr("class", this._getClass)

        transaction.merge(transaction)
            .attr("d", this._getTransactionPath)
            .attr("class", this._getClass)

        transaction.exit()
            .remove();
    };

    SimulationChart.prototype._printMarks = function(marks) {
        if (!marks || marks.length <= 0) return;

        var mark = this.markGroup.selectAll("path")
            .data(marks);
        mark.enter()
            .append("path")
            .attr("d", this._getMarkPath)
            .style("stroke", this._getMarkColor);

        mark.merge(mark)
            .attr("d", this._getMarkPath)
            .style("stroke", this._getMarkColor);

        mark.exit()
            .remove();
    };

    SimulationChart.prototype.getViewportTransactionValues = function() {
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
        toIndex++;
        return transactions.slice(fromIndex, toIndex);
    };

    SimulationChart.prototype.getViewportMarkValues = function() {
        var viewportIndexRange = this.config.viewportIndexRange; // array of [from, to]
        var marks = this.data.marks;
        var fromIndex = 0;
        var toIndex = 0;

        for (var i in marks) {
            var markIndex = this.config.indexByDate(marks[i].date);
            // Closest visible index from
            if (!fromIndex && markIndex >= viewportIndexRange[0]) {
                fromIndex = i;
            }
            // Closest visible index to
            if (markIndex <= viewportIndexRange[1]) {
                toIndex = i;
            } else {
                break;
            }
        }
        toIndex++;
        return marks.slice(fromIndex, toIndex);
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
