define(function() {
    function Color() {
        this.counter = 0;
        this.colors = [
            "#ff4351",
            "#49e845",
            "#7b72e9",
            "#feae1b",
            "#55dae1",
            "#fd6631",
            "#ffd426",
            "#db49d8",
            "#fc880f",
            "#1b9af7",
            "#a5de37",
            "#ed4694",
            "#6ff467",
            "#b9acf4",
            "#ffd728",
            "#ff667a",
            "#80edf0",
            "#fe9949",
            "#fee83a",
            "#ec6eec",
            "#fec418",
            "#94e6fe",
            "#f668ca",
            "#d1ef51"
        ];
    }

    Color.prototype.next = function() {
        var color = this.colors[this.counter];
        this.counter++;
        this.counter %= this.colors.length;
        return color;
    };

    return Color;
});