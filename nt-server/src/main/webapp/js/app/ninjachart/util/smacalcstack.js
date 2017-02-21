define(function() {

    function SmaCalcStack(maxSize) {
        this.maxSize = maxSize;
        this.prices = [];
        this.total = 0;
    }

    SmaCalcStack.prototype.addPrice = function(price) {
        // Collect total
        this.prices.push(price);
        this.total += price.c;

        // Remove excess
        while (this.prices.length > this.maxSize) {
            var removedPrice = this.prices.shift();
            this.total -= removedPrice.c;
        }
    };

    SmaCalcStack.prototype.getValue = function() {
        if (this.prices.length == this.maxSize) {
            return this.total / this.maxSize;
        }
        return Number.NaN;
    };

    return SmaCalcStack;
});
