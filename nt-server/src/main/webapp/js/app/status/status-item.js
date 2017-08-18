define(['jquery'], function ($) {

    function StatusItem(parentContainer, msg) {
        this.parentContainer = parentContainer;
        this.container = $('<div class="statusItem"></div>');
        this.statusMsg = $('<div class="msg">' + msg + '</div>');
        this.closeBtn = $('<div class="close">X</div>');

        this.container.append(this.closeBtn);
        this.container.append(this.statusMsg);

        // Close Button click
        var that = this;
        this.closeBtn.click(function() {
            that.remove();
        });
    }

    StatusItem.prototype.show = function(msg) {
        clearTimeout(this.timeoutId);
        this.container.show();
        if (msg) {
            this.msg(msg);
        }

        this.parentContainer.append(this.container);
        return this;
    };

    StatusItem.prototype.quickShow = function(msg) {
        this.show(msg);

        var that = this;
        this.timeoutId = setTimeout(function() {
            that.remove();
        }, 4000);
        return this;
    };

    StatusItem.prototype.msg = function(msg) {
        this.statusMsg.html(msg);
        return this;
    };

    StatusItem.prototype.remove = function() {
        this.container.fadeOut(200, function() {
            $(this).remove();
        });
        return this;
    };

    return StatusItem;
});
