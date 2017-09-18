define(function() {

    function NumUtil() {

    }

    NumUtil.toPcnt = function(number) {
        number = number * 100;
        return number.toFixed(2);
    };

    NumUtil.withComma = function (number) {
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    return NumUtil;
});