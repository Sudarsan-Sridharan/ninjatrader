define(function() {

    function BoardLot() {

    }

    BoardLot.prototype.getBaseUnit = function(price) {
        if (price > 1000) return 5;
        if (price > 10) return 1;
        if (price > 3) return 0.5;
        if (price > 1) return 0.05;
        if (price > 0.1) return 0.01;
        if (price > 0.01) return 0.001;
        if (price > 0.001) return 0.0005;
        return 0.00005;
    };

    BoardLot.prototype.getDecimalPlaces = function(price) {
        if (price >= 1000) return 0;
        if (price >= 0.01) return 2;
        if (price >= 0.0001) return 4;
        return 5;
    };

    return BoardLot;
});