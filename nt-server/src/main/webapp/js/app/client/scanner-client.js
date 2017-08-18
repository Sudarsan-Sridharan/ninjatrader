define(['jquery', 'app/auth/token-auth'], function ($, TokenAuth) {

    var ajaxUrl = context.serviceHost + "/scan";

    function ScannerClient() {
    }

    ScannerClient.scan = function(algoId, days, callback) {
        var url = ajaxUrl + "/" + algoId + "?days=" + days;

        return $.ajax({
            url: url,
            type: "GET",
            contentType: 'application/json',
            beforeSend: TokenAuth.addAuthHeaders
        }).done(function(data) {
            callback(data);
        });
    };

    return ScannerClient;
});
