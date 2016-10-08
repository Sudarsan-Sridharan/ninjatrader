define(function() {
    function candle(date, open, high, low, close, volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    candle.prototype = {
        print: function() {
            console.log("CANDLE: " + this.open + " " + this.close);
        }
    }

    return candle;
});