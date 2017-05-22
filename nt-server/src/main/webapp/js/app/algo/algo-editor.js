define(['jquery', 'require', 'jquerySerializeJson', 'ace',
    'app/client/algo-client'], function ($, require, serializeJson, ace) {

    var AlgoClient = require("app/client/algo-client");

    function AlgoEditor(containerId) {
        this.container = $(containerId);
        this.editor = ace.edit("codeEditor");
        this.editor.setTheme("ace/theme/monokai");
        this.editor.getSession().setMode("ace/mode/groovy");
    }

    AlgoEditor.prototype.loadByAlgoId = function(algoId) {
        if (!algoId) return;
        var editor = this.editor;
        return AlgoClient.getById(algoId)
            .done(function(data) {
                editor.setValue(data.algorithm);
            });
    };

    AlgoEditor.prototype.save = function(algoId, description) {
        var form = {};
        form.algoId = algoId;
        form.description = description;
        form.algorithm = this.editor.getValue();

        return AlgoClient.save(form);
    };

    AlgoEditor.prototype.val = function() {
        return this.editor.getValue();
    };

    return AlgoEditor;
});
