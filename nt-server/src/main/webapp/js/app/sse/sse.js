define(['jquery', 'require', 'pusher',
    'app/status/status',
    'app/auth/token-auth'], function ($, require, Pusher) {

    Pusher.logToConsole = true;

    var pusher = new Pusher('58435a6af88c4d4ce9d4', {
        cluster: 'ap1',
        encrypted: true
    });
    var channel;
    var callbacks = [];

    function Sse() {

    }

    Sse.subscribe = function(callback) {
        callbacks.push(callback);

        if (!channel) {
            channel = pusher.subscribe("ADMIN");
            channel.bind("scan-update", function(data) {
                var result = JSON.parse(data.message);
                for (var i in callbacks) {
                    callbacks[i](result);
                }
            });
        }
    };

    return Sse;
});
