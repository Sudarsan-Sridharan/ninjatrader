define(['jquery', 'require', 'jquerySerializeJson', 'ace',
    'app/status/status',
    'app/client/algoclient',
    'app/client/simulationclient'],

    function ($, require, serializeJson, ace) {

    var Status = require("app/status/status");
    var AlgoClient = require("app/client/algoclient");
    var SimulationClient = require("app/client/simulationclient");

    function AlgoEditor(containerId) {
        this.container = $(containerId);
        this.form = this.container.find(".saveForm");
        this.algoId = this.container.find("[name=algoId]");
        this.algorithm = this.container.find(".algorithm");
        this.description = this.container.find(".description");
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

    AlgoEditor.prototype.load = function() {
        var algoIdParam = $.queryParam("algoId");

        if (!algoIdParam) return;

        var algoId = this.algoId;
        var description = this.description;
        var editor = this.editor;
        var statusMsg = this.status.show("Loading... ");

        AlgoClient.getById(algoIdParam)
            .done(function(data) {
                algoId.val(data.algorithmId);
                description.val(data.description);
                editor.setValue(data.algorithm);
                statusMsg.remove();
            });
    };

    AlgoEditor.prototype.save = function() {
        var statusMsg = this.status.show("Saving...");
        var formObj = this.form.serializeJSON();
        formObj.algorithm = this.editor.getValue();

        AlgoClient.save(formObj).done(function(data) {
            statusMsg.quickShow("Saving... Success!");
        });
    };

    AlgoEditor.prototype.runAlgorithm = function() {
        var statusMsg = this.status.show("Running...");
        var symbol = this.symbolInput.val();
        var algoId = this.algoId.val();

        SimulationClient.run(algoId, symbol, function(simulationReport) {
            console.log(simulationReport);
            if (simulationReport.error) {
                statusMsg.quickShow(simulationReport.error);
            } else {
                statusMsg.quickShow("Running... Success!");
            }
        });
    };

    $(document).ready(function() {
        new AlgoEditor("#algoEditor").load();
    });
});
