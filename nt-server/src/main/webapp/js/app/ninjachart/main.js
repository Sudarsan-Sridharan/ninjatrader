define(['jquery', 'require', './ninjachart', './model/query'], function ($, require, NinjaChart) {

    var Query = require('./model/query');

    var ninjaChart = new NinjaChart('chart');

    /**
     * Similar to request.getParameter("name")
     * @param name
     * @returns parameter value
     */
    $.queryParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results==null){
            return null;
        }
        else{
            return results[1] || 0;
        }
    };

    $(document).ready(function() {

        var symbolInput = $("#symbolInput");
        var timeframeInput = $("#timeframe");

        // Submit button
        $("#symbolForm").submit(function() {
            var symbol = symbolInput.val().toUpperCase();
            var timeframe = timeframeInput.val();

            showChart(symbol, timeframe);

            symbolInput.select();
            return false;
        });

        // Check symbol from query param, use it if found.
        var queryParamSymbol = $.queryParam("symbol");
        var queryParamAlgoId = $.queryParam("algoId");
        if (queryParamSymbol) {
            var symbol = queryParamSymbol.toUpperCase();
            showChart(symbol, "ONE_DAY", queryParamAlgoId);
            symbolInput.val(symbol);
        }
    });

    /**
     * Display chart given symbol and timeframe
     * @param symbol
     * @param timeframe
     */
    function showChart(symbol, timeframe, algoId) {
        var symbol = symbol.toUpperCase();
        var from = new Date();
        var to = new Date().toDbFormat();

        from.setFullYear(from.getFullYear() - 10);
        from = from.toDbFormat();

        ninjaChart.show(new Query()
            .setSymbol(symbol)
            .setFrom(from)
            .setTo(to)
            .setTimeframe(timeframe)
            .setPeriods("sma", [20])
            .setPeriods("ema", [18, 50, 100, 200])
            .setPeriods("rsi", [14])
            .setAlgoId(algoId)
        );

        document.title = symbol + " | Beach Ninja Trader";
    }
});
