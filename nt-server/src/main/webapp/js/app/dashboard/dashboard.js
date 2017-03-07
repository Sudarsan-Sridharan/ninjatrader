define(['jquery', 'require', '../scanner/scanner', '../status/status'], function ($, require, Scanner, Status) {

    $(document).ready(function() {
        var status = new Status("#status");

        // Stock Scanner Panel
        var scanner = new Scanner("#scanner .panelContent");
        var algoSelector = $("#scanner .algo");

        scanner.scan(algoSelector.val());
        $("#scanner .refreshBtn").click(function() {
            $("#scanner .panelAction button").prop("disabled", true);
            var scanCompleteCallback = function() {
                $("#scanner .panelAction button").prop("disabled", false);
            };
            scanner.scan(algoSelector.val(), scanCompleteCallback);
        });

        // Admin Panel
        $("#admin .importQuotesBtn").click(function() {
            $(this).prop("disabled", true);
            var importQuotesBtn = $(this);

            var statusItem = status.show("Importing quotes...");

            $.ajax({
                url: context.serviceHost + "/task/import-pse-trader-quotes",
                type: "POST",
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=utf-8"
            }).done(function() {
                statusItem.msg("Successfully imported quotes.").quickShow();
            }).fail(function(e) {
                statusItem.msg("Failed to import quotes: " + e.statusText).quickShow();
            }).always(function() {
                importQuotesBtn.prop("disabled", false);
            });
        });

    });
});
