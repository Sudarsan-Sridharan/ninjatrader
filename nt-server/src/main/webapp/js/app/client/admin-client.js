define(['jquery', 'app/token/token-auth'], function ($, TokenAuth) {

    var importQuotesRestUrl = context.serviceHost + "/task/import-pse-trader-quotes";
    var adjustPricesRestUrl = context.serviceHost + "/task/price-adjustment/run";
    var renameStockSymbolRestUrl = context.serviceHost + "/task/rename-stock-symbol/run";

    function AdminClient() {}

    AdminClient.importQuotes = function() {
        var jsonObj = {};
        jsonObj.dates = [];

        return doPost(importQuotesRestUrl, { dates: []});
    };

    AdminClient.adjustPrices = function(symbol, from, to, script) {
        var jsonObj = {};
        jsonObj.symbol = symbol;
        jsonObj.from = from;
        jsonObj.to = to;
        jsonObj.script = script;

        return doPost(adjustPricesRestUrl, jsonObj);
    };

    AdminClient.renameStockSymbol = function(from, to) {
        var jsonObj = {};
        jsonObj.from = from;
        jsonObj.to = to;

        return doPost(renameStockSymbolRestUrl, jsonObj);
    }

    /**
     * Posts data via ajax
     */
    function doPost(url, data) {
        return $.ajax({
            url: url,
            type: "POST",
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(data),
            beforeSend: TokenAuth.addAuthHeaders
        });
    }

    return AdminClient;
});
