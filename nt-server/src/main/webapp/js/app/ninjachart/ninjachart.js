var monthNames = ['', 'Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];

function NinjaChart(divId, readOnly) {
    this.id = '#' + divId;
    this.ajaxUrl = "price";
    this.readOnly = readOnly;
    this.stockMeta = new StockMeta();

    // Chart Container
    this.container = $(this.id);

    // Candle Settings
    this.candleWidth = 4;
    this.candleMargin = 3;
    this.columnWidth = 7;

    // Chart Settings
    this.sidebarWidth = 40;
    this.canvasPaddingTop = 50;
    this.canvasPaddingBottom = 30;
    this.canvasHeight = 400;
    this.canvasWidth = 1200;
    this.chartHeight = this.canvasHeight - this.canvasPaddingTop - this.canvasPaddingBottom;

    // Date Indicator Settings
    this.dateIndHeight = 20;
    this.dateIndFontSize = 10;

    this.extraCharts = [];

    this.priceSidebar = new PriceSidebar(this);

    // Create the canvas
    this.initCanvas();
};

/**
 * Create the canvas
 */
NinjaChart.prototype.initCanvas = function() {
    $(this.id).addClass("ninjaChart");

    var table = $("<table></table>");
    var row = $("<tr></tr>");
    var menuPanel= $("<td></td>").addClass("menuPanel").attr("colspan", 10);
    var sidePanel = $("<td></td>").addClass("sidePanel");
    var chartPanel = $("<td></td>").addClass("chartPanel");
    var bottomPanel = $("<td></td>").attr("colspan", 10);

    var metaPanel = $("<div></div>").addClass("meta");

    // Menu Panel
    table.append(row);
    row.append(menuPanel);
    menuPanel.append(metaPanel);
    metaPanel.append(this.stockMeta.element());

    // Side and Chart Panel
    row = $("<tr></tr>");
    table.append(row);
    sidePanel.append(this.priceSidebar.createElement());
    row.append(sidePanel).append(chartPanel);

    // Bottom Panel
    row = $("<tr></tr>");
    table.append(row);
    row.append(bottomPanel);

    this.container.append(table);

    var outerSvg = $("<svg></svg>")
        .attr("width", this.canvasWidth)
        .attr("height", this.canvasHeight)
        .addClass("outer");
    this.container.append(outerSvg);

    // this.addGroup(outerSvg, "priceLines");
    // this.addGroup(outerSvg, "prices");
    // this.addGroup(outerSvg, "lastPrice");
    // this.addGroup(outerSvg, "scroll");

    var innerSvg = $("<svg></svg>")
        .attr("viewBox", "0 0 " + this.canvasWidth + " " + this.canvasHeight)
        .attr("preserveAspectRatio", "xMidYMid slice")
        .addClass("inner");
    outerSvg.append(innerSvg);

    this.addGroup(innerSvg, "cols");
    this.addGroup(innerSvg, "dateLines");
    this.addGroup(innerSvg, "datelabels");
    this.addGroup(innerSvg, "charts");
    this.addGroup(innerSvg, "xCursor");
    this.addGroup(innerSvg, "yCursor");

    this.cols = $(this.id + ' .cols');
    this.prices = $(this.id + ' .prices');
    this.dateLines = $(this.id + ' .dateLines');
    this.dateLabels = $(this.id + ' .datelabels');
    this.lastPrice = $(this.id + ' .lastPrice');
    this.charts = $(this.id + ' .charts');

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
            .addClass("dateInd");
        this.xCursor.append(xCursorLine);
        this.xCursor.append(xCursorBox);
        this.xCursor.append(xCursorText);
    }
};

/**
 * Update the chart w/ new boardlot document
 * @param symbol
 * @param timeFrame
 */
NinjaChart.prototype.showChart = function(symbol, timeFrame) {
    this.symbol = symbol;

    var timeFrameUrlPath = '';
    if (timeFrame == '1D') {
        timeFrameUrlPath = '/daily';
    } else if (timeFrame == '1W') {
        timeFrameUrlPath = '/weekly';
    }

    var thisInstance = this;

    $.get(this.ajaxUrl + "/" + symbol + timeFrameUrlPath, function(priceResponse) {
        thisInstance.showChartWithData(symbol, timeFrame, priceResponse);
    });
};

NinjaChart.prototype.showChartWithData = function(symbol, timeFrame, priceData) {
    this.symbol = symbol;
    this.priceData = priceData;
    this.lowest = priceData.summary.l * 0.9; // give more headroom
    this.highest = priceData.summary.h * 1.10; // give more headroom
    this.diff = this.highest - this.lowest;
    this.canvasRatio = this.chartHeight / this.diff;

    this.clearChart();
    this.printChart();

    // Print other charts (e.g. Ichimoku)
    for (var j in this.extraCharts) {
        this.extraCharts[j].showChart(symbol, timeFrame, priceData);
    }

    this.prepareChartViewBox(priceData);

    // Refresh to display SVG stuff
    this.refreshChart();
};

NinjaChart.prototype.prepareChartViewBox = function(priceData) {
    var numOfFutureBars = 30; // For Ichimoku future
    var totalChartWidth = this.columnWidth * (priceData.size + numOfFutureBars);
    var excessWidth = totalChartWidth - this.canvasWidth;
    excessWidth = excessWidth < 0 ? 0 : excessWidth;

    $(this.id + " .inner").removeAttr("viewBox"); // Remove attribute first to fix lowercase viewbox bug.
    $(this.id + " .inner").attr("viewBox", excessWidth + " 0 " + this.canvasWidth + " " + this.canvasHeight);
};

NinjaChart.prototype.refreshChart = function() {
    var canvasSvg = $(this.id + " .outer");
    canvasSvg.html(canvasSvg.html());

    // Init interactions
    this.initInteraction();
};

/**
 * Clear the boardlot chart
 */
NinjaChart.prototype.clearChart = function() {
    $(this.id + ' .cols').html('');
    $(this.id + ' .prices').html('');
    // $(this.id + ' .priceLines').html('');
    $(this.id + ' .dateLines').html('');
    $(this.id + ' .dateLabels').html('');
    $(this.id + ' .lastPrice').html('');

    this.stockMeta.clear();

    // Clear other charts (e.g. Ichimoku)
    for (var i in this.extraCharts) {
        this.extraCharts[i].clearChart();
    }
};

NinjaChart.prototype.addGroup = function(container, className) {
    var g = $("<g></g>").addClass(className);
    container.append(g);
};

/**
 * Print the boardlot chart
 */
NinjaChart.prototype.printChart = function() {
    // this.printPriceIndicators();
    this.printXCursor();
    this.printLastPriceInd();
    this.priceSidebar.show(this.priceData.summary.l, this.priceData.summary.h);

    var x = 0;
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

        x += this.columnWidth;
    }
};

/**
 * Print price indicators along the Y-axis
 */
NinjaChart.prototype.printPriceIndicators = function() {
    var pip = getBaseUnit(this.priceData.summary.o);

    var minMargin = 30; // minimum px margin between price indicators
    var marginBetweenPips = Math.abs((pip - (pip * 2)) * this.canvasRatio); // margin between 2 pips

    var decimalPlaces = getDecimalPlaces(this.priceData.summary.o);
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
        .attr("x1", this.sidebarWidth)
        .attr("y1", this.canvasPaddingTop)
        .attr("x2", this.sidebarWidth)
        .attr("y2", this.canvasHeight - this.canvasPaddingBottom);
    $(this.id + ' .priceLines').append(borderLine);
};

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
        .attr("x1", 0)
        .attr("y1", bottomOfChartY)
        .attr("x2", this.canvasWidth)
        .attr("y2", bottomOfChartY);
    $(this.id + ' .dateLines').append(dateBorderLine);
};

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
};

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
        .attr("y2", priceY);

    var textBox = $('<rect></rect>')
        .attr("x", 0)
        .attr("y", priceY - 7.5)
        .attr("width", 35)
        .attr("height", 15);

    var text = $('<text></text>')
        .attr("x", 2)
        .attr("y", priceY + 4)
        .html(latestClosingPrice.toFixed(getDecimalPlaces(latestClosingPrice)));

    this.lastPrice.append(line);
    this.lastPrice.append(textBox);
    this.lastPrice.append(text);
};

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
    col.attr("x", x)
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
};

NinjaChart.prototype.priceToY = function(price) {
    return (this.chartHeight - (price - this.lowest) * this.canvasRatio) + this.canvasPaddingTop;
};

/**
 * Initialize interactive events
 */
NinjaChart.prototype.initInteraction = function() {
    if (this.readOnly) {
        return;
    }

    var dateInd = $(this.id + " .xCursor text");
    var priceMeta = $(this.id + " .priceMeta");

    var thisInstance = this;

    // Hover columns
    $(this.id + " .cols rect").hover(function() {
        var col = $(this);
        var colX = parseFloat(col.attr("x"));
        var width = parseFloat(col.attr("width"));
        var dateIndX = (width / 2) + colX;

        dateInd.html(col.attr("date"));

        var dateLine = $('#chart').find('.xCursor');
        dateLine.attr("transform", "translate(" + dateIndX + ",0)");

        var isUp = col.attr("up") == "true" ? true : false;
        thisInstance.stockMeta.meta(
            thisInstance.symbol, col.attr("o"), col.attr("h"), col.attr("l"), col.attr("c"),  col.attr("v"), isUp);

    });

    // $(this.id + " .inner").mousedown(function(e) {
    //     thisInstance.startScrollX = e.pageX;
    // }).mousemove(function(e) {
    //     thisInstance.scrollChart(e)
    // });
};

NinjaChart.prototype.scrollChart = function(e) {
    // if (e.which == 1) {
    //     if (!this.chart) {
    //         this.chart = $(this.id + " .inner");
    //     }
    //     // var displacementX = this.startScrollX - e.pageX;
    //
    //
    //     // console.log("Scroll: " + this.startScrollX + " --- " + );
    // }
};

NinjaChart.prototype.addChart = function(chart) {
    this.extraCharts.push(chart);
    var g = $("<g></g>").addClass(chart.className);
    this.charts.append(g);
};

