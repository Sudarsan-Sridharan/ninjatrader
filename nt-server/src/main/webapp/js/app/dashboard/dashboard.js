define(['jquery', 'require',
    'app/scanner/scanner',
    'app/status/status',
    'app/dashboard/panel/algolistpanel',
    'app/dashboard/panel/scannerpanel',
    'app/client/algoclient'], function ($, require) {

    var Scanner = require("app/scanner/scanner");
    var Status = require("app/status/status");
    var AlgoListPanel = require("app/dashboard/panel/algolistpanel");
    var ScannerPanel = require("app/dashboard/panel/scannerpanel");
    var AlgoClient = require("app/client/algoclient");

    function Dashboard() {
        this.status = new Status("#status");
    }

    /**
     * Initialize scanner panel
     * @param id of container
     */
    Dashboard.prototype.initScanner = function(id) {
        var scanner = new Scanner(id + " .panelContent");
        var scanButton = $(id + " .refreshBtn");
        var algoSelector = $(id + " .algo");
        var daySelector = $(id + " .days");

        scanner.disable = function() {
            scanButton.prop("disabled", true);
            algoSelector.prop("disabled", true);
            daySelector.prop("disabled", true);
        };
        scanner.enable = function() {
            scanButton.prop("disabled", false);
            algoSelector.prop("disabled", false);
            daySelector.prop("disabled", false);
        };
        scanner.blockAndScan = function() {
            scanner.disable();
            scanner.scan(algoSelector.val(), daySelector.val(), scanner.enable);
        };

        // Scan on button click
        scanButton.click(function() {
            scanner.blockAndScan();
        });

        // Scan on algo change
        algoSelector.change(function() {
            scanner.blockAndScan();
        });

        // Scan on day change
        daySelector.change(function() {
            scanner.blockAndScan();
        });
    };

    Dashboard.prototype.initAdminTaskPanel = function(id) {
        var status = this.status;

        $(id + " .importQuotesBtn").click(function() {
            $(this).prop("disabled", true);
            var importQuotesBtn = $(this);

            var statusItem = status.show("Importing quotes...");

            $.ajax({
                url: context.serviceHost + "/task/import-pse-trader-quotes",
                type: "POST",
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=utf-8"
            }).done(function() {
                statusItem.msg("Successfully imported quotes.").quickShow();
            }).fail(function(e) {
                statusItem.msg("Failed to import quotes: " + e.statusText).quickShow();
            }).always(function() {
                importQuotesBtn.prop("disabled", false);
            });
        });
    };

    Dashboard.prototype.initAlgo = function(callback) {
        var algoListPanel = new AlgoListPanel();
        $("#panels").append(algoListPanel.node());
        algoListPanel.load();

        AlgoClient.getAll(function(data) {
            var algoDropdown = $("select.algo").empty();
            for(var i in data) {
                var algo = data[i];
                var option = $('<option></option>').attr("value", algo.algorithmId).text(algo.description);
                algoDropdown.append(option);
            }

            if (callback) {
                callback();
            }
        });
    };

    $(document).ready(function() {
        var dashboard = new Dashboard();

        dashboard.initAlgo(function() {
            $("#panels").append(new ScannerPanel().node());
            $("#panels").append(new ScannerPanel().node());
        });



        dashboard.initAdminTaskPanel("#admin");
    });

    return Dashboard;
});
