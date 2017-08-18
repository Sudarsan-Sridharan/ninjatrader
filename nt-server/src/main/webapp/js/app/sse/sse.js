define(['jquery', 'require',
    'app/status/status',
    'app/auth/token-auth'], function ($, require) {

    var Status = require("app/status/status");
    var TokenAuth = require("app/auth/token-auth");

    var MAX_RECONNECT_DELAY = 60000 * 10; // 10 minutes

    var isConnected = false; // Allow only 1 connection
    var eventSource;
    var sseUrl = context.serviceHost + "/sse";
    var status;
    var statusItem;

    function Sse() {
        status = new Status("#status");
        statusItem = status.newStatusItem();
        this.connectDelaySeconds = 1;
    }

    // Connect to SSE Server
    Sse.prototype.connect = function() {
        // prevent multiple connections
        if (isConnected) {
            return this;
        }
        isConnected = true;

        var that = this;
        try {
            eventSource = new EventSource(TokenAuth.addAuthQueryParam(sseUrl));
        } catch (e) {
            // hide error. Just reconnect.
        }

        // On connect error,
        eventSource.onerror = function() {
            // Close connection
            isConnected = false;
            this.close();

            that._reconnect();
        };

        eventSource.onopen = function() {
            statusItem.remove();
        };

        return this;
    };

    Sse.prototype.addListener = function(event, listener) {
        eventSource.addEventListener(event, listener);
        return this;
    };

    Sse.prototype.onMessage = function(dostuff) {
        eventSource.onmessage = dostuff;
        return this;
    };

    /**
     * Attempt to connect with increasing delay.
     */
    Sse.prototype._reconnect = function() {
        var that = this;
        setTimeout(function() { that.connect() }, this.connectDelaySeconds * 1000);

        statusItem.show("Error connecting to server. Retrying in " + parseInt(this.connectDelaySeconds) + " seconds.");

        // Increase delay
        this.connectDelaySeconds = (this.connectDelaySeconds * 1.2) + 2;

        // Set max delay to 10 minutes
        if (this.connectDelaySeconds > MAX_RECONNECT_DELAY) {
            this.connectDelaySeconds = MAX_RECONNECT_DELAY;
        }
    };

    return Sse;
});
