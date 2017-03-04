define(['jquery', 'require', '../scanner/scanner'], function ($, require, Scanner) {

    $(document).ready(function() {

        // Stock Scanner Panel
        var scanner = new Scanner("#scanner .panelContent");
        scanner.scan();
        $("#scanner .refreshBtn").click(function() {
            $("#scanner .panelAction button").prop("disabled", true);
            var scanCompleteCallback = function() {
                $("#scanner .panelAction button").prop("disabled", false);
            };
            scanner.scan(scanCompleteCallback);
        });

        // Admin Panel
        $("#admin .importQuotesBtn").click(function() {
            $(this).prop("disabled", true);
            var importQuotesBtn = $(this);

            $.ajax({
                url: context.serviceHost + "/task/import-pse-trader-quotes",
                type: "POST",
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=utf-8"
            }).always(function() {
                importQuotesBtn.prop("disabled", false);
            });
        });
    });
});
