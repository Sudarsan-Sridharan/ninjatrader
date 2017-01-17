define(function() {
    function Query(symbol, timeframe, from, to) {
        this.symbol = symbol;
        this.timeframe = timeframe;
        this.from = from;
        this.to = to;
        this.periodMap = []; // Map of <IndicatorName, [periods]>
    }

    Query.prototype.setSymbol = function(symbol) {
        this.symbol = symbol;
        return this;
    };

    Query.prototype.setTimeframe = function(timeframe) {
        this.timeframe = timeframe;
        return this;
    };

    Query.prototype.setFrom = function(from) {
        this.from = from;
        return this;
    };

    Query.prototype.setTo = function(to) {
        this.to = to;
        return this;
    };

    Query.prototype.setPeriods = function(indicatorName, periods) {
        this.periodMap[indicatorName] = periods;
        return this;
    }

    Query.prototype.restUrl = function(indicatorName) {
        var restUrl = "/" + this.symbol + "?";
        if (this.timeframe) {
            restUrl += "&timeframe=" + this.timeframe;
        }
        if (this.from) {
            restUrl += "&from=" + this.from;
        }
        if (this.to) {
            restUrl += "&to=" + this.to;
        }
        if (indicatorName && this.periodMap[indicatorName]) {
            var periods = this.periodMap[indicatorName]
            for (var i in periods) {
                restUrl += "&period=" + periods[i];
            }
        }
        return restUrl;
    };
    return Query;
});