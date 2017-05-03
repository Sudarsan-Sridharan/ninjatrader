requirejs(['../common'], function () {
    requirejs(['jquery', 'app/dashboard/dashboard'], function($, Dashboard) {

        $(document).ready(function() {
            var dashboard = new Dashboard("#dashboard");

            dashboard.load();
        });
    });
});