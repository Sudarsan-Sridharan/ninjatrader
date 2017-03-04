define(['jquery', '../status/statusitem'], function ($, StatusItem) {

    function Status(divId) {
        this.container = $(divId);
        this.statusItems = [];
    }

    Status.prototype.show = function(msg) {
        var statusItem = new StatusItem(this.container, msg).show();
        this.statusItems.push();
        return statusItem;
    };

    Status.prototype.quickShow = function(msg) {
        var statusItem = new StatusItem(this.container, msg).quickShow();
        return statusItem;
    };

    Status.prototype.clearAll = function() {
        for (var i in this.statusItems) {
            this.statusItems[i].remove();
        }
        this.statusItems = [];
    };

    return Status;
});
