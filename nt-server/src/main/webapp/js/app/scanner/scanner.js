define(['jquery'], function ($) {

    function Scanner(containerId) {
        this.container = $(containerId);
        this.ajaxUrl = "/task/scanner/run?algoId=";
    }

    Scanner.prototype.scan = function(algoId, scanCompleteCallback) {
        var that = this;
        $.get(context.serviceHost + that.ajaxUrl + algoId)
            .done(function(data) {
                that._displayResults(data, algoId);
                if (scanCompleteCallback) {
                    scanCompleteCallback();
                }
            });
    }

    Scanner.prototype._displayResults = function(scanResults, algoId) {
        var table = $('<table cellpadding="0" cellspacing="0"></table>');
        var th = $("<tr><th>Symbol</th><th>1yr Profit</th><th>Action</th><th>Date</th><th>Price</th></tr>")
        table.append(th);

        for (var i in scanResults) {
            var result = scanResults[i];
            var txn = result.lastTxn;
            var tr = $('<tr class="' + txn.type +'"></tr>');
            var link = '<a href="/chart?&algoId=' + algoId + '&symbol=' + result.symbol + '" target="_blank">' + result.symbol + '</a>';

            var txnDate = this._formatDate(txn.dt);
            var profit = Math.trunc(result.profit).toLocaleString();

            tr.append('<td class="symbol">' + link + '</td>');
            tr.append('<td class="profit">' + profit + '</td>');
            tr.append('<td class="txnType">' + txn.tnxType + '</td>');
            tr.append('<td class="date">' + txnDate + '</td>');
            tr.append('<td class="price">' + txn.price + '</td>');

            table.append(tr);
        }

        var container = this.container;
        this.container.fadeOut(300, function() {
            container.html("").append(table).fadeIn(300);
        })
    }

    Scanner.prototype._formatDate = function(basicIsoDate) {
        var year = Math.trunc(basicIsoDate / 10000);
        var month = Math.trunc(basicIsoDate / 100 % 100);
        var day = Math.trunc(basicIsoDate % 100);

        month = ("0" + month).slice(-2);
        day = ("0" + day).slice(-2);

        return month + "/" + day + "/" + year;
    }

    return Scanner;
});
