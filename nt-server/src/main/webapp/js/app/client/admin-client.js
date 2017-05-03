define(['jquery'], function ($) {

    var importQuotesRestUrl = context.serviceHost + "/task/import-pse-trader-quotes";
    var adjustPricesRestUrl = context.serviceHost + "/task/price-adjustment/run";

    function AdminClient() {}

    AdminClient.importQuotes = function() {
        return $.ajax({
            url: importQuotesRestUrl,
            type: "POST",
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=utf-8"
        });
    };

    AdminClient.adjustPrices = function(symbol, from, to, script) {
        var jsonObj = {};
        jsonObj.symbol = symbol;
        jsonObj.from = from;
        jsonObj.to = to;
        jsonObj.script = script;

        return $.ajax({
            url: adjustPricesRestUrl,
            type: "POST",
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(jsonObj)
        });
    };

    return AdminClient;
});
