define(['jquery'], function ($) {

    var ajaxUrl = context.serviceHost + "/task/simulation/run";

    function SimulationClient() {
    }

    SimulationClient.run = function(algoId, symbol, callback) {
        $.get(ajaxUrl + "?algoId=" + algoId + "&symbol=" + symbol + "&isDebug=true")
            .done(function(data) {
                callback(data);
            });
    };

    return SimulationClient;
});
