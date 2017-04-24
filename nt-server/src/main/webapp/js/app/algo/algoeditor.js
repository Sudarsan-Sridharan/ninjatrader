define(['jquery', 'jquerySerializeJson', 'ace', '../status/status'], function ($, serializeJson, ace, Status) {

    function AlgoEditor(containerId) {
        this.container = $(containerId);
        this.form = this.container.find("form");
        this.algoId = this.container.find("[name=algoId]");
        this.algorithm = this.container.find(".algorithm");
        this.description = this.container.find(".description");
        this.saveBtn = this.container.find(".submitBtn");
        this.status = new Status("#status");
        this.editor = ace.edit("codeEditor");
        this.editor.setTheme("ace/theme/monokai");
        this.editor.getSession().setMode("ace/mode/groovy");

        var that = this;
        this.saveBtn.click(function() {
            that.save();
            return false;
        });
    }

    AlgoEditor.prototype.load = function() {
        var algoIdParam = $.queryParam("algoId");

        var algoId = this.algoId;
        var description = this.description;
        var editor = this.editor;
        var statusMsg = this.status.show("Loading... ");

        $.get(context.serviceHost + "/algorithms/" + algoIdParam)
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

        $.ajax({
            url: context.serviceHost + "/algorithms/",
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(formObj),
            dataType: 'json',
            success: function(data) {
                statusMsg.remove();
            }
        });
    }

    $(document).ready(function() {
        new AlgoEditor("#algoEditor").load();
    });
});
