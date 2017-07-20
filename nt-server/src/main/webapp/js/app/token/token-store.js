define(['cookie'], function (Cookie) {

    var tokenCookieName = "_bnt";
    var expiryDays = 30;

    function TokenStore() {
    }

    TokenStore.get = function() {
        console.log("Getting cookie: " + tokenCookieName);
        return Cookie.get(tokenCookieName);
    };

    TokenStore.set = function(token) {
        console.log("Storing cookie: " + token);
        Cookie.set(tokenCookieName, token, { expires: expiryDays });
        console.log("Verify stored: " + TokenStore.get())
    };

    return TokenStore;
});
