define(['jquery'], function ($) {

    var ajaxUrl = context.serviceHost + "/tasks/simulation/run";

    function SimulationClient() {
    }

    SimulationClient.run = function(algoId, symbol) {
        return $.get(ajaxUrl + "?algoId=" + algoId + "&symbol=" + symbol + "&isDebug=true");
    };

    SimulationClient.run = function(algoId, symbol, from, to) {
        return $.get(ajaxUrl + "?algoId=" + algoId + "&symbol=" + symbol + "&from=" + from + "&to=" + to + "&isDebug=true");
    };

    return SimulationClient;
});
