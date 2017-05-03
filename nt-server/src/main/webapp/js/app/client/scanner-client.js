define(['jquery'], function ($) {

    var ajaxUrl = context.serviceHost + "/task/scanner/run";

    function ScannerClient() {
    }

    ScannerClient.scan = function(algoId, days, callback) {
        $.get(ajaxUrl + "?algoId=" + algoId + "&days=" + days)
            .done(function(data) {
                callback(data);
            });
    };

    return ScannerClient;
});
