define(['jquery', 'require', 'boardlot', './basicpanel', 'app/client/algoclient', 'app/client/scannerclient'], function ($, require) {

    var BoardLot = require('boardlot');
    var BasicPanel = require("./basicpanel");
    var AlgoClient = require("app/client/algoclient");
    var ScannerClient = require("app/client/scannerclient");

    function ScannerPanel() {
        BasicPanel.call(this, "Scanner", "scannerPanel");
        this.actionBar = $("<div></div>").addClass("panelAction");
        this.algoSelector = $("<select></select>").addClass("algo");
        this.daySelector = $("<select></select>").addClass("days");
        this.scanButton = $("<button>Scan</button>").addClass("refreshBtn");
        this.scanResult = $("<div></div>");

        this.actionBar.append(this.algoSelector).append(this.daySelector).append(this.scanButton);
        this.content.append(this.actionBar).append(this.scanResult);

        this._prepareDaysSelector([1, 2, 3, 5, 7, 15, 30]);

        this.disable();

        var that = this;
        this.scanButton.click(function() {
            that.scan();
        });

        this.load();
    }

    ScannerPanel.prototype = Object.create(BasicPanel.prototype);
    ScannerPanel.prototype.constructor = ScannerPanel;

    ScannerPanel.prototype._prepareDaysSelector = function(dayOptions) {
        for (var i in dayOptions) {
            var days = dayOptions[i];
            var description = days + " days ago";
            if (days == 1) {
                description = "Today";
            }
            var option = $('<option value="' + days + '">' + description + '</option>');
            this.daySelector.append(option);
        }
    }

    ScannerPanel.prototype.load = function() {
        var algoSelector = this.algoSelector;
        var that = this;

        AlgoClient.getAll(function(algorithms) {
            algoSelector.html("");
            for (var i in algorithms) {
                var algorithm = algorithms[i];
                var option = $('<option value="' + algorithm.algorithmId + '">' + algorithm.description + '</option>');
                algoSelector.append(option);
            }
            that.enable();
        });
    };

    ScannerPanel.prototype.disable = function() {
        this.scanButton.prop("disabled", true);
        this.algoSelector.prop("disabled", true);
        this.daySelector.prop("disabled", true);
    };

    ScannerPanel.prototype.enable = function() {
        this.scanButton.prop("disabled", false);
        this.algoSelector.prop("disabled", false);
        this.daySelector.prop("disabled", false);
    };

    ScannerPanel.prototype.scan = function() {
        var that = this;
        var algoId = this.algoSelector.val();

        this.disable();

        ScannerClient.scan(algoId, this.daySelector.val(), function(scanResult) {
            that._displayResults(scanResult, algoId);
            that.enable();
        });
    };

    ScannerPanel.prototype.node = function() {
        return this.container;
    };

    ScannerPanel.prototype._displayResults = function(scanResults, algoId) {
        var table = $('<table cellpadding="0" cellspacing="0"></table>');
        var th = $("<tr><th>Symbol</th><th>1yr Profit</th><th>Action</th><th>Date</th><th>Price</th></tr>")
        table.append(th);

        for (var i in scanResults) {
            var result = scanResults[i];
            var txn = result.lastTxn;
            var tr = $('<tr class="' + txn.type +'"></tr>');
            var link = '<a href="chart?&algoId=' + algoId + '&symbol=' + result.symbol + '" target="_blank">' + result.symbol + '</a>';

            var txnDate = this._formatDate(txn.dt);
            var profit = Math.trunc(result.profit).toLocaleString();
            var decimalPlaces = BoardLot.getDecimalPlaces(txn.price);

            tr.append('<td class="symbol">' + link + '</td>');
            tr.append('<td class="profit">' + profit + '</td>');
            tr.append('<td class="txnType">' + txn.tnxType + '</td>');
            tr.append('<td class="date">' + txnDate + '</td>');
            tr.append('<td class="price">' + txn.price.toFixed(decimalPlaces) + '</td>');

            table.append(tr);
        }

        var container = this.scanResult;
        container.fadeOut(300, function() {
            container.html("").append(table).fadeIn(300);
        })
    }

    ScannerPanel.prototype._formatDate = function(basicIsoDate) {
        var year = Math.trunc(basicIsoDate / 10000);
        var month = Math.trunc(basicIsoDate / 100 % 100);
        var day = Math.trunc(basicIsoDate % 100);

        month = ("0" + month).slice(-2);
        day = ("0" + day).slice(-2);

        return month + "/" + day + "/" + year;
    }

    return ScannerPanel;
});
