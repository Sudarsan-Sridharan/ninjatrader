requirejs(['../common'], function () {
    requirejs(['jquery', 'require',
        'app/ninjachart/ninjachart',
        'app/ninjachart/model/query',
        'app/client/algo-client'], function($, require) {

        var Query = require('app/ninjachart/model/query');
        var NinjaChart = require('app/ninjachart/ninjachart');
        var AlgoClient = require('app/client/algo-client');

        $(document).ready(function() {
            var chart = new NinjaChart('chart');
            var symbolParam = $.queryParam("symbol");
            var algoIdParam = $.queryParam("algoId");

            var form = $("#chartForm");
            var symbolInput = form.find("[name='symbol']");
            var algoIdInput = form.find("[name='algoId']");

            initAlgoSelector();

            initFormSubmit();

            initOnPageLoad();

            /**
             * Load algorithms for the selector.
             */
            function initAlgoSelector() {
                form.find("button").prop("disabled", true);
                AlgoClient.getAll(function(algorithms) {
                    algoIdInput.html("");
                    for (var i in algorithms) {
                        var algorithm = algorithms[i];
                        var option = $('<option value="' + algorithm.algorithmId + '">' + algorithm.description + '</option>');
                        if (algoIdParam == algorithm.algorithmId) {
                            option.prop("selected", true);
                        }
                        algoIdInput.append(option);
                    }
                    form.find("button").prop("disabled", false);
                });
            }

            function initFormSubmit() {
                form.submit(function() {

                    var symbol = symbolInput.val().toUpperCase();
                    var algoId = algoIdInput.val();


                    showChart(symbol, "ONE_DAY", algoId);

                    symbolInput.select();

                    return false;
                });
            }

            function initOnPageLoad() {
                if (symbolParam) {
                    var symbol = symbolParam.toUpperCase();
                    showChart(symbol, "ONE_DAY", algoIdParam);
                    symbolInput.val(symbol);
                    algoIdInput.val(algoIdParam);
                }
            }

            /**
             * Display chart given symbol, timeframe, and algoId
             */
            function showChart(symbol, timeframe, algoId) {
                symbol = symbol.toUpperCase();
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
                    .setAlgoId(algoId), function() {

                    var url = "chart?&algoId=" + algoId + "&symbol=" + symbol;
                    window.history.pushState(chart.getState(), symbol, url);
                });
            }

            /**
             * Restore state when user clicks back / forward.
             */
            $(window).bind("popstate", function(e) {
                var state = e.originalEvent.state;
                if (state) {
                    chart.setState(state);
                    algoIdInput.val(state.query.algoId);
                    symbolInput.val(state.query.symbol);
                    document.title = state.query.symbol + " | Beach Ninja Trader";
                }
            });
        });
    });
});
