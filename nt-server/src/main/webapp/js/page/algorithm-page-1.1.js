requirejs(['../common'], function () {
    requirejs(['jquery', 'require', 'app/algo/algo-editor'], function($, require, AlgoEditor) {
        $(document).ready(function() {

            var algoIdParam = $.queryParam("algoId");

            new AlgoEditor("#algoEditor").loadByAlgoId(algoIdParam);
        });
    });
});