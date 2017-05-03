define(['jquery', 'require', 'jquerySerializeJson', 'ace',
    'app/status/status',
    'app/client/algo-client',
    'app/client/simulation-client'], function ($, require, serializeJson, ace) {

    var Status = require("app/status/status");
    var AlgoClient = require("app/client/algo-client");
    var SimulationClient = require("app/client/simulation-client");

    function AlgoEditor(containerId) {
        this.container = $(containerId);
        this.form = this.container.find(".saveForm");
        this.algoIdInput = this.container.find("[name=algoId]");
        this.descInput = this.container.find(".description");
        this.symbolInput = this.container.find(".symbol");
        this.saveBtn = this.container.find(".submitBtn");
        this.runBtn = this.container.find(".runBtn");
        this.status = new Status("#status");
        this.editor = ace.edit("codeEditor");
        this.editor.setTheme("ace/theme/monokai");
        this.editor.getSession().setMode("ace/mode/groovy");

        var that = this;
        this.saveBtn.click(function() {
            that.save();
            return false;
        });
        this.runBtn.click(function() {
            that.runAlgorithm();
            return false;
        });
    }

    AlgoEditor.prototype.loadByAlgoId = function(algoId) {
        if (!algoId) return;

        var algoIdInput = this.algoIdInput;
        var descInput = this.descInput;
        var editor = this.editor;
        var statusMsg = this.status.show("Loading... ");

        AlgoClient.getById(algoId)
            .done(function(data) {
                algoIdInput.val(data.algorithmId);
                descInput.val(data.description);
                editor.setValue(data.algorithm);
                statusMsg.remove();
            });
    };

    AlgoEditor.prototype.save = function() {
        var statusMsg = this.status.show("Saving...");
        var formObj = this.form.serializeJSON();
        formObj.algorithm = this.editor.getValue();

        AlgoClient.save(formObj).done(function() {
            statusMsg.quickShow("Saving... Success!");
        });
    };

    AlgoEditor.prototype.runAlgorithm = function() {
        var statusMsg = this.status.show("Running...");
        var symbol = this.symbolInput.val();
        var algoId = this.algoIdInput.val();

        SimulationClient.run(algoId, symbol, function(simulationReport) {
            console.log(simulationReport);
            if (simulationReport.error) {
                statusMsg.quickShow(simulationReport.error);
            } else {
                statusMsg.quickShow("Running... Success!");
            }
        });
    };

    return AlgoEditor;
});
