define(['jquery', 'require', './ninjachart', './model/query'], function ($, require, NinjaChart) {

    var Query = require('./model/query');
    $(document).ready(function() {
        var ninjaChart = new NinjaChart('chart');


        $("#symbolForm").submit(function() {
            var symbolInput = $("#symbolInput");
            var symbol = symbolInput.val().toUpperCase();
            var timeframe = $("#timeframe").val();
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
            );

            symbolInput.select();
            return false;
        });

        $("#symbolForm").submit();
    });
});