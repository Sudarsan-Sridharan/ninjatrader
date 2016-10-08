define(function (require) {
    // Load any app-specific modules
    // with a relative require call,
    // like:
    var messages = require('./messages');

    console.log(messages.getHello());
});