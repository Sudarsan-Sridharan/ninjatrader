define(["d3"], function(d3) {

    Date.monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    Date.dbFormat = d3.timeFormat("%Y%m%d");
    Date.humanFormat = d3.timeFormat("%m-%d-%Y");
    Date.parseDbFormat = d3.timeParse("%Y%m%d");

    Date.nextWeekDay = function(date) {
        var nextWeekDay = new Date(date);
        var dayOfWeek = date.getDay();
        switch(dayOfWeek) {
            case 5: nextWeekDay.setDate(nextWeekDay.getDate() + 3); break;
            case 6: nextWeekDay.setDate(nextWeekDay.getDate() + 2); break;
            default: nextWeekDay.setDate(nextWeekDay.getDate() + 1); break;
        }
        return nextWeekDay;
    };

    Date.prototype.toDbFormat = function() {
        return Date.dbFormat(this);
    };

    Date.prototype.toHumanFormat = function() {
        return Date.humanFormat(this);
    };

    return Date;
});