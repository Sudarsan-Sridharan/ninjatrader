define(['jquery', 'require',
    'app/util/boardlot',
    'app/dashboard/panel/basicpanel',
    'app/client/algo-client',
    'app/client/scanner-client',
    'app/sse/sse'], function ($, require) {

    var BoardLot = require('app/util/boardlot');
    var BasicPanel = require("app/dashboard/panel/basicpanel");
    var AlgoClient = require("app/client/algo-client");
    var ScannerClient = require("app/client/scanner-client");
    var Sse = require("app/sse/sse");

    function ScannerPanel( id) {
        BasicPanel.call(this, "Scanner", "scannerPanel");
        this.sse = new Sse();
        this.actionBar = $("<div></div>").addClass("panelAction");
        this.algoSelector = $("<select></select>").addClass("algo");
        this.daySelector = $("<select></select>").addClass("days");
        this.scanButton = $("<button>Scan</button>").addClass("refreshBtn");
        this.scanResult = $("<div></div>");

        this.actionBar.append(this.algoSelector).append(this.daySelector).append(this.scanButton);
        this.content.append(this.actionBar).append(this.scanResult);

        this.scanResultsMap = [];

        this.id = id;

        this._init();
    }

    ScannerPanel.prototype = Object.create(BasicPanel.prototype);
    ScannerPanel.prototype.constructor = ScannerPanel;

    ScannerPanel.prototype._init = function() {
        var that = this;

        this.disable();
        this._loadAlgorithms();
        this._initDaysSelector([1, 2, 3, 5, 7, 15, 30]);

        // Initialize Scan button click
        this.scanButton.click(function() {
            that.scan();
        });

        // On change algorithm, do scan
        this.algoSelector.change(function() {
            that.scan();
        });
    };

    /**
     * Load all of user's algorithms for the dropdown.
     */
    ScannerPanel.prototype._loadAlgorithms = function() {
        var algoSelector = this.algoSelector;
        var that = this;

        // Retrieve all of user's algorithms
        AlgoClient.getAll(function(algorithms) {
            var algorithmIds = [];
            algoSelector.html("");
            for (var i in algorithms) {
                var algorithm = algorithms[i];
                var option = $('<option value="' + algorithm.algorithmId + '">' + algorithm.description + '</option>');
                algoSelector.append(option);
                algorithmIds.push(algorithm.algorithmId);
            }
            that.enable();
            that._onAlgorithmsLoaded(algorithmIds);
        });
    };

    /**
     * Initialize dropdown for selecting days.
     */
    ScannerPanel.prototype._initDaysSelector = function(dayOptions) {
        var that = this;
        for (var i in dayOptions) {
            var days = dayOptions[i];
            var description = days + " days ago";
            if (days === 1) {
                description = "Today";
            }
            var option = $('<option value="' + days + '">' + description + '</option>');
            this.daySelector.append(option);
        }

        this.daySelector.change(function() {
            that.scan();
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

        // Disable button and show Scanning message
        this.disable();
        this.scanButton.html("Scanning...");

        ScannerClient.scan(algoId, this.daySelector.val(), function(scanResult) {
            that._mergeScanResults(scanResult);
            that._displayResults(scanResult, algoId);
            that.enable();
            that.scanButton.html("Scan");
        });
    };

    ScannerPanel.prototype.node = function() {
        return this.container;
    };

    ScannerPanel.prototype._displayResults = function(scanResults, algoId, highlightSymbols) {
        var table = $('<table cellpadding="0" cellspacing="0"></table>');
        var th = $("<tr><th>Symbol</th><th>1yr Profit</th><th>Action</th><th>Date</th><th>Price</th></tr>");
        table.append(th);

        for (var i in scanResults) {
            var result = scanResults[i];
            var txn = result.lastTxn;
            var tr = $('<tr class="' + txn.txnType.toLowerCase() +'"></tr>');
            var link = '<a href="chart?&algoId=' + algoId + '&symbol=' + result.symbol + '" target="_blank">' + result.symbol + '</a>';
            var txnDate = this._formatDate(txn.dt);
            var profitPcnt = Math.round(result.profitPcnt * 10000) / 100.0 ; //Convert to "100.00%"
            var decimalPlaces = BoardLot.getDecimalPlaces(txn.price);

            if ($.inArray(result.symbol, highlightSymbols) != -1) {
                tr.addClass("new");
            } else {
                tr.removeClass("new");
            }

            tr.append('<td class="symbol">' + link + '</td>');
            tr.append('<td class="profit">' + profitPcnt + '%</td>');
            tr.append('<td class="txnType">' + txn.txnType + '</td>');
            tr.append('<td class="date">' + txnDate + '</td>');
            tr.append('<td class="price">' + txn.price.toFixed(decimalPlaces) + '</td>');

            table.append(tr);
        }

        var container = this.scanResult;
        container.fadeOut(300, function() {
            container.html("").append(table).fadeIn(300);
        })
    };

    /**
     * Add listener to each algorithm once it's loaded.
     */
    ScannerPanel.prototype._onAlgorithmsLoaded = function(algorithmIds) {
        var that = this;
        for (var i in algorithmIds) {
            var algorithmId = algorithmIds[i];
            this.sse.addListener(algorithmId, function (e) {
                if (that.algoSelector.val() == e.type) {
                    that._onNewUpdateReceived(JSON.parse(e.data), e.type);
                }
            });
        }
    };

    ScannerPanel.prototype._onNewUpdateReceived = function(newScanResults, algorithmId) {
        this._mergeScanResults(newScanResults);

        // Get new ScanResults to highlight
        var highlightSymbols = [];
        for (var i in newScanResults) {
            highlightSymbols.push(newScanResults[i].symbol);
        }

        // Get all ScanResults
        var scanResults = []
        for (var i in this.scanResultsMap) {
            scanResults.push(this.scanResultsMap[i]);
        }

        // Sort by symbols
        scanResults.sort(function(a, b) {
            return a.symbol.localeCompare(b.symbol);
        });

        console.log(highlightSymbols, scanResults);

        this._displayResults(scanResults, algorithmId, highlightSymbols);
    };

    ScannerPanel.prototype._mergeScanResults = function(newScanResults) {
        for (var i in newScanResults) {
            var scanResult = newScanResults[i];
            this.scanResultsMap[scanResult.symbol] = scanResult;
        }
    };

    ScannerPanel.prototype._formatDate = function(basicIsoDate) {
        var year = Math.trunc(basicIsoDate / 10000);
        var month = Math.trunc(basicIsoDate / 100 % 100);
        var day = Math.trunc(basicIsoDate % 100);

        month = ("0" + month).slice(-2);
        day = ("0" + day).slice(-2);

        return month + "/" + day + "/" + year;
    };

    return ScannerPanel;
});
