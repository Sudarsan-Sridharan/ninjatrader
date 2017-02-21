define(function() {

    function EmaCalcStack(maxSize) {
        this.maxSize = maxSize;
        this.prices = [];
        this.emaValue = Number.NaN;
        this.multiplier = 2.0 / (maxSize + 1);
    }

    EmaCalcStack.prototype.addPrice = function(price) {
        this.prices.push(price);
        while (this.prices.length > this.maxSize) {
            this.prices.shift();
        }

        // for the first value, get the average
        if (isNaN(this.emaValue)) {
            if (this.prices.length == this.maxSize) {
                var total = 0;
                for (var i in this.prices) {
                    total += this.prices[i].c;
                }
                this.emaValue = total / this.maxSize;
            }
        } else {
            // for succeeding values, smooth the curve.
            this.emaValue = (price.c - this.emaValue) * this.multiplier + this.emaValue;
        }
    };

    EmaCalcStack.prototype.getValue = function() {
        return this.emaValue;
    };

    return EmaCalcStack;
});
