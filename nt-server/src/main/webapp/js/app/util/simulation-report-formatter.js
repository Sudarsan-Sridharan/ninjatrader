define(['jquery'], function($) {

    function SimulationReportFormatter() {

    }

    SimulationReportFormatter.toNode = function(json) {
        console.log(json);
        return div().addClass("simulationResult")
            .append(stats(json))
            .append(transactions(json))
            .append(logs(json));
    };

    function stats(json) {
        return div().addClass("stats").append(subHeading("Stats"))
            .append(field("Symbol", json.symbol))
            .append(field("Starting Cash", json.startingCash))
            .append(field("Ending Cash", json.endingCash))
            .append(field("Profit", json.stats.totalProfit))
            .append(div().html("--------------------------"))
            .append(field("Wins", json.stats.wins + " (" + json.stats.winPcnt * 100 + "%)"))
            .append(field("Losses", json.stats.losses  + " (" + json.stats.losePcnt * 100 + "%)"))
            .append(field("Total Gain Amount", json.stats.totalGain))
            .append(field("Total Loss Amount", json.stats.totalLoss))
            .append(div().html("--------------------------"))
            .append(field("Biggest Win", formatTxn(json.stats.maxGainTxn).html()))
            .append(field("Biggest Loss", formatTxn(json.stats.maxLossTxn).html()))
            .append(field("Biggest % Win", formatTxn(json.stats.maxPcntGainTxn).html()))
            .append(field("Biggest % Loss", formatTxn(json.stats.maxPcntLossTxn).html()))
    }

    function transactions(json) {
        var txns =  div().addClass("transactions").append(subHeading("Transactions"))
        for (var i in json.transactions) {
            var txn = json.transactions[i];
            txns.append(formatTxn(txn));
        }
        return txns;
    }

    function logs(json) {
        var logs = div().addClass("logs").append(subHeading("Broker Logs"));
        for (var i in json.brokerLogs) {
            var log = json.brokerLogs[i];
            logs.append(formatLog(log));
        }
        return logs;
    }

    function div() {
        return $("<div></div>");
    }

    function field(label, value) {
        var field = div();
        field.append($("<label>" + label +":</label>"));
        field.append($("<span> " + value + "</span>"));
        return field;
    }

    function formatTxn(txn) {
        var result =  txn.dt + " - " + txn.tnxType + " " + txn.sym + " " + txn.shares + " shares at " + txn.price + ". Value: " + txn.value;
        if (txn.profit) {
            result += " Profit: " + txn.profit + " (" + txn.profitPcnt + "%)"
        }
        return div().html(result);
    }

    function formatLog(log) {
        return div().html(log);
    }

    function subHeading(title) {
        return $("<h2>" + title + "</h2>");
    }

    return SimulationReportFormatter;
});