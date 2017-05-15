requirejs(['../common'], function () {
    requirejs(['jquery', 'require', 'app/algo/algo-editor', 'app/status/status',], function($, require, AlgoEditor, Status) {
        $(document).ready(function() {

            var algoIdParam = $.queryParam("algoId");
            var status = new Status("#status");

            new AlgoEditor("#algoEditor", status).loadByAlgoId(algoIdParam);
        });
    });
});