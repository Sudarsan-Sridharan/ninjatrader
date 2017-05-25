requirejs(["../common"], function () {
    requirejs(["jquery",
        "require",
        "app/algo/algo-editor",
        "app/client/algo-client",
        "app/client/simulation-client",
        "app/status/status",
        "app/ui/action-panel",
        "app/util/simulation-report-formatter"], function($, require) {

        var AlgoEditor = require("app/algo/algo-editor");
        var AlgoClient = require("app/client/algo-client");
        var Status = require("app/status/status");
        var ActionPanel = require("app/ui/action-panel");
        var SimulationClient = require("app/client/simulation-client");
        var SimulationReportFormatter = require("app/util/simulation-report-formatter");

        $(document).ready(function() {
            var algoIdParam = $.queryParam("algoId");

            var status = new Status("#status");
            var actionPanel = new ActionPanel("#actionPanel");
            var algoEditor = new AlgoEditor("#algoEditor");

            var saveAlgoForm = $(".saveAlgoForm");
            var algoIdInput = $("[name=algoId]");
            var descInput = $("[name=description]");
            var symbolInput = $("[name=symbol]");
            var runBtn = $(".runBtn");

            /**
             * Load Algorithm
             */
            if (algoIdParam) {
                var loadingAlgoStatus = status.show("Loading algorithm...");
                $.when(algoEditor.loadByAlgoId(algoIdParam)).done(function (data) {
                    algoIdInput.val(data.algorithmId);
                    descInput.val(data.description);
                    loadingAlgoStatus.remove();
                });
            }

            /**
             * Toggle Action panel
             */
            $(".actionToggleBtn").click(function() {
                actionPanel.toggle();
            });

            /**
             * Save Algorithm
             */
            $(".saveBtn").click(function() {
                var saveStatus = status.show("Saving...");
                $.when(algoEditor.save(algoIdInput.val(), descInput.val())).done(function(data) {
                    algoIdInput.val(data.algorithmId);
                    saveStatus.quickShow("Saving... Success!");
                });
                return false;
            });

            /**
             * Run Algorithm button
             */
            $(".runBtn").click(function() {
                var runStatus = status.show("Running...");

                SimulationClient.run(algoIdInput.val(), symbolInput.val())
                    .done(function(result) {
                        if (result.error) {
                            runStatus.show(result.error);
                        } else {
                            var formattedResult = SimulationReportFormatter.toNode(result);
                            // Show result to action panel.
                            actionPanel.body(formattedResult).show();
                            runStatus.quickShow("Running... Success!");
                        }
                    }).fail(function(e) {
                        var error = JSON.parse(e.responseText);
                        runStatus.quickShow(error.message);
                    });

                return false;
            });
        });
    });
});