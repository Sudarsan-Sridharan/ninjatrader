define(function() {

    function IchimokuMeanStack(maxSize) {
        this.maxSize = maxSize;
        this.prices = [];
        this.highest = 0;
        this.lowest = Number.MAX_SAFE_INTEGER;
    }

    IchimokuMeanStack.prototype.addPrice = function(price) {
        this.prices.push(price);

        // Get highest and lowest;
        this.highest = Math.max(this.highest, price.h);
        this.lowest = Math.min(this.lowest, price.l);

        while (this.prices.length > this.maxSize) {
            var removedPrice = this.prices.shift();

            // If removed price has highest value, search stack for new highest value
            if (removedPrice.h == this.highest) {
                var priceHighs = this.prices.map(function(price) { return price.h });
                this.highest = Math.max.apply(null, priceHighs);
            }

            // If removed price has lowest value, search stack for new lowest value
            if (removedPrice.l == this.lowest) {
                var priceLows = this.prices.map(function(price) { return price.l })
                this.lowest = Math.min.apply(null, priceLows);
            }
        }
    };

    IchimokuMeanStack.prototype.getValue = function() {
        if (this.prices.length == this.maxSize) {
            return (this.highest + this.lowest) / 2;
        }
        return Number.NaN;
    };

    IchimokuMeanStack.prototype.clear = function() {
        this.prices = [];
        this.highest = 0;
        this.lowest = Number.MAX_SAFE_INTEGER;
    };

    return IchimokuMeanStack;
});
