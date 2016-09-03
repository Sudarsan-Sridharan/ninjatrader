var monthNames = ['', 'Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];

function NinjaChart(divId, readOnly) {
    this.id = '#' + divId;
    this.ajaxUrl = "price";
    this.readOnly = readOnly;

    // Chart Container
    this.chart = $(this.id);

    // Candle Settings
    this.candleWidth = 4;
    this.candleMargin = 3;

    // Chart Settings
    this.canvasPaddingLeft = 40;
    this.canvasPaddingTop = 50;
    this.canvasPaddingBottom = 30;
    this.canvasHeight = 400;
    this.canvasWidth = 1200;
    this.chartHeight = this.canvasHeight - this.canvasPaddingTop - this.canvasPaddingBottom;

    // Date Indicator Settings
    this.dateIndHeight = 20;
    this.dateIndFontSize = 10;

    this.extraCharts = new Array();

    // Create the canvas
    this.initCanvas();
}

/**
 * Update the chart w/ new boardlot document
 * @param json
 */
NinjaChart.prototype.showChart = function(symbol, period) {
    this.symbol = symbol;

    var periodUrlPath = '';
    if (period == '1D') {
        periodUrlPath = '/daily';
    } else if (period == '1W') {
        periodUrlPath = '/weekly';
    }

    var thisInstance = this;

    $.get(this.ajaxUrl + "/" + symbol + periodUrlPath, function(priceData) {
        thisInstance.showChartWithData(symbol, period, priceData);
    });
}

NinjaChart.prototype.showChartWithData = function(symbol, period, priceData) {
    this.symbol = symbol;
    this.priceData = priceData;
    this.lowest = priceData.summary.l * 0.9; // give more headroom
    this.highest = priceData.summary.h * 1.10; // give more headroom
    this.diff = this.highest - this.lowest;
    this.canvasRatio = this.chartHeight / this.diff;
    this.x = this.canvasPaddingLeft;

    this.clearChart();
    this.printChart();

    // Print other charts (e.g. Ichimoku)
    for (var j in this.extraCharts) {
        this.extraCharts[j].showChart(symbol, period, priceData);
    }

    // Refresh to display SVG stuff
    this.refreshChart();
}

NinjaChart.prototype.refreshChart = function() {
    this.chart.html(this.chart.html());

    // Init interactions
    this.initInteraction();
}

/**
 * Clear the boardlot chart
 */
NinjaChart.prototype.clearChart = function() {
    $(this.id + ' .cols').html('');
    $(this.id + ' .candles').html('');
    $(this.id + ' .wicks').html('');
    $(this.id + ' .prices').html('');
    $(this.id + ' .priceLines').html('');
    $(this.id + ' .dateLines').html('');
    $(this.id + ' .dateLabels').html('');
    $(this.id + ' .lastPrice').html('');
    $(this.id + ' .stockMeta .symbol').html('');

    // Clear other charts (e.g. Ichimoku)
    for (var i in this.extraCharts) {
        this.extraCharts[i].clearChart();
    }
}

/**
 * Create the canvas
 */
NinjaChart.prototype.initCanvas = function() {
    $(this.id).addClass("ninjaChart");

    var svg = $("<svg></svg>").attr("style", "width:"+this.canvasWidth+"px;height:"+this.canvasHeight+"px;");
    this.chart.append(svg);
    svg = $(this.id + ' svg');

    this.addGroup(svg, "cols");
    this.addGroup(svg, "priceLines");
    this.addGroup(svg, "prices");
    this.addGroup(svg, "lastPrice")
    this.addGroup(svg, "dateLines");
    this.addGroup(svg, "datelabels");
    this.addGroup(svg, "wicks");
    this.addGroup(svg, "candles");
    this.addGroup(svg, "xCursor");
    this.addGroup(svg, "yCursor");
    this.addGroup(svg, "priceMeta");
    this.addGroup(svg, "stockMeta");

    this.cols = $(this.id + ' .cols');
    this.priceLines = $(this.id + ' .priceLines');
    this.prices = $(this.id + ' .prices');
    this.dateLines = $(this.id + ' .dateLines')
    this.dateLabels = $(this.id + ' .datelabels')
    this.candles = $(this.id + ' .candles');
    this.wicks = $(this.id + ' .wicks');
    this.candles = $(this.id + ' .candles');
    this.stockMeta = $(this.id + ' .stockMeta');
    this.lastPrice = $(this.id + ' .lastPrice');

    this.stockMeta.html('<text  x="20" y="20" class="symbol"></text>');

    if (!this.readOnly) {
        this.xCursor = $(this.id + ' .xCursor');
        this.yCursor = $(this.id + ' .yCursor');
        this.priceMeta = $(this.id + ' .priceMeta');

        var xCursorLine = $("<line></line>")
            .addClass("xCursorLine")
            .attr("x1", 0)
            .attr("y1", 0)
            .attr("x2", 0);
        var xCursorBox = $("<rect></rect>")
            .attr("x", -30)
            .attr("y", this.canvasHeight - 20)
            .attr("height", 12)
            .attr("width", 60);
        var xCursorText = $("<text></text>")
            .attr("x", -25)
            .addClass("dateInd")
        this.xCursor.append(xCursorLine);
        this.xCursor.append(xCursorBox);
        this.xCursor.append(xCursorText);

        this.priceMeta.html('<text  x="100" y="20">' +
            'O: <tspan class="metaValue open"></tspan> ' +
            'H: <tspan class="metaValue high"></tspan> ' +
            'L: <tspan class="metaValue low"></tspan> ' +
            'C: <tspan class="metaValue close"></tspan> ' +
            'V: <tspan class="metaValue volume"></tspan>' +
            '</text>');
    }
}

NinjaChart.prototype.addGroup = function(container, className) {
    var g = $("<g></g>").addClass(className);
    container.append(g);
}

/**
 * Print the boardlot chart
 */
NinjaChart.prototype.printChart = function() {
    this.printPriceIndicators();
    this.printXCursor();
    this.printLastPriceInd();

    // Show boardlot symbol
    $(this.id + ' .stockMeta .symbol').html(this.symbol);

    var x = this.canvasPaddingLeft;
    var lastMonth = 0;
    var lastPrintedDateX = 0; // to check if printed dates are too close.
    var minDateXMargin = 100;

    for (var i in this.priceData.prices) {
        var price = this.priceData.prices[i];
        var year = parseInt(price.d.substr(0, 4));
        var month = parseInt(price.d.substr(4, 2));

        // If month has changed
        if (month != lastMonth) {
            lastMonth = month;

            // Check that we're not printing dates too closely.
            if (x - lastPrintedDateX > minDateXMargin) {
                lastPrintedDateX = x;
                this.printDateIndicator(x, year, month);
            }
        }

        this.printColumn(x, price);
        this.printCandleBar(x, price);
        this.printCandleWick(x, price);

        x += this.candleWidth + this.candleMargin;
    }
}

NinjaChart.prototype.getBaseUnit = function(price) {
    if (price > 1000) return 5;
    if (price > 10) return 1;
    if (price > 3) return 0.5;
    if (price > 1) return 0.05;
    if (price > 0.1) return 0.01;
    if (price > 0.01) return 0.001;
    if (price > 0.001) return 0.0005;
    return 0.00005;
}

NinjaChart.prototype.getDecimalPlaces = function(price) {
    if (price >= 1000) return 0;
    if (price >= 0.01) return 2;
    if (price >= 0.0001) return 4;
    return 5;
}

/**
 * Print price indicators along the Y-axis
 */
NinjaChart.prototype.printPriceIndicators = function() {
    var pip = this.getBaseUnit(this.priceData.summary.o);

    var minMargin = 30; // minimum px margin between price indicators
    var marginBetweenPips = Math.abs((pip - (pip * 2)) * this.canvasRatio); // margin between 2 pips

    var decimalPlaces = this.getDecimalPlaces(this.priceData.summary.o);
    var base = Math.pow(10, decimalPlaces);

    // set right amount of pips so that prices have enough room between them.
    pip = pip * Math.floor(minMargin / marginBetweenPips) + pip;

    // plot prices from lowest to highest, increasing in pips
    for (var i = (Math.floor(this.lowest-pip) + pip); i < (this.highest + pip); i += pip) {
        i = Math.ceil(i * base)/base; // floating point precision

        var priceY = this.priceToY(i);

        // Prevent breaching top
        if (priceY < this.canvasPaddingTop) {
            continue;
        }
        // Prevent breaching bottom
        if (priceY > (this.chartHeight + this.canvasPaddingTop)) {
            continue
        }

        // Price text
        var price = $('<text>'+ i.toFixed(decimalPlaces) +'</text>')
            .attr("x", 2)
            .attr("y", priceY + 3);
        $(this.id + ' .prices').append(price);

        // Price Line
        var priceLine = $('<line></line>')
            .attr("x1", 35)
            .attr("y1", priceY)
            .attr("x2", this.canvasWidth)
            .attr("y2", priceY);
        $(this.id + ' .priceLines').append(priceLine);
    }

    // Price Border Line
    var borderLine = $('<line></line>')
        .addClass("border")
        .attr("x1", 35)
        .attr("y1", this.canvasPaddingTop)
        .attr("x2", 35)
        .attr("y2", this.canvasHeight - this.canvasPaddingBottom);
    $(this.id + ' .priceLines').append(borderLine);
}

/**
 * Print a date indicator given the month and X coordinate
 */
NinjaChart.prototype.printDateIndicator = function(x, year, month) {
    // Print Date Labels
    var dateLabel = $('<text>' + (month == 1 ? year : monthNames[month]) + '</text>');
    dateLabel.attr("x", x - 10);
    dateLabel.attr("y", this.chartHeight + this.canvasPaddingTop + this.canvasPaddingBottom - 5);
    $(this.id + ' .dateLabels').append(dateLabel);
    if (month == 1) {
        dateLabel.addClass("year");
    }

    var topOfChartY = this.canvasPaddingTop;
    var bottomOfChartY = this.canvasHeight - this.canvasPaddingBottom;

    // Print Date Lines
    var dateLine = $('<line></line>')
        .attr("x1", x + this.candleWidth / 2)
        .attr("y1", topOfChartY)
        .attr("x2", x + this.candleWidth / 2)
        .attr("y2", bottomOfChartY);
    $(this.id + ' .dateLines').append(dateLine);

    // Print border line
    var dateBorderLine = $('<line></line>')
        .addClass("border")
        .attr("x1", 35)
        .attr("y1", bottomOfChartY)
        .attr("x2", this.canvasWidth)
        .attr("y2", bottomOfChartY);
    $(this.id + ' .dateLines').append(dateBorderLine);
}


/**
 * Print vertical line following mouse cursor
 */
NinjaChart.prototype.printXCursor = function() {
    if (this.readOnly) {
        return;
    }

    this.xCursor.find('rect')
        .attr("height", this.dateIndHeight)
        .attr("y", this.canvasHeight - this.dateIndHeight);
    this.xCursor.find('line')
        .attr("y2", this.canvasHeight)
        .attr("y1", this.canvasPaddingTop);
    this.xCursor.find('text')
        .attr("y", this.canvasHeight - this.dateIndFontSize / 2);
}

NinjaChart.prototype.printLastPriceInd = function() {
    this.lastPrice = $(this.id + ' .lastPrice');

    var prices = this.priceData.prices;
    var latestPrice = prices[prices.length - 1];
    var latestClosingPrice = latestPrice.c;
    var priceY = this.priceToY(latestClosingPrice);

    var line = $('<line></line>')
        .attr("x1", 35)
        .attr("x2", this.canvasWidth)
        .attr("y1", priceY)
        .attr("y2", priceY)

    var textBox = $('<rect></rect>')
        .attr("x", 0)
        .attr("y", priceY - 7.5)
        .attr("width", 35)
        .attr("height", 15);

    var text = $('<text></text>')
        .attr("x", 2)
        .attr("y", priceY + 4)
        .html(latestClosingPrice.toFixed(this.getDecimalPlaces(latestClosingPrice)));

    this.lastPrice.append(line);
    this.lastPrice.append(textBox);
    this.lastPrice.append(text);
}

/**
 * Print columns containing document and has mouse hover
 */
NinjaChart.prototype.printColumn = function(x, price) {
    if (this.readOnly) {
        return;
    }

    var year = price.d.substr(0, 4);
    var month = price.d.substr(4, 2);
    var day = price.d.substr(6, 2);

    var col = $('<rect></rect>');
    col.attr("x", x - (this.candleMargin / 2))
        .attr("y", 0)
        .attr("width", this.candleWidth + this.candleMargin)
        .attr("height", this.canvasHeight)
        .attr("date", month+"-"+day+"-"+year)
        .attr("o", price.o)
        .attr("h", price.h)
        .attr("l", price.l)
        .attr("c", price.c)
        .attr("v", price.v)
        .attr("up", price.c > price.o);
    $(this.id + ' .cols').append(col);
}

NinjaChart.prototype.priceToY = function(price) {
    return (this.chartHeight - (price - this.lowest) * this.canvasRatio) + this.canvasPaddingTop;
}

/**
 * Print candle bar
 */
NinjaChart.prototype.printCandleBar = function(x, price) {
    var candleHeight = Math.abs(Math.abs(price.o) - Math.abs(price.c)) * this.canvasRatio;
    var y = this.priceToY(Math.min(price.c, price.o)) - candleHeight;
    //var y = (this.chartHeight - (Math.min(price.c, price.o) - this.lowest) * this.canvasRatio) - candleHeight + this.canvasPaddingTop;
    var clazz = price.c > price.o ? 'up' : 'down';
    var candle = $('<rect></rect>')
        .attr("x", x)
        .attr("y", y)
        .attr("width", this.candleWidth)
        .attr("height", candleHeight == 0 ? 0.1 : candleHeight)
        .addClass(clazz);
    $(this.id + ' .candles').append(candle);
}

/**
 * Print candle wick
 */
NinjaChart.prototype.printCandleWick = function(x, price) {
    var clazz = price.c > price.o ? 'up' : 'down';

    var x1 = x + this.candleWidth / 2;
    var x2 = x1;
    var y1 = this.chartHeight - (price.l - this.lowest) * this.canvasRatio + this.canvasPaddingTop;
    var y2 = this.chartHeight - (price.h - this.lowest) * this.canvasRatio + this.canvasPaddingTop;

    var wick = $('<line></line>')
        .attr("x1", x1)
        .attr("y1", y1)
        .attr("x2", x2)
        .attr("y2", y2)
        .addClass(clazz);
    $(this.id + ' .wicks').append(wick);
}

/**
 * Initialize interactive events
 */
NinjaChart.prototype.initInteraction = function() {
    if (this.readOnly) {
        return;
    }

    var dateInd = $(this.id + " .xCursor text");
    var priceMeta = $(this.id +" .priceMeta");

    // Hover columns
    $(this.id + " .cols rect").hover(function() {
        var col = $(this);
        var colX = parseFloat(col.attr("x"));
        var width = parseFloat(col.attr("width"));
        var dateIndX = (width / 2) + colX;

        dateInd.html(col.attr("date"));

        var obj = $('#chart .xCursor');
        obj.attr("transform", "translate(" + dateIndX + ",0)");

        priceMeta.find(".open").html(col.attr("o"));
        priceMeta.find(".high").html(col.attr("h"));
        priceMeta.find(".low").html(col.attr("l"));
        priceMeta.find(".close").html(col.attr("c"));
        priceMeta.find(".volume").html(col.attr("v"));
        if (col.attr("up") == "true") {
            priceMeta.addClass("up");
            priceMeta.removeClass("down");
        } else {
            priceMeta.addClass("down");
            priceMeta.removeClass("up");
        }
    });
}

NinjaChart.prototype.addChart = function(chart) {
    this.extraCharts.push(chart);

    var g = $("<g></g>").addClass(chart.className);
    g.insertBefore(this.id + " .wicks");
}

