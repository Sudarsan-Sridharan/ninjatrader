define(['jquery'], function ($) {

    var ajaxUrl = context.serviceHost + "/task/simulation/run";

    function SimulationClient() {
    }

    SimulationClient.run = function(algoId, symbol) {
        return $.get(ajaxUrl + "?algoId=" + algoId + "&symbol=" + symbol + "&isDebug=true");
    };

    return SimulationClient;
});
