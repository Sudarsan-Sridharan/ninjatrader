define(['jquery', 'require',
    'app/status/status',
    'app/dashboard/panel/adminpanel',
    'app/dashboard/panel/algolistpanel',
    'app/dashboard/panel/scannerpanel'], function ($, require) {

    var Status = require("app/status/status");
    var AdminPanel = require("app/dashboard/panel/adminpanel");
    var AlgoListPanel = require("app/dashboard/panel/algolistpanel");
    var ScannerPanel = require("app/dashboard/panel/scannerpanel");

    function Dashboard(containerId) {
        this.container = $(containerId);
        this.status = new Status("#status");
        this.panels = $('<div id="panels"></div>');

        this.container.append(this.panels);
    }

    Dashboard.prototype.load = function()  {
        this.panels.append(new ScannerPanel("1111").node())
            .append(new ScannerPanel("2222").node())
            .append(new AdminPanel(this.status).node())
            .append(new AlgoListPanel(this.status).node());
    };

    return Dashboard;
});
