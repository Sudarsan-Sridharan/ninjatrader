/**
 * Created by Brad on 5/29/16.
 */

function IchimokuChart(ninjaChart) {
    this.ajaxUrl = 'ichimoku';
    this.ninjaChart = ninjaChart;
    this.className = 'ichimoku';
    this.data = null;
    this.priceData = null;

    //svg = $(this.ninjaChart.id + ' svg');
    //this.ninjaChart.addGroup(svg, this.className);
}

IchimokuChart.prototype.showChart = function(symbol, period, priceData) {
    this.priceData = priceData;

    var periodUrlPath = '';
    if (period == '1D') {
        periodUrlPath = '/daily';
    } else if (period == '1W') {
        periodUrlPath = '/weekly';
    }

    var thisInstance = this;
    $.get(this.ajaxUrl + periodUrlPath + "/" + symbol, function(data) {
        thisInstance.data = data;
        thisInstance.clearChart();
        thisInstance.printChart();
        thisInstance.ninjaChart.refreshChart();
    });
}

IchimokuChart.prototype.printChart = function() {
    this.ichimokuCanvas = $(this.ninjaChart.id + ' .' + this.className);

    var tenkan = $('<polyline></polyline>').addClass("tenkan");
    var kijun = $('<polyline></polyline>').addClass("kijun");
    var chikou = $('<polyline></polyline>').addClass("chikou");
    var senkouA = $('<polyline></polyline>').addClass("senkouA");
    var senkouB = $('<polyline></polyline>').addClass("senkouB");
    var kumo = $('<g></g>').addClass("kumo");

    var x = this.ninjaChart.canvasPaddingLeft + (this.ninjaChart.candleWidth / 2);

    var tenkanPath = '';
    var kijunPath = '';
    var chikouPath = '';
    var senkouAPath = '';
    var senkouBPath = '';

    this.ichimokuCanvas.append(tenkan);
    this.ichimokuCanvas.append(kijun);
    this.ichimokuCanvas.append(chikou);
    this.ichimokuCanvas.append(senkouA);
    this.ichimokuCanvas.append(senkouB);
    this.ichimokuCanvas.append(kumo);

    for (var i in this.data) {
        var ichimoku = this.data[i];

        if (ichimoku.t > 0) {
            var y = this.ninjaChart.priceToY(ichimoku.t);
            tenkanPath += x + ',' + y + ' ';
        }

        if (ichimoku.k > 0) {
            var y = this.ninjaChart.priceToY(ichimoku.k);
            kijunPath += x + ',' + y + ' ';
        }

        if (ichimoku.c > 0) {
            var y = this.ninjaChart.priceToY(ichimoku.c);
            chikouPath += x + ',' + y + ' ';
        }

        var senkouA_Y;
        if (ichimoku.sa > 0) {
            senkouA_Y = this.ninjaChart.priceToY(ichimoku.sa);
            senkouAPath += x + ',' + senkouA_Y + ' ';
        }

        if (ichimoku.sb > 0) {
            var y = this.ninjaChart.priceToY(ichimoku.sb);
            senkouBPath += x + ',' + y + ' ';

            var clazz = ichimoku.sa > ichimoku.sb ? "u" : "d";
            var kumoLine = $('<line></line>')
                .addClass(clazz)
                .attr("x1", x)
                .attr("y1", senkouA_Y)
                .attr("x2", x)
                .attr("y2", y);
            kumo.append(kumoLine);
        }
        x += this.ninjaChart.candleWidth + this.ninjaChart.candleMargin;
    }

    tenkan.attr("points", tenkanPath);
    kijun.attr("points", kijunPath);
    chikou.attr("points", chikouPath);
    senkouA.attr("points", senkouAPath);
    senkouB.attr("points", senkouBPath);
}

IchimokuChart.prototype.clearChart = function() {
    $(this.ninjaChart.id + ' .' + this.className).html('');
}