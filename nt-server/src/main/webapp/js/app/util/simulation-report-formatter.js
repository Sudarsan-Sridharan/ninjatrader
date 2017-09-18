define(['jquery', 'app/util/num-util'], function($, NumUtil) {

    function SimulationReportFormatter() {

    }

    SimulationReportFormatter.toNode = function(json) {
        console.log(json);
        return div().addClass("simulationResult")
            .append(summary(json))
            .append(stats(json))
            .append(transactions(json))
            .append(logs(json));
    };

    function summary(json) {
        return div().addClass("summary").append(subHeading("Summary"))
            .append(field("Symbol", json.symbol))
            .append(field("Starting Cash", NumUtil.withComma(json.startingCash)))
            .append(field("Ending Cash", NumUtil.withComma(json.endingCash)))
            .append(field("Profit", NumUtil.withComma(json.profit) + " (" + NumUtil.toPcnt(json.profitPcnt) + "%)"))
    }

    function stats(json) {
        console.log(json);
        return div().addClass("stats").append(subHeading("Stats"))
            .append(field("Wins", json.stats.wins + " (" + NumUtil.toPcnt(json.stats.winPcnt) + "%)"))
            .append(field("Losses", json.stats.losses  + " (" + NumUtil.toPcnt(json.stats.losePcnt) + "%)"))
            .append(field("Total Gain Amount", NumUtil.withComma(json.stats.totalGain)))
            .append(field("Total Loss Amount", NumUtil.withComma(json.stats.totalLoss)))
            .append(div().html("--------------------------"))
            .append(field("Biggest Win", formatShortTxn(json.stats.maxGainTxn).html()))
            .append(field("Biggest Loss", formatShortTxn(json.stats.maxLossTxn).html()))
            .append(field("Biggest % Win", formatShortTxn(json.stats.maxPcntGainTxn).html()))
            .append(field("Biggest % Loss", formatShortTxn(json.stats.maxPcntLossTxn).html()))
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

    function formatShortTxn(txn) {
        var result;
        if (txn) {
            result = formatDate(txn.dt) + " - Profit: " + NumUtil.withComma(txn.profit) + " (" + NumUtil.toPcnt(txn.profitPcnt) + "%)";
        } else {
            result = "N/A";
        }
        return div().html(result);
    }

    function formatTxn(txn) {
        var result =  formatDate(txn.dt) + " - " + txn.txnType + " " + NumUtil.withComma(txn.shares) + " shares at " + txn.price + ". Value: " + NumUtil.withComma(txn.value);
        if (txn.profit) {
            result += " Profit: " + NumUtil.withComma(txn.profit) + " (" + NumUtil.toPcnt(txn.profitPcnt) + "%)"
        }
        return div().html(result);
    }

    function formatLog(log) {
        return div().html(log);
    }

    function subHeading(title) {
        return $("<h2>" + title + "</h2>");
    }

    function formatDate(date) {
        if (!date) return "";

        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    return SimulationReportFormatter;
});