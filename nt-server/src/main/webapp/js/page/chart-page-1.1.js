requirejs(['../common'], function () {
    requirejs(['jquery', 'require',
        'app/ninjachart/ninjachart',
        'app/ninjachart/model/query',
        'app/client/algo-client'], function($, require) {

        var Query = require('app/ninjachart/model/query');
        var NinjaChart = require('app/ninjachart/ninjachart');
        var AlgoClient = require('app/client/algo-client');

        var chart = new NinjaChart('chart');

        $(document).ready(function() {

            var form = $("#chartForm");
            var symbolInput = $("#symbolInput");
            var algoIdInput = $("#algoIdInput");

            form.find("button").prop("disabled", true);
            AlgoClient.getAll(function(algorithms) {
                algoIdInput.html("");
                for (var i in algorithms) {
                    var algorithm = algorithms[i];
                    var option = $('<option value="' + algorithm.algorithmId + '">' + algorithm.description + '</option>');
                    algoIdInput.append(option);
                }
                form.find("button").prop("disabled", false);
            });
            
            // Submit button
            form.submit(function() {
                var symbol = symbolInput.val().toUpperCase();
                showChart(symbol, "ONE_DAY", algoIdInput.val());

                symbolInput.select();
                return false;
            });

            // Check symbol from query param, use it if found.
            var symbolParam = $.queryParam("symbol");
            var algoIdParam = $.queryParam("algoId");
            if (symbolParam) {
                var symbolParam = symbolParam.toUpperCase();
                showChart(symbolParam, "ONE_DAY", algoIdParam);
                symbolInput.val(symbolParam);
                algoIdInput.val(algoIdParam);
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

            chart.show(new Query()
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
});
