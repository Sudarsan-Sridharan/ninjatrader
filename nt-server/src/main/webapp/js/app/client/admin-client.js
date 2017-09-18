define(['jquery', 'app/auth/token-auth'], function ($, TokenAuth) {

    var importQuotesRestUrl = context.serviceHost + "/tasks/import-pse-trader-quotes";
    var adjustPricesRestUrl = context.serviceHost + "/tasks/price-adjustment/run";
    var renameStockSymbolRestUrl = context.serviceHost + "/tasks/rename-stock-symbol/run";

    function AdminClient() {}

    AdminClient.importQuotes = function(date) {
        var jsonObj = {};
        jsonObj.dates = [];

        if (date) {
            jsonObj.dates.push(date);
        }

        return doPost(importQuotesRestUrl, jsonObj);
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
